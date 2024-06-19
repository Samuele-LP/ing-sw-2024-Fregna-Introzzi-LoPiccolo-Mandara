package it.polimi.ingsw.controller;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.view.ImmutableScoreTrack;

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
        PRE_LOBBY,
        SIDE_CHOICE,
        COLOR_CHOICE,
        SECRET_CHOICE,
        PLACING,
        DRAWING,
        END_FOR_DISCONNECTION
    }

    /**
     * instance of GameController declared in order to use design pattern Singleton
     */
    private static GameController instance;
    private int finalRoundCounter = -1;
    public int numPlayers = -1;
    private Game game;
    private final List<String> playersName = new ArrayList<>();
    private final HashMap<String, String> playersColour = new HashMap<>();
    private ClientHandler firstPlayer;
    private int objectivesChosen;
    private final HashMap<ClientHandler, ObjectiveCard[]> objectiveChoices = new HashMap<>();
    public GameState currentState;
    private ClientHandler nextExpectedPlayer;
    private final ArrayList<ClientHandler> connectedClients = new ArrayList<>();
    private final ArrayList<ClientHandler> softLockedClients = new ArrayList<>();
    private final HashMap<ClientHandler, String> SenderName = new HashMap<>();

    /**
     * Constructor
     */
    private GameController() {
        currentState = GameState.PRE_LOBBY;
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
     *
     * @param sender is the reference to the client
     * @param mes    is a server to client message
     */
    private void passMessage(ClientHandler sender, ServerToClientMessage mes) {
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
        synchronized (connectedClients) {//synchronized on connected clients because there could be a conflict in the handling of messages
            connectedClients.add(sender);

            if (connectedClients.indexOf(sender) == 0)
                firstPlayer = sender;


            if (!currentState.equals(GameState.PRE_LOBBY)) {
                passMessage(sender, new LobbyFullMessage());
                connectedClients.remove(sender);
            } else
                passMessage(sender, new LobbyFoundMessage());
        }
    }

    /**
     * This method sets the chosen username by every player, if the name is the same of one of the other players, it sends
     * a NameNotAvailableMessage to the client, or a NameChosenSuccessfullyMessage otherwise
     *
     * @param mes    is the name chosen by the player
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseNameMessage mes, ClientHandler sender) {
        if (!currentState.equals(GameState.PRE_LOBBY)) {
            passMessage(sender, new GenericMessage("You can't o that now"));
            return;
        }

        String chosenName = mes.getName();
        SenderName.put(sender, chosenName);

        if (playersName.contains(chosenName)) {
            passMessage(sender, new NameNotAvailableMessage());
            return;

        }
        synchronized (connectedClients) {
            playersName.add(chosenName);
        }


        passMessage(sender, new NameChosenSuccessfullyMessage());
        synchronized (connectedClients) {
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    try {
                        c.sendMessage(new GenericMessage(chosenName + " connected to the server"));
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            }
        }
        if (sender == firstPlayer && numPlayers == -1)
            passMessage(sender, new ChooseHowManyPlayersMessage());


        if (playersName.size() == numPlayers) {
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

        if ((numPlayers != -1 || sender != firstPlayer) || !currentState.equals(GameState.PRE_LOBBY)) {
            passMessage(sender, new ClientCantStartGameMessage());
        } else if (mes.getNumber() < 2 || mes.getNumber() > 4) {
            passMessage(sender, new GenericMessage("Invalid number of players, it must be between 2 and 4"));
        } else {
            this.numPlayers = mes.getNumber();
            //create a new game with the chosen number of players
            this.game = new Game(numPlayers);
            synchronized (connectedClients) {
                if (connectedClients.size() > numPlayers) {//If there are for example 10 clients connected and max 3 players the other 7 must be disconnected
                    List<ClientHandler> toRemove = new ArrayList<>();//List used to avoid ConcurrentModificationException thrown by the list we iterate on
                    for (int i = numPlayers; i < connectedClients.size(); i++) {
                        passMessage(connectedClients.get(i), new LobbyFullMessage());
                        toRemove.add(connectedClients.get(i));
                    }
                    connectedClients.removeAll(toRemove);
                }

                for (ClientHandler c : connectedClients) {
                    passMessage(c, new GenericMessage("The game will start when " + numPlayers + " players are connected."));
                }
            }
            //if the players connected are the same amount of the chosen number the proper game starts
            if (playersName.size() == numPlayers) {
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

        if (playersName.size() == numPlayers && sender == firstPlayer) {
            randomizePlayersOrder();
            try {
                game.startGame(playersName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            passMessage(firstPlayer, new GameStartingMessage(playersName, game.getStartingCardId(SenderName.get(firstPlayer)), game.getPlayerHand(SenderName.get(firstPlayer)), generateFieldUpdate(), game.getFirstCommonObjective(), game.getSecondCommonObjective(), SenderName.get(firstPlayer)));
            synchronized (connectedClients) {
                for (ClientHandler c : connectedClients) {
                    if (c != firstPlayer) {
                        passMessage(c, new GenericMessage(SenderName.get(firstPlayer) + " is choosing their starting card side"));
                    }
                }
            }
            currentState = GameState.SIDE_CHOICE;
        } else if (playersName.size() != numPlayers) {
            passMessage(sender, new ServerCantStartGameMessage());
            System.out.println(numPlayers);
            System.out.println(playersName);
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
        synchronized (connectedClients) {
            connectedClients.clear();
            for (Map.Entry<ClientHandler, String> entry : SenderName.entrySet()) {
                connectedClients.add(entry.getKey());

            }
            //the reference of the first player with the new order
            firstPlayer = connectedClients.getFirst();
        }
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

        if (!nextExpectedPlayer.equals(sender) || !currentState.equals(GameState.SIDE_CHOICE)) {
            passMessage(sender, new GenericMessage("At the moment you aren't allowed to choose the starting card side"));
            return;
        }

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
        //after choosing the startingCard side, the player has to choose his pawn's colour
        passMessage(sender, new ChooseColourMessage());
        synchronized (connectedClients) {
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, new OtherPlayerTurnUpdateMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(0, 0, startingPosition, game.getStartingCardId(SenderName.get(sender))), generateFieldUpdate(), SenderName.get(sender)));
                    passMessage(c, new GenericMessage(SenderName.get(sender) + " is choosing their colour"));
                }
            }
        }
        currentState = GameState.COLOR_CHOICE;
        nextExpectedPlayer = sender;


    }

    /**
     * @param mes    is the message containing the chosen color for the pawn
     * @param sender is the reference to who has sent the message
     */
    @Override
    public void handle(ChosenColourMessage mes, ClientHandler sender) {


        if (!sender.equals(nextExpectedPlayer) || !currentState.equals(GameState.COLOR_CHOICE)) {
            passMessage(sender, new GenericMessage("You cannot choose the color now"));
            return;
        }

        String chosenColour = mes.getColour();

        if (!isAColour(chosenColour)) {
            passMessage(sender, new NotAColourMessage());
            return;
        }
        if (!isColourAvailable(sender, chosenColour)) {
            passMessage(sender, new ColourAlreadyChosenMessage());
            return;
        } else playersColour.put(SenderName.get(sender), chosenColour);

        //sets the chosen color in the score track
        game.setPawnColour(SenderName.get(sender), chosenColour);
        SharedFieldUpdateMessage sharedFieldAfterColourChoice = generateFieldUpdate();
        synchronized (connectedClients) {
            if (connectedClients.indexOf(sender) + 1 == connectedClients.size()) {//If all connected players have completed their choices about side and colour
                currentState = GameState.SECRET_CHOICE;
                objectivesChosen = numPlayers;
                for (ClientHandler c : connectedClients) {
                    passMessage(c, sharedFieldAfterColourChoice);
                    try {
                        objectiveChoices.put(c, game.dealSecretObjective());
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
                for (ClientHandler c : connectedClients) {//Updates the colours for every player
                    passMessage(c, sharedFieldAfterColourChoice);
                }
                passMessage(nextSender, new GameStartingMessage(playersName,
                        game.getStartingCardId(SenderName.get(nextSender)), game.getPlayerHand(SenderName.get(nextSender)), sharedFieldAfterColourChoice, game.getFirstCommonObjective(), game.getSecondCommonObjective(), SenderName.get(firstPlayer)));
                for (ClientHandler c : connectedClients) {
                    if (c != nextSender) {
                        passMessage(c, new GenericMessage(SenderName.get(nextSender) + " is choosing their starting card side"));
                    }
                }
                currentState = GameState.SIDE_CHOICE;
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

        return switch (chosenColour) {
            case ConstantValues.ansiBlue, ConstantValues.ansiRed, ConstantValues.ansiGreen, ConstantValues.ansiYellow ->
                    true;
            default -> false;
        };

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
            if (c != sender && playersColour.containsKey(SenderName.get(c)) && playersColour.get(SenderName.get(c)).equals(chosenColour))
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

        if (!currentState.equals(GameState.SECRET_CHOICE)) {
            passMessage(sender, new GenericMessage("At the moment you can't choose the objective!"));
            return;
        }
        ObjectiveCard objectiveChosen = null;
        String currentPlayerName = SenderName.get(sender);
        ObjectiveCard[] objectives = objectiveChoices.get(sender);

        for (ObjectiveCard c : objectives) {
            if (mes.getID() == c.getID())
                objectiveChosen = c;
        }
        if(objectiveChosen==null){//this block won't be normally entered . It has been added as a safety measure
            ObjectiveCard[] objs=objectiveChoices.get(sender);
            passMessage(sender, new SecretObjectiveChoiceMessage(objs[0].getID(),objs[1].getID()));
            passMessage(sender, new GenericMessage("Invalid objective choice! Please choose again!"));
            return;
        }
        try {
            game.placeSecretObjective(currentPlayerName, objectiveChosen);
            objectivesChosen--;
            passMessage(sender, new GenericMessage("Objective chosen successfully!"));
        } catch (Exception e) {
            System.err.println("C");
        }

        if (objectivesChosen == 0) {
            passMessage(firstPlayer, new StartPlayerTurnMessage());
            currentState = GameState.PLACING;
            nextExpectedPlayer = firstPlayer;

            synchronized (connectedClients) {
                for (ClientHandler c : connectedClients) {
                    if (c != firstPlayer) {
                        passMessage(c, new GenericMessage("It's " + SenderName.get(firstPlayer) + "'s turn"));

                    }
                }
            }

        }

    }
    /**
     * This method receives the PlaceCardMessage and calls the PlaceCard game method to set the card
     *
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(PlaceCardMessage mes, ClientHandler sender) {

        if (!nextExpectedPlayer.equals(sender)) {
            passMessage(sender, new GenericMessage("You aren't allowed to place the card"));
            return;
        }

        String currentPlayerName = SenderName.get(sender);

        if (currentState == GameState.PLACING) {

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

            passMessage(sender, new SuccessfulPlacementMessage(game.getPlayerVisibleSymbols(currentPlayerName), placingInfos(mes.getX(), mes.getY(),
                    mes.isFacingUp(), mes.getID()), generateFieldUpdate()));
            currentState = GameState.DRAWING;
            nextExpectedPlayer = sender;

            try {
                List<Point> pos = game.getAvailablePoints(currentPlayerName);
                passMessage(sender, new AvailablePositionsMessage(pos));
            } catch (NotPlacedException e) {
                throw new RuntimeException(e);
            } catch (PlayerCantPlaceAnymoreException e) {
                passMessage(sender, new PlayerCantPlayAnymoreMessage());
                softLockedClients.add(sender);
            }

            synchronized (connectedClients) {
                for (ClientHandler c : connectedClients) {
                    if (c != sender) {
                        passMessage(c, new OtherPlayerTurnUpdateMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(mes.getX(), mes.getY(), mes.isFacingUp(), mes.getID()), generateFieldUpdate(), SenderName.get(sender)));
                    }
                }
            }
            if (softLockedClients.size() == numPlayers) {//If no one can play a card the game will have to end
                game.gameOver();
                for (ClientHandler c : connectedClients) {
                    passMessage(c, new GameEndingMessage(game.getScoreTrack(), game.getWinners()));
                }
                System.exit(1);
                return;
            }
            if (game.isInFinalPhase() && finalRoundCounter == -1) {
                EndGame(sender);
            }
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

        if (!nextExpectedPlayer.equals(sender) || !currentState.equals(GameState.DRAWING)) {
            passMessage(sender, new GenericMessage("You aren't allowed to draw the card"));
            return;
        }

        String currentPlayerName = SenderName.get(sender);

        try {
            game.drawCard(currentPlayerName, mes);
        } catch (EmptyDeckException e) {
            passMessage(sender, new EmptyDeckMessage());
            return;//added returns because without  them the player would end their turn without drawing
        } catch (NoVisibleCardException e) {
            passMessage(sender, new EmptyDrawnCardPositionMessage());
            return;//added returns because without  them the player would end their turn without drawing
        } catch (CardAlreadyPresentException | HandAlreadyFullException e) {
            System.err.println("Fatal error while drawing a card");
            throw new RuntimeException();
        }

        passMessage(sender, new SendDrawncardMessage(generateFieldUpdate(), game.getPlayerHand(SenderName.get(sender))));

        passMessage(sender, new EndPlayerTurnMessage());


        if (game.isInFinalPhase()) {
            if (finalRoundCounter == -1) {
                EndGame(sender);
            } else finalRoundCounter--;
        }

        synchronized (connectedClients) {
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    passMessage(c, generateFieldUpdate());
                }
            }

            if (finalRoundCounter != 0) {
                int currentIndex = connectedClients.indexOf(sender);
                int nextIndex = (currentIndex + 1) % connectedClients.size();
                ClientHandler nextClient = connectedClients.get(nextIndex);
                while (softLockedClients.contains(nextClient)) {/*This check on the next client has to be done only here,
                    in the other methods where the next client is picked a soft lock cannot have happened as they refer to the initial phase of the game*/
                    passMessage(nextClient, new GenericMessage("Your turn has been skipped! You do not have any available placing position!"));
                    nextIndex = (nextIndex + 1) % connectedClients.size();
                    nextClient = connectedClients.get(nextIndex);
                }
                passMessage(nextClient, new StartPlayerTurnMessage());
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

        if (currentState.equals(GameState.END_FOR_DISCONNECTION)) {
            game.gameOver();
            ImmutableScoreTrack finalPlayerScore = game.getScoreTrack();
            List<String> winners = new ArrayList<>();
            winners.add(SenderName.get(sender));
            passMessage(sender, new GameEndingMessage(finalPlayerScore, winners));
        }

        if (game.isInFinalPhase()) {
            synchronized (connectedClients) {

                if (finalRoundCounter == -1) {
                    finalRoundCounter = 2 * numPlayers - (connectedClients.indexOf(sender) + 1);
                }

                if (finalRoundCounter == 0) {
                    game.gameOver();
                    ImmutableScoreTrack finalPlayerScore = game.getScoreTrack();
                    List<String> winners = game.getWinners();
                    for (ClientHandler c : connectedClients) {
                        passMessage(c, new GameEndingMessage(finalPlayerScore, winners));
                    }
                    System.exit(1);
                }
            }

        }
    }

    /**
     * If a player disconnects from the game, the game instantly ends and the current score is sent to the client. If a player disconnects during the setup phase of the game,
     * before playing a single round and scoring points, this method ends the game and sends to the client the score with 0 points for each player connected at that moment.
     *
     * @param mes    when a player have to leave the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ClientDisconnectedVoluntarilyMessage mes, ClientHandler sender) {
        disconnectionHappened(sender);
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
     * @return SimpleCard containing all the information about the card just placed
     */
    private SimpleCard placingInfos(int x, int y, boolean face, int id) {
        return new SimpleCard(id, x, y, face);
    }


    @Override
    public void handle(Ping ping, ClientHandler sender) {
        sender.pingWasReceived();
    }

    /**
     * The listener is notified of a disconnection.<br>
     * It then sends the appropriate game termination messages according to the phase where the disconnection has
     * happened<br>
     * In the initial phase of the game, before the first player's turn, the game ends without declaring any winner.<br>
     * If the game has been going on then the winners are announced excluding the disconnected player/s
     *
     * @param clientHandler is the client who was disconnected
     */
    @Override
    public void disconnectionHappened(ClientHandler clientHandler) {
        synchronized (connectedClients) {
            connectedClients.remove(clientHandler);
            if(!SenderName.containsKey(clientHandler)){//Clients that have not chosen a name won't make the game crash for everyone
                clientHandler.stopConnection();
                return;
            }
            SenderName.remove(clientHandler);
            clientHandler.stopConnection();

            if (currentState.equals(GameState.PRE_LOBBY) || currentState.equals(GameState.SIDE_CHOICE) || currentState.equals(GameState.SECRET_CHOICE)) {
                for (ClientHandler c : connectedClients) {
                    passMessage(c, new InitialPhaseDisconnectionMessage());
                }
            } else {
                for (ClientHandler c : connectedClients) {
                    passMessage(c, new GenericMessage("The game is ending because of a disconnection"));
                    game.gameOver();
                    passMessage(c, new GameEndingAfterDisconnectionMessage(game.getScoreTrack(), game.getWinnersAfterDisconnection(SenderName.values())));
                }
            }
        }
        System.exit(1);
    }

    @Override
    public void handle(ChatMessage chatMessage, ClientHandler sender) {
        if (chatMessage.getBody() == null) return;
        boolean isGlobal = chatMessage.isGlobal();
        String recipient = chatMessage.getHead();
        if (!isGlobal && (recipient == null || !SenderName.containsValue(recipient))) {
            passMessage(sender, new ReceivedChatMessage("The server",recipient+" is not a player",false));
            return;
        }
        String name = SenderName.get(sender);
        String message = chatMessage.getBody();
        synchronized (connectedClients) {
            for (ClientHandler c : connectedClients) {
                if (c != sender && isGlobal) {
                    passMessage(c, new ReceivedChatMessage(name, message, true));
                } else if (c != sender) {
                    passMessage(c, new ReceivedChatMessage(name, message, false));
                    return;
                }
            }
        }
    }
}
