package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.commonData.ConstantValues;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.network.socket.server.ClientHandler;

import java.io.IOException;
import java.util.*;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements ServerSideMessageListener {




    /**
     * Enumeration used to set the different game phases in order to check the validity pf a certain move
     * in a specific time
     */
    public enum GameState {
        PRELOBBY,
        NAMECHOICE,
        SIDECHOICE,
        SECRETCHOICE,
        PLACING,
        DRAWING,
        COLORCHOICE;
    }

    /**
     * instance of GameController declared in order to use design pattern Singleton
     */
    private static GameController instance;
    private int finalRoundCounter = -1;
    public int numPlayers = -1;
    private Game game;
    private final List<String> playersName = new ArrayList<>();
    private boolean isGameStarted = false;
    private HashMap<ClientHandler, String> SenderName = new HashMap<>();
    private final ArrayList<ClientHandler> connectedClients = new ArrayList<>();
    private ClientHandler firstPlayer;
    private int objectivesChosen;
    private HashMap<ClientHandler, ObjectiveCard[]> objectiveChoices = new HashMap<>();
    public GameState currentState;
    private HashMap<String, String> playersColour = new HashMap<>();
    private ClientHandler nextExpectedPlayer;
    private final ArrayList<ClientHandler> disconnectedClients = new ArrayList<>();



    /**
     * Constructor
     */
    private GameController() {
        currentState = GameState.PRELOBBY;
    }

    /**
     * Singleton usage of GameController
     *
     * @return instance of GameController
     */
    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }


    /**
     * Method to handle the try catch of the sendMessage without repeating it every time the controller
     * tries to send a message
     * @param sender is the reference to the client
     * @param mes is a server to client message
     */
    private void passMessage(ClientHandler sender, ServerToClientMessage mes){
        try {
            sender.sendMessage(mes);
        } catch (IOException e) {
            disconnectionHappened(sender);
        }
    }

    /**
     * Receives the findLobbyMessage by the client and sends back to it the message when the lobby is found
     *
     * @param mes    when a player is looking for a lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(FindLobbyMessage mes, ClientHandler sender) {
        synchronized (connectedClients) {//synchronized on connected clients because there could be a conflict in the handling of ChooseNameMessage
            connectedClients.add(sender);

            if (connectedClients.indexOf(sender) == 0)
                firstPlayer = sender;


            if (!currentState.equals(GameState.PRELOBBY)) {
                passMessage(sender, new LobbyFullMessage());
                connectedClients.remove(sender);
            } else
                passMessage(sender, new LobbyFoundMessage());
        }
    }

    /**
     * This method sets the chosen username by every player, if the name is the same of one of the other players, it sends
     * a NameNotAvailablaMessage to the client, or a NameChosenSuccessfullyMessage otherwise
     *
     * @param mes    is the name choosen by the player
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseNameMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (currentState.equals(GameState.PRELOBBY)) {
            String chosenName = mes.getName();
            SenderName.put(sender, chosenName);

            if (playersName.contains(chosenName)) {
                passMessage(sender, new NameNotAvailableMessage());
                return;

            }
            synchronized (connectedClients) {
                playersName.add(chosenName);
                //add(connectedClients.indexOf(sender),chosenName);
            }


            passMessage(sender, new NameChosenSuccessfullyMessage());

            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    try {
                        c.sendMessage(new GenericMessage(chosenName + " connected to the server"));
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            }
            if (sender == firstPlayer)
                passMessage(sender, new ChooseHowManyPlayersMessage());


            if (playersName.size() == numPlayers) //added !=-1 to be safe; and the index must be increased by 1: it can only be between 0  and 1
                startGame(firstPlayer);
        }
    }

    /**
     * This sets the number of player in the game
     *
     * @param mes    is used by the first player to choose how big is the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(NumberOfPlayersMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if ((numPlayers != -1 || sender != firstPlayer) || !currentState.equals(GameState.PRELOBBY)) {
            passMessage(sender, new ClientCantStartGameMessage());
        } else {
            this.numPlayers = mes.getNumber();
            synchronized (connectedClients) {
                if (connectedClients.size() > numPlayers) {//If there are for example 10 clients connected and max 3 players the other 7 must be disconnected
                    List<ClientHandler> toRemove = new ArrayList<>();//List used to avoid ConcurrentModificationException thrown by the list we iterate on
                    for (int i = numPlayers; i < connectedClients.size(); i++) {
                        passMessage(connectedClients.get(i), new LobbyFullMessage());
                        toRemove.add(connectedClients.get(i));
                    }
                    connectedClients.removeAll(toRemove);
                }
            }
            //create a new game with the chosen number of players
            this.game = new Game(numPlayers);

            passMessage(sender, new GenericMessage("The game will start when " + numPlayers + " players are connected"));

            //if the players connected are the same amount of the chosen number the proper game starts
            if (playersName.size() >= numPlayers) {
                startGame(sender);
            }
        }
    }

    /**
     * Method called to initialize the starting phase of the game when the all the players are connected with a valid name
     *
     * @param sender is the reference to who has sent the relative mes
     */

    private void startGame(ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (playersName.size() == numPlayers && sender == firstPlayer) {
            randomizePlayersOrder();
            try {
                game.startGame(playersName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            passMessage(firstPlayer, new GameStartingMessage(playersName, game.getStartingCardId(SenderName.get(firstPlayer)), game.getPlayerHand(SenderName.get(firstPlayer)), generateFieldUpdate(), game.getFirstCommon(), game.getSecondCommon()));
                currentState = GameState.SIDECHOICE;
                isGameStarted = true;
        } else if (playersName.size() != numPlayers) {
            passMessage(sender, new ServerCantStartGameMessage());
            System.out.println(numPlayers);
            System.out.println(playersName.get(0));
            System.out.println(playersName.get(1));
        } else if (!Objects.equals(sender, firstPlayer))
            passMessage(sender, new ClientCantStartGameMessage());

    }

    /**
     * Randomize the order of the playersName and of the ClientHandler associated with each of them before starting the game
     */
    private void randomizePlayersOrder() {
        Collections.shuffle(playersName);

        //from this line to the end, this is necessary to ordinate the clientHandler reference of players
        //with the new players order
        for (String playerName : playersName) {
            ClientHandler tmp = null;
            for (Map.Entry<ClientHandler, String> entry : SenderName.entrySet()) {
                if (entry.getValue().equals(playerName)) {
                    tmp = entry.getKey();
                    break;
                }
            }

            if (tmp != null) {
                SenderName.remove(tmp);
                SenderName.put(tmp, playerName);
            }
        }
        connectedClients.clear();
        for (Map.Entry<ClientHandler, String> entry : SenderName.entrySet()) {
            connectedClients.add(entry.getKey());

        }

        //the reference of the first player with the new order
        firstPlayer = connectedClients.getFirst();
        nextExpectedPlayer = firstPlayer;
    }


    /**
     * This method receives the placing side of the starting card chose by the player and set it to his playing field
     *
     * @param mes    contains the choice regarding the side of the starting card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if(!nextExpectedPlayer.equals(sender)){
            passMessage(sender, new GenericMessage("You aren't allowed to choose the starting card side"));
            return;
        }

        if (currentState == GameState.SIDECHOICE) {
            String currentPlayerName = SenderName.get(sender);
            boolean startingPosition = mes.facingUp();
            try {
                game.setStartingCard(currentPlayerName, startingPosition);
            } catch (NotPlacedException | AlreadyPlacedException e) {
                throw new RuntimeException(e);
            }

            //update necessary to display the startingCard on the field
            passMessage(sender, new SuccessfulPlacementMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(0, 0, startingPosition, game.getStartingCardId(SenderName.get(sender))), generateFieldUpdate()));
            //to show the startingCard to the other players
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, new OtherPlayerTurnUpdateMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(0, 0, startingPosition, game.getStartingCardId(SenderName.get(sender))), generateFieldUpdate(), SenderName.get(sender)));
                }
            }

            //after choosing the startingCard side, the player has to choose his pawn's colour
            passMessage(sender, new ChooseColourMessage());
            currentState = GameState.COLORCHOICE;
            nextExpectedPlayer = sender;


        }
    }

    /**
     * @param mes    is the message containing the chosen color for the pawn
     * @param sender is the reference to who has sent the message
     */
    @Override
    public void handle(ChosenColourMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (!sender.equals(nextExpectedPlayer)) {
            passMessage(sender, new GenericMessage("You cannot choose the color now"));
            return;
        }

        if (currentState.equals(GameState.COLORCHOICE)) {
            String chosenColour = mes.getColour();

            if (!isColourAvailable(sender, chosenColour)) {
                passMessage(sender, new ColourAlreadyChosenMessage());
                return;
            } else playersColour.put(SenderName.get(sender), chosenColour);

            if (!isAColour(chosenColour)) {
                passMessage(sender, new NotAColourMessage());
                return;
            }

            //sets the chosen color in the scoretrack
            game.setPawnColour(SenderName.get(sender), chosenColour);

            if (connectedClients.indexOf(sender) + 1 == numPlayers) {//indexes must be increased by 1 because otherwise thy will rang from 0 tu numPlayers -1
                currentState = GameState.SECRETCHOICE;
                objectivesChosen = numPlayers;
                for (ClientHandler c : connectedClients) {
                    try {
                        objectiveChoices.put(c, game.dealSecretObjective(SenderName.get(c)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ObjectiveCard[] tmp = objectiveChoices.get(c);
                    passMessage(c, new SecretObjectiveChoiceMessage(tmp[0].getID(), tmp[1].getID()));
                }
                return;
            }

            int currIndex = connectedClients.indexOf(sender);
            int nextIndex = (currIndex + 1);
            if (currIndex < connectedClients.size() - 1) {
                ClientHandler nextSender = connectedClients.get(nextIndex);
                passMessage(nextSender, new GameStartingMessage(playersName,
                        game.getStartingCardId(SenderName.get(nextSender)), game.getPlayerHand(SenderName.get(nextSender)), generateFieldUpdate(), game.getFirstCommon(), game.getSecondCommon()));
                currentState = GameState.SIDECHOICE;
                nextExpectedPlayer = nextSender;
            }
        }
    }

    /**
     * Checks if a colour is valid or not
     *
     * @param chosenColour is the ANSI string of the chosen colour for the player's pawn
     * @return true if the chosenColour is a valid ANSI string colour, false otherwise
     */
    private boolean isAColour(String chosenColour) {

        switch (chosenColour) {
            case ConstantValues.ansiBlue:
            case ConstantValues.ansiRed:
            case ConstantValues.ansiGreen:
            case ConstantValues.ansiYellow:
                return true;
            default:
                return false;
        }

    }

    /**
     * Checks if the pawn colour has been already chosen by a previous player
     *
     * @param sender       is the reference of the client who has sent the message with the color choice
     * @param chosenColour is the pawn's colour
     * @return true if it hasn't been chosen yet, false otherwise
     */
    private boolean isColourAvailable(ClientHandler sender, String chosenColour) {
        for (ClientHandler c : connectedClients) {
            if (c != sender && playersColour.containsKey(SenderName.get(c))&&playersColour.get(SenderName.get(c)).equals(chosenColour))
                return false;
        }
        return true;
    }

    /**
     * This method handles the choice of the secretObjective by comparing the cardID in the message and the two cards presented to the player and then calls
     * the method in the game to set the choice
     *
     * @param mes    is the message with the secretObjective card the player chose between the twos dealt
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (currentState == GameState.SECRETCHOICE) {
            ObjectiveCard objectiveChosen = null;
            String currentPlayerName = SenderName.get(sender);
            ObjectiveCard[] objectives = objectiveChoices.get(sender);

            for (ObjectiveCard c : objectives) {
                if (mes.getID() == c.getID())
                    objectiveChosen = c;
            }

            // todo id check validity


            try {
                game.placeSecretObjective(currentPlayerName, objectiveChosen);
                objectivesChosen--;
            } catch (Exception e) {
                System.err.println("C");
            }

            if (objectivesChosen == 0) {
                passMessage(firstPlayer, new StartPlayerTurnMessage());
                currentState = GameState.PLACING;

                for (ClientHandler c : connectedClients) {
                    if (c != firstPlayer) {
                        passMessage(c, new GenericMessage("It's " + SenderName.get(firstPlayer) + " turn"));

                    }
                }
            }
        }

    }


    /**
     * This method gets the available placing positions from game and send them to the client
     *
     * @param mes    is the message used by the players for knowing where they can place a card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(RequestAvailablePositionsMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (!currentState.equals(GameState.PLACING) && !currentState.equals(GameState.DRAWING)) {
            passMessage(sender, new GenericMessage("You can't request these informations during this game pahse"));
            return;
        }
        String currentPlayerName = SenderName.get(sender);
        List<Point> availablePositions = null;

        try {
            availablePositions = game.getAvailablePoints(currentPlayerName);
        } catch (NotPlacedException e) {
            throw new RuntimeException(e);
        } catch (PlayerCantPlaceAnymoreException e) {//The appropriate response is now sent

            passMessage(sender, new PlayerCantPlayAnymoreMessage());

        }

        passMessage(sender, new AvailablePositionsMessage(availablePositions));

    }

    /**
     * This method receives the PlaceCardMessage and calls the PlaceCard game method to set the card
     *
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(PlaceCardMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if(!nextExpectedPlayer.equals(sender)){
            passMessage(sender, new GenericMessage("You aren't allowed to place the card"));
            return;
        }

        String currentPlayerName = SenderName.get(sender);

        if (currentState == GameState.PLACING) {
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, new GenericMessage(SenderName.get(sender) + " is playing his turn"));
                }
            }
            try {
                game.playCard(currentPlayerName, mes);
            } catch (NotPlacedException | AlreadyPlacedException | CardNotInHandException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughResourcesException e) {
                passMessage(sender, new NotEnoughResourcesMessage());
                return;
            } catch (InvalidPositionException I) {//Now the appropriate message is sent
                passMessage(sender, new IllegalPlacementPositionMessage());
                return;
            }

            passMessage(sender, new SuccessfulPlacementMessage(game.getPlayerVisibleSymbols(currentPlayerName), placingInfos(mes.getX(), mes.getY(), mes.isFacingUp(), mes.getID()), generateFieldUpdate()));
            currentState = GameState.DRAWING;
            nextExpectedPlayer = sender;

            try {
                game.getAvailablePoints(currentPlayerName);
            } catch (NotPlacedException e) {
                throw new RuntimeException(e);
            } catch (PlayerCantPlaceAnymoreException e) {
                passMessage(sender, new PlayerCantPlayAnymoreMessage());

            }

            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, new OtherPlayerTurnUpdateMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(mes.getX(), mes.getY(), mes.isFacingUp(), mes.getID()), generateFieldUpdate(), SenderName.get(sender)));

                }
            }

            if (game.isInFinalPhase() && finalRoundCounter == -1)
                EndGame(sender);
        }
    }

    /**
     * This method receives the DrawCardMessage and calls the drawCard game method to draw the card to end his turn after placing
     *
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(DrawCardMessage mes, ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if(!nextExpectedPlayer.equals(sender)){
            passMessage(sender, new GenericMessage("You aren't allowed to draw the card"));
            return;
        }

        String currentPlayerName = SenderName.get(sender);

        if (currentState == GameState.DRAWING) {

            try {
                game.drawCard(currentPlayerName, mes);
            } catch (EmptyDeckException e) {
                passMessage(sender, new EmptyDeckMessage());
                return;//added returns because without  them the player would end their turn without drawing
            } catch (NoVisibleCardException e) {
                passMessage(sender, new EmptyDrawnCardPositionMessage());
                return;//added returns because without  them the player would end their turn without drawing
            } catch (Exception e) { /////??????.
                throw new RuntimeException(e);
            }

            passMessage(sender, new SendDrawncardMessage(generateFieldUpdate(), game.getPlayerHand(SenderName.get(sender))));

            passMessage(sender, new EndPlayerTurnMessage());


            if (game.isInFinalPhase()){
                if(finalRoundCounter == -1){
                    EndGame(sender);
                } else finalRoundCounter--;
            }


            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, generateFieldUpdate());
                }
            }

            if (finalRoundCounter != 0) {
                int currentIndex = connectedClients.indexOf(sender);
                int nextIndex = (currentIndex + 1) % connectedClients.size();
                passMessage(connectedClients.get(nextIndex), new StartPlayerTurnMessage());
                currentState = GameState.PLACING;
                nextExpectedPlayer = connectedClients.get(nextIndex);
            } else EndGame(sender);
        }
    }


    /**
     * When the finalPhase starts a final counter is initialized to allow players to finish the current round and to play another additional round for each player.
     * After this the final point are calculated by calling game.gameOver and the winner is declared. After this the server sends the GameEndingMessage containing
     * the players and their totalPoints to each player
     *
     * @param sender is the reference to who has sent the relative mes
     */
    private void EndGame(ClientHandler sender) {

        for(ClientHandler c: disconnectedClients){
            if(sender == c )
                return;
        }

        if (game.isInFinalPhase()) {
            if (finalRoundCounter == -1) {
                finalRoundCounter = 2 * numPlayers - (connectedClients.indexOf(sender) + 1);//indexOfSender would be between 0 and numPlayers-1 without the (.. +1 and an extra round would be played)
            }
            if (finalRoundCounter == 0) {
                game.gameOver();
                HashMap<String, Integer> finalPlayerScore = game.getFinalScore();
                for (ClientHandler c : connectedClients) {
                    passMessage(c, new GameEndingMessage(finalPlayerScore));
                }
            }
        }
    }

    /**
     * @param mes    is used if the connection between the client and the server
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ClientTryReconnectionMessage mes, ClientHandler sender) {


    }

    /**
     * If a player disconnects from the game, the game instantly ends and the current score is sent to the client. If a player disconnects during the setup phase of the game,
     * before playing a single round and scoring points, this method ends the game and sends to the client the score with 0 points for each player connected in that moment.
     *
     * @param mes    when a player have to leave the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ClientDisconnectedVoluntarilyMessage mes, ClientHandler sender) {
        /*
        connectedClients.remove(sender);
        disconnectedClients.add(sender);
        try{
            game.removePlayer(currentPlayerName) //metodo che rimuoverà il player dalla lista degli attivi in game
        }catch(Exception e)
         */
        String disconnectedPlayerName = SenderName.get(sender);

        if (currentState != GameState.PRELOBBY) {
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, genericMessage("The player" + disconnectedPlayerName + "left the game"));

                }
            }
            game.gameOver();
            HashMap<String, Integer> finalPlayerScore = game.getFinalScore();
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, new GameEndingMessage(finalPlayerScore));
                }
            }
        } else {
            HashMap<String, Integer> score = new HashMap<>();
            for (ClientHandler c : connectedClients) {
                score.put(SenderName.get(c), 0);
            }
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, genericMessage("The player " + disconnectedPlayerName + " left the game before anyone played a single round"));
                    passMessage(c, new GameEndingMessage(score));
                }
            }
        }
    }

    /**
     * This method is called to send a message in any type of game phases to communicate something to the client
     *
     * @param message contains the string of the specific message the client has to receive
     * @return GenericMessage
     */
    private GenericMessage genericMessage(String message) {
        return new GenericMessage(message);
    }

    /**
     * @return ShareFieldUpdateMessage containing the update of the common ground after a certain player has played his turn
     */
    private SharedFieldUpdateMessage generateFieldUpdate() {
        return new SharedFieldUpdateMessage(game.getScoreTrack(), game.getResourceTop(),
                game.getGoldTop(), game.getVisibleCards());
    }

    /**
     * @param x    coordinate of the card
     * @param y    coordinate of the card
     * @param face of the card (on the face or on the back)
     * @param id   of the card
     * @return PlayerPlacedCardInformation containing all the information about the card just placed
     */
    private PlayerPlacedCardInformation placingInfos(int x, int y, boolean face, int id) {
        return new PlayerPlacedCardInformation(id, x, y, face);
    }

    /**
     * @return currentState of the GameController
     */
    public GameState getCurrentState() {
        return currentState;
    }

    @Override
    public void handle(Ping ping, ClientHandler sender) {
        sender.pingWasReceived();
    }

    /**
     * The listener is notified of a disconnection
     * @param clientHandler is the client who was disconnected
     */
    @Override
    public void disconnectionHappened(ClientHandler clientHandler) {
        //TODO: fully handle a disconnection
        disconnectedClients.add(clientHandler);
        connectedClients.remove(clientHandler);
        clientHandler.stopConnection();
    }
}
