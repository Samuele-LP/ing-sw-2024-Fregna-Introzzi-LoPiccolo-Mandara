package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
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
    public enum GameState{
        PRELOBBY,
        NAMECHOICE,
        SIDECHOICE,
        SECRETCHOICE,
        PLACING,
        DRAWING,
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
    private HashMap<ClientHandler, String> SenderName = new HashMap <>();
    private final ArrayList <ClientHandler> connectedClients = new ArrayList<>();
    private ClientHandler firstPlayer;
    private int objectivesChosen;
    private HashMap<ClientHandler, ObjectiveCard[]> objectiveChoices = new HashMap<>();
    public GameState currentState;



    /**
     * Constructor
     */
    private GameController(){
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
//Added numPlayers!=-1 because every connection would be refused: 1 >-1 always ( the number of players is not yet set)
            if (!currentState.equals(GameState.PRELOBBY)) {
                try {
                    sender.sendMessage(new LobbyFullMessage());
                    connectedClients.remove(sender);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else try {
                sender.sendMessage(new LobbyFoundMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        if(currentState.equals(GameState.PRELOBBY)) {
            String chosenName = mes.getName();
            SenderName.put(sender, chosenName);

            if(playersName.contains(chosenName)){
                try{
                    sender.sendMessage(new NameNotAvailableMessage());
                    return;
                }catch (IOException e){
                    throw new RuntimeException();
                }
            }
            synchronized (connectedClients) {
                playersName.add(chosenName);
                //add(connectedClients.indexOf(sender),chosenName);
            }

                try {
                    sender.sendMessage(new NameChosenSuccessfullyMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    sender.sendMessage(new ChooseHowManyPlayersMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
        if((numPlayers != -1||sender!=firstPlayer)||!currentState.equals(GameState.PRELOBBY)) {//Now the message is refused also if the sender isn't firstPlayer.
            try {
                sender.sendMessage(new ClientCantStartGameMessage());
            }catch (IOException e){
                throw new RuntimeException();
            }
        }else{
            this.numPlayers= mes.getNumber();
            synchronized (connectedClients){
                if(connectedClients.size()>numPlayers){//If there are for example 10 clients connected and max 3 players the other 7 must be disconnected
                    List<ClientHandler> toRemove = new ArrayList<>();//List used to avoid ConcurrentModificationException thrown by the list we iterate on
                    for(int i=numPlayers;i<connectedClients.size();i++){
                        try {
                            connectedClients.get(i).sendMessage(new LobbyFullMessage());
                        }catch  (IOException e){
                            throw new RuntimeException();
                        }
                        toRemove.add(connectedClients.get(i));
                    }
                    connectedClients.removeAll(toRemove);
                }
            }
            this.game=new Game(numPlayers);
            try {
                sender.sendMessage(new GenericMessage("The game will start when "+numPlayers+" players are connected"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (playersName.size()>=numPlayers){
                startGame(sender);
            }
        }
    }

    /**
     * Method called to initialize the starting phase of the game when the all the players are connected with a valid name
     * @param sender is the reference to who has sent the relative mes
     */

    private void startGame(ClientHandler sender) {

        if(playersName.size() == numPlayers && sender == firstPlayer) {
            randomizePlayersOrder();
            try {
                game.startGame(playersName);
                firstPlayer.sendMessage(new GenericMessage("eirufgefgerugferyerthrth"));
                firstPlayer.sendMessage(new GameStartingMessage(playersName, game.getStartingCardId(SenderName.get(firstPlayer)), game.getPlayerHand(SenderName.get(firstPlayer)), generateFieldUpdate(),game.getFirstCommon(), game.getSecondCommon()));
                currentState = GameState.SIDECHOICE;
                isGameStarted = true;
            } catch (IOException e) {
                System.err.println("IOException while starting the game\n");
                e.printStackTrace();
            }catch (Exception e){
                throw new RuntimeException();
            }
        }else if(playersName.size() != numPlayers){
            try {
                sender.sendMessage(new ServerCantStartGameMessage());
                System.out.println(numPlayers);
                System.out.println(playersName.get(0));
                System.out.println(playersName.get(1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(!Objects.equals(sender, firstPlayer))
            try {
                sender.sendMessage(new ClientCantStartGameMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Randomize the order of the playersName and of the ClientHandler associated with each of them before starting the game
     */
    private void randomizePlayersOrder(){
        Collections.shuffle(playersName);

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

        firstPlayer=connectedClients.getFirst();
    }


    /**
     * This method receives the placing side of the starting card chose by the player and set it to his playing field
     * @param mes  contains the choice regarding the side of the starting card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes, ClientHandler sender) {
        if(currentState == GameState.SIDECHOICE){
        String currentPlayerName = SenderName.get(sender);
        boolean startingPosition = mes.facingUp();

        try {
            game.setStartingCard(currentPlayerName,startingPosition);
        } catch (NotPlacedException | AlreadyPlacedException e) {
            throw new RuntimeException(e);
        }

        if(connectedClients.indexOf(sender)+1==numPlayers){//indexes must be increased by 1 because otherwise thy will rang from 0 tu numPlayers -1
            currentState = GameState.SECRETCHOICE;
            objectivesChosen = numPlayers;
            for(ClientHandler c: connectedClients){
                try {
                    objectiveChoices.put(c,game.dealSecretObjective(SenderName.get(c))) ;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                try {
                    ObjectiveCard[] tmp = objectiveChoices.get(c);
                    c.sendMessage(new SecretObjectiveChoiceMessage(tmp[0].getID(), tmp[1].getID()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            int currIndex = connectedClients.indexOf(sender);
            int nextIndex = (currIndex+1);
            if(currIndex<connectedClients.size()-1) {
                try {
                    ClientHandler nextSender = connectedClients.get(nextIndex);
                    nextSender.sendMessage(new GameStartingMessage(playersName, game.getStartingCardId(SenderName.get(nextSender)),game.getPlayerHand(SenderName.get(nextSender)),generateFieldUpdate(), game.getFirstCommon(), game.getSecondCommon()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        }
    }
    /**
     * This method handles the choice of the secretObjective by comparing the cardID in the message and the two cards presented to the player and then calls
     * the method in the game to set the choice
     * @param mes    is the message with the secretObjective card the player chose between the twos dealt
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender) {
        if(currentState == GameState.SECRETCHOICE) {
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
                try {
                    firstPlayer.sendMessage(new StartPlayerTurnMessage());
                    currentState = GameState.PLACING;
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
        if(!currentState.equals(GameState.PLACING)&&!currentState.equals(GameState.DRAWING)){
            //todo generic
            return;
        }
        String currentPlayerName = SenderName.get(sender);
        List<Point> availablePositions = null;

        try {
            availablePositions = game.getAvailablePoints(currentPlayerName);
        } catch (NotPlacedException  e) {
            throw new RuntimeException(e);
        }catch (PlayerCantPlaceAnymoreException e){//The appropriate response is now sent
            try {
                sender.sendMessage(new PlayerCantPlayAnymoreMessage());
            }catch (IOException e1){
                throw new RuntimeException();
            }
        }

        try {
            sender.sendMessage(new AvailablePositionsMessage(availablePositions));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method receives the PlaceCardMessage and calls the PlaceCard game method to set the card
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(PlaceCardMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);

        if (currentState == GameState.PLACING) {
            try {
                game.playCard(currentPlayerName, mes);
            } catch (NotPlacedException | AlreadyPlacedException | CardNotInHandException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughResourcesException e) {
                try {
                    sender.sendMessage(new NotEnoughResourcesMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            } catch (InvalidPositionException I) {//Now the appropriate message is sent
                try {
                    sender.sendMessage(new IllegalPlacementPositionMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }


            try {
                sender.sendMessage(new SuccessfulPlacementMessage(game.getPlayerVisibleSymbols(currentPlayerName), placingInfos(mes.getX(), mes.getY(), mes.isFacingUp(), mes.getID()), generateFieldUpdate()));
                currentState = GameState.DRAWING;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                game.getAvailablePoints(currentPlayerName);
                } catch (NotPlacedException e) {
                    throw new RuntimeException(e);
                } catch (PlayerCantPlaceAnymoreException e) {
                try {
                    sender.sendMessage(new PlayerCantPlayAnymoreMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    try {
                        c.sendMessage(new OtherPlayerTurnUpdateMessage(game.getPlayerVisibleSymbols(SenderName.get(sender)), placingInfos(mes.getX(), mes.getY(), mes.isFacingUp(), mes.getID()), generateFieldUpdate(), SenderName.get(sender)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            if (game.isInFinalPhase())
                EndGame(sender);
        }
    }

    /**
     * This method receives the DrawCardMessage and calls the drawCard game method to draw the card to end his turn after placing
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(DrawCardMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);

        if(currentState == GameState.DRAWING) {

            try {
                game.drawCard(currentPlayerName, mes);
            } catch (EmptyDeckException e) {
                try {
                    sender.sendMessage(new EmptyDeckMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;//added returns because without  them the player would end their turn without drawing
            } catch (NoVisibleCardException e) {
                try {
                    sender.sendMessage(new EmptyDrawnCardPositionMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;//added returns because without  them the player would end their turn without drawing
            } catch (Exception e) { /////??????.
                throw new RuntimeException(e);
            }

            try {
                sender.sendMessage(new SendDrawncardMessage(generateFieldUpdate(), game.getPlayerHand(SenderName.get(sender))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                sender.sendMessage(new EndPlayerTurnMessage());
                if (game.isInFinalPhase())
                    finalRoundCounter--;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    try {
                        c.sendMessage(generateFieldUpdate());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if(finalRoundCounter != 0) {
                try {
                    int currentIndex = connectedClients.indexOf(sender);
                    int nextIndex = (currentIndex + 1) % connectedClients.size();
                    connectedClients.get(nextIndex).sendMessage(new StartPlayerTurnMessage());
                    currentState = GameState.PLACING;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
    private void EndGame (ClientHandler sender){
        if(game.isInFinalPhase()) {
            if (finalRoundCounter == -1) {
                finalRoundCounter = 2 * numPlayers - (connectedClients.indexOf(sender) + 1);//indexOfSender would be between 0 and numPlayers-1 without the (.. +1 and an extra round would be played)
            }
            if (finalRoundCounter == 0) {
                game.gameOver();
                HashMap<String, Integer> finalPlayerScore = game.getFinalScore();
                for (ClientHandler c : connectedClients) {
                    try {
                        c.sendMessage(new GameEndingMessage(finalPlayerScore));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

        if(currentState != GameState.PRELOBBY) {
            for(ClientHandler c: connectedClients){
                if(c != sender){
                    try {
                        c.sendMessage(genericMessage("The player"+disconnectedPlayerName+"left the game"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            game.gameOver();
            HashMap<String, Integer> finalPlayerScore = game.getFinalScore();
            for (ClientHandler c : connectedClients) {
                if (c != sender) {
                    try {
                        c.sendMessage(new GameEndingMessage(finalPlayerScore));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else {
            HashMap <String, Integer> score = new HashMap<>();
            for(ClientHandler c: connectedClients){
                score.put(SenderName.get(c),0);
            }
            for(ClientHandler c: connectedClients){
                if(c != sender){
                    try {
                        c.sendMessage(genericMessage("The player "+disconnectedPlayerName+" left the game before anyone played a single round"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        c.sendMessage(new GameEndingMessage(score));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
    private GenericMessage genericMessage(String message){
            return new GenericMessage(message);
    }

    /**
     * @return ShareFieldUpdateMessage containing the update of the common ground after a certain player has played his turn
     */
    private SharedFieldUpdateMessage generateFieldUpdate() {
        return new SharedFieldUpdateMessage(game.getScoreTrack(),game.getResourceTop(),game.getGoldTop(),game.getVisibleCards());
    }

    /**
     *
     * @param x coordinate of the card
     * @param y coordinate of the card
     * @param face of the card (on the face or on the back)
     * @param id of the card
     * @return PlayerPlacedCardInformation containing all the information about the card just placed
     */
    private PlayerPlacedCardInformation placingInfos(int x, int y , boolean face, int id){
        return new PlayerPlacedCardInformation(id,x,y,face);
    }

    /**
     * @return currentState of the GameController
     */
    public GameState getCurrentState() {
        return currentState;
    }
}
