package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.network.commonData.ConstantValues;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.network.socket.client.ClientSocket;
import it.polimi.ingsw.view.GameView;

import java.io.IOException;

/**
 * Controller for the client, it handles all messages that can be received and handles the user input.
 * Implements the design pattern singleton.
 */
public class ClientController implements ClientSideMessageListener, UserListener {

    private int lastPlayed;
    @SuppressWarnings("final") static ClientController instance = null;
    private GameView gameView;
    private String clientName = "";
    private ClientSocket serverConnection;
    private ClientControllerState currentState;
    /**
     * Attribute used to synchronize the gameView attribute
     */
    private final Object viewLock= new Object();

    /**
     * Creates a new ClientController object. To start connecting to the server a joinLobbyCommand must be received
     */
    private ClientController() {
        this.gameView = null;
        serverConnection = null;
        currentState=ClientControllerState.INIT;
    }

    /**
     * Implementing the singleton design pattern this method creates the instance of the controller
     * the first time it is called, then it returns that instance<br>
     *To start connecting to the server joinLobbyCommand must be received
     * @return the instance of the controller
     */
    public static ClientController getInstance() {
        return instance == null ? new ClientController() : instance;
    }

    /**
     * After the player has chosen IP and port of the serer the connection is started.
     * Two new threads are created: one to receive messages from the server and queuing them
     * and one to extract them from the queue and executing them.
     */
    private void begin() {
        serverConnection = new ClientSocket(this);
        new Thread(() -> serverConnection.receiveMessages()).start();
        new Thread(() -> serverConnection.passMessages()).start();
        sendMessage(new FindLobbyMessage());
    }

    /**
     * Sends a message to the server and handles any IOExceptions that may arise
     */
    private void sendMessage(ClientToServerMessage mes) {
        try {
            serverConnection.send(mes);
        } catch (IOException e) {
            System.err.println("\nError while sending a message\n");
            throw new RuntimeException();
        }
    }

    /**
     * When a message that is not supported is received an error is printed
     */
    @Override
    public void handle(ServerToClientMessage m) {
        System.err.println("\nUnhandled message received\n");
    }

    /**
     * Placeholder for a future feature
     */
    @Override
    public void handle(ClientFieldCheckValidityMessage m) {

    }

    /**
     * @param cmd is used to connect to the lobby
     */
    @Override
    public void receiveCommand(JoinLobbyCommand cmd) {
        if(currentState.equals(ClientControllerState.INIT)) {
            ConstantValues.setServerIp(cmd.getIp());
            ConstantValues.setSocketPort(cmd.getPort());
            currentState=ClientControllerState.CONNECTING;
            this.begin();
        }else{
            System.out.println("\nYou are already connected to "+ConstantValues.serverIp+":"+ConstantValues.socketPort);
        }
    }
    /**
     * The player can't connect to the lobby as it is full. The connection will be interrupted
     */
    @Override
    public void handle(LobbyFullMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        try {
            serverConnection.stopConnection();
        } catch (IOException e) {
            System.err.println("\nIOException while terminating the connection.\n");
            throw new RuntimeException();
        }
    }

    /**
     * The user tried to connect to a game that was already started, they will be disconnected
     */
    @Override
    public void handle(GameAlreadyStartedMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        GameView.showText("\nThis game has already started, you can't participate!");
        GameView.showText("\nYou will be disconnected from the server.\n");
        try {
            serverConnection.stopConnection();
        } catch (IOException e) {
            System.err.println("\nIOException while terminating the connection.\n");
        }
    }

    /**
     * The player has successfully connected to the lobby, they can now choose their name
     */
    @Override
    public void handle(LobbyFoundMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;//TODO: information about who's present ecc
        GameView.showText("\nConnected successfully to a game.\n" +
                " Now choose your name\n(Type 'set_name' followed by your name)\n");
    }

    /**
     * @param cmd is used to choose a name
     */
    @Override
    public void receiveCommand(NameCommand cmd) {
        if (!currentState .equals( ClientControllerState.CHOOSING_NAME)) {
            GameView.showText("\nYou can't choose the name now!\n");
        } else {
            clientName= cmd.getName();
            sendMessage(new ChooseNameMessage(clientName));
            currentState = ClientControllerState.WAITING_FOR_NAME_CONFIRMATION;
        }
    }

    /**
     * A new name must be chosen by the user.
     */
    @Override
    public void handle(NameNotAvailableMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;
        GameView.showText("\n"+clientName+" is already taken, choose another name.\n");
        clientName="";
    }

    /**
     * The player has chosen a valid name. They will now wait for either a message that signals
     * the start of the game or for a ChooseHowManyPlayersMessage
     */
    @Override
    public void handle(NameChosenSuccessfullyMessage m) {
        currentState = ClientControllerState.WAITING_FOR_START;
        GameView.showText("\nSuccessfully registered as "+this.clientName+"\n");
    }

    /**
     * The controller now waits for a UserCommand that tells how many players will pay the game;
     * most of the other commands are rejected
     */
    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        currentState = ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS;
        GameView.showText("\nChoose the number of players that will play in this game" +
                "\n(Type 'num_players' and the number of players that will play)\n");
    }

    /**
     * The user sent a command with the number of players they want to have in that game.
     * If they do not have the right to do that the command is refused
     */
    @Override
    public void receiveCommand(NumberOfPlayerCommand cmd) {
        if (!currentState .equals( ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS)) {
            GameView.showText("\nAt the moment you can't choose the number of players\n");
        }
        else if(cmd.getNumberOfPlayer()<2){
            GameView.showText("\nThe minimum number of players is 2\n");
        }
        else if(cmd.getNumberOfPlayer()>4){
            GameView.showText("\nThe maximum number of players is 4\n");
        }
        else {
            sendMessage(new NumberOfPlayersMessage(cmd.getNumberOfPlayer()));
            currentState = ClientControllerState.WAITING_FOR_START;
        }
    }

    /**
     * A command to choose how many players would be playing was sent to the server that has answered with a
     * ClientCantStartGameMessage as the client didn't receive a ChooseHowManyPlayersMessage.
     */
    @Override
    public void handle(ClientCantStartGameMessage m) {
        GameView.showText("\nYou don't have permission to start the game\n");
    }

    /**
     * The game has started, the view will be updated with the initial information.
     * The player now has to choose how to place their starting card
     */
    @Override
    public void handle(GameStartingMessage m) {
        currentState = ClientControllerState.CHOOSING_STARTING_CARD_FACE;
        synchronized (viewLock) {
            try {
                gameView = new GameView( m.getPlayersInfo(), clientName, m.getStartingCard(), m.getFirstCommonObjective(), m.getSecondCommonObjective());
                SharedFieldUpdateMessage tmp = m.getSharedFieldData();
                gameView.updateDecks(tmp.getGoldBackside(), tmp.getResourceBackside(), tmp.getVisibleCards());
                gameView.updatePlayerHand(m.getPlayerHand());
            } catch (IOException e) {
                System.err.println("\nError initializing the view\n");
                throw new RuntimeException();
            }
            gameView.printCommonField();
            gameView.printHand();
        }
        gameView.printStartingCard();
        GameView.showText("\nPlace your starting card." +
                "           ( Type 'place_starting_card' and 'up' or 'down')\n");
    }

    /**
     * @param cmd is used by the player to choose the side of the initial card
     */
    @Override
    public void receiveCommand(StartingCardSideCommand cmd) {
        if (currentState.equals(ClientControllerState.CHOOSING_STARTING_CARD_FACE)) {
            sendMessage(new ChooseStartingCardSideMessage(cmd.getSide()));
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
        } else {
            GameView.showText("\nYou can't place your starting card now!\n");
        }
    }

    /**
     * Another player has been disconnected, now the winner of the incomplete game will be displayed
     */
    @Override
    public void handle(GameEndingAfterDisconnectionMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        GameView.showText("\nThe game has ended because of a disconnection here is the final leaderboard:");
        gameView.displayWinners(m.getFinalPlayerScore());
        try {
            serverConnection.stopConnection();
        } catch (IOException e) {
            System.err.println("\nIOException while terminating the connection. After the end of the Game\n");
        }
    }

    /**
     * The game has ended, the view will be updated with the final information
     */
    @Override
    public void handle(GameEndingMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        GameView.showText("\nThe game has ended here is the final leaderboard:");
        gameView.displayWinners(m.getFinalPlayerScore());
        try {
            serverConnection.stopConnection();
        } catch (IOException e) {
            System.err.println("\nIOException while terminating the connection.\n");
        }
    }

    /**
     * Message sent when there are problems with the start of the game. The client will continue waiting
     */
    @Override
    public void handle(ServerCantStartGameMessage m) {
        GameView.showText("\nThe game can't be started right now.\n");
    }

    /**
     * The view is updated by showing the player their objective choice. Any command
     * other than their choice will be rejected.
     */
    @Override
    public void handle(SecretObjectiveChoiceMessage m) {
        currentState = ClientControllerState.CHOOSING_OBJECTIVE;
        synchronized (viewLock) {
            gameView.secretObjectiveChoice(m.getFirstChoice(), m.getSecondChoice());
        }
    }

    /**
     * @param cmd is used by the player to choose the secret objective
     */
    @Override
    public void receiveCommand(SecretObjectiveCommand cmd) {
        if(!currentState.equals(ClientControllerState.CHOOSING_OBJECTIVE)){
            GameView.showText("\nNow is not the moment for the choice of an objective\n");
            return;
        }
        if (gameView.setSecretObjective(cmd.getObjective())) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
            sendMessage(new ChosenSecretObjectiveMessage(cmd.getObjective()));
        } else {
            GameView.showText("\n"+cmd.getObjective() + " is not between your objective choices!");
        }
    }

    /**
     * The player's turn has begun. They will now have to send
     * a command containing information on the placement they want to make
     */
    @Override
    public void handle(StartPlayerTurnMessage m) {
        if (gameNotSoftLocked()) {
            currentState = ClientControllerState.REQUESTING_PLACEMENT;
            GameView.showText("It's your turn, now place a card!" +
                    "\n(Type 'place' 'id' 'up' or 'down' 'x' 'y')\n");

        } else {
            GameView.showText("\nYour field has no more available corners! Your turn will be skipped");
        }
        gameView.printOwnerField();
    }

    /**
     * When there are no more available positions to play a card in, this message is received.
     * The player turn will now be skipped
     */
    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {
        currentState = ClientControllerState.GAME_SOFT_LOCKED;
        GameView.showText("\nYour field has no more available corners! Your turn will be skipped");
        gameView.printOwnerField();
    }

    /**
     * A message regarding another player's move has been received. The view will be updated and shown.
     */
    @Override
    public void handle(OtherPlayerTurnUpdateMessage m) {
        if (gameNotSoftLocked()) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
        }
        String opponent = m.getPlayerName();
        PlayerPlacedCardInformation info = m.getPlacedCardInformation();
        synchronized (viewLock) {
            gameView.updateOtherPlayerField(opponent, info.getCardId(), info.getXPos(), info.getYPos(), info.isFacingUp(), m.getVisibleSymbols());
            GameView.showText(opponent + " has made a move!\n");
            gameView.printOpponentField(opponent);
        }
    }

    /**
     * @param cmd contains the information about the player's move
     */
    @Override
    public void receiveCommand(PlaceCardCommand cmd) {
        if(!gameNotSoftLocked()){
            GameView.showText("\nYou can't place a card. There are no more points where you could do that.\n");
        }
        else if (!currentState .equals( ClientControllerState.REQUESTING_PLACEMENT)) {
            GameView.showText("\nYou can't place a card now\n");
        }
        else {
            currentState = ClientControllerState.WAITING_FOR_PLACEMENT_CONFIRMATION;
            lastPlayed=cmd.getCardID();
            synchronized (viewLock) {
                if (gameView.getPlayerHand().contains(lastPlayed)) {
                    sendMessage(new PlaceCardMessage(cmd.getXPosition(), cmd.getYPosition(), cmd.isFacingUP(), cmd.getCardID()));
                } else {
                    GameView.showText("\nYou do not have a card with id:" + lastPlayed+" in your hand");
                }
            }
        }
    }

    /**
     * The player has made an illegal move, they are asked to make another move
     */
    @Override
    public void handle(IllegalPlacementPositionMessage m) {
        currentState = ClientControllerState.REQUESTING_PLACEMENT;
        GameView.showText("\nThis position is not available for placement!\n");
    }

    /**
     * A gold card was placed face up when there weren't enough visible symbols. So the player must make another move.
     */
    @Override
    public void handle(NotEnoughResourcesMessage m) {
        currentState = ClientControllerState.REQUESTING_PLACEMENT;
        GameView.showText("\nYou don't have enough resources to play this gold card!\n");
        gameView.printOwnerField();
    }

    /**
     * The player has placed a card in an available position. They will now be requested to draw a card.
     */
    @Override
    public void handle(SuccessfulPlacementMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        synchronized (viewLock) {
            gameView.updatePlayerHand(lastPlayed);

            PlayerPlacedCardInformation info = m.getPlacedCardInformation();
            gameView.updateOwnerField(info.getCardId(),info.getXPos(),info.getYPos(),info.isFacingUp(),m.getVisibleSymbols());

            SharedFieldUpdateMessage tmp = m.getSharedField();
            gameView.updateDecks(tmp.getGoldBackside(), tmp.getResourceBackside(), tmp.getVisibleCards());
            gameView.updateScoreTrack(tmp.getScoreTrack());

            gameView.printOwnerField();
            GameView.showText("\nNow draw a card!" +
                    "Type 'draw' followed by\n" +
                    "'GoldDeck' for the top card f the gold deck\n" +
                    "'GoldFirstVisible' for the first visible gold card\n" +
                    "'GoldSecondVisible' for the second visible gold card\n" +
                    "'ResourceDeck' for the top card f the gold deck\n" +
                    "'ResourceFirstVisible' for the first visible resource card\n" +
                    "'ResourceSecondVisible' for the second visible resource card\n");
            gameView.printCommonField();
            gameView.printHand();
        }
    }

    /**
     * @param cmd is used to choose a card to draw
     */
    @Override
    public void receiveCommand(DrawCardCommand cmd) {
        if (!currentState .equals( ClientControllerState.REQUESTING_DRAW_CARD)) {
            GameView.showText("\nIt's not time to draw!");
        } else {
            sendMessage(new DrawCardMessage(cmd.getChoice()));
            currentState = ClientControllerState.WAITING_FOR_DRAW_CONFIRMATION;
        }
    }

    /**
     * The player tried to draw from an empty deck, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDeckMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        GameView.showText("\nYou tried to draw from an empty deck. Change your choice.");
    }
    /**
     * The player tried to draw from an empty visible card position, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        GameView.showText("This card position is empty. Change your drawing choice"+
                "Type 'draw' followed by\n\n" +
                "'GoldDeck' for the top card f the gold deck\n" +
                "'GoldFirstVisible' for the first visible gold card\n" +
                "'GoldSecondVisible' for the second visible gold card\n" +
                "'ResourceDeck' for the top card f the gold deck\n" +
                "'ResourceFirstVisible' for the first visible resource card\n" +
                "'ResourceSecondVisible' for the second visible resource card\n");
    }

    /**
     *The controller updates the view with the information about the drawn card
     */
    @Override
    public void handle(SendDrawncardMessage m) {
        SharedFieldUpdateMessage temp = m.getSharedField();
        synchronized (viewLock) {
            gameView.updateDecks(temp.getGoldBackside(), temp.getResourceBackside(), temp.getVisibleCards());
            gameView.updateScoreTrack(temp.getScoreTrack());
            gameView.updatePlayerHand(m.getPlayerHand());
            gameView.printCommonField();
            gameView.printHand();
        }
    }

    /**
     * Signals the end of a player's turn and updates the view according to the received information
     */
    @Override
    public void handle(EndPlayerTurnMessage m) {
        currentState = ClientControllerState.OTHER_PLAYER_TURN;
        GameView.showText("\nYour turn has ended");
    }

    /**
     * Message received at the end of an opponent's turn, it contains information about
     * how the common field has changed after their drawing phase.
     */
    @Override
    public void handle(SharedFieldUpdateMessage m) {
        synchronized (viewLock) {
            gameView.updateDecks(m.getGoldBackside(), m.getResourceBackside(), m.getVisibleCards());
            gameView.updateScoreTrack(m.getScoreTrack());
            gameView.printCommonField();
        }
    }

    /**
     * @param cmd requests the available position for a card
     */
    @Override
    public void receiveCommand(AvailablePositionCommand cmd) {
        if(hasGameBegun())
            sendMessage(new RequestAvailablePositionsMessage());
        else{
            GameView.showText("\nThe game has not yet begun!\n");}
    }

    /**
     * When an AvailablePositionsMessage is received the view is updated to show where the available positions are
     */
    @Override
    public void handle(AvailablePositionsMessage m){
        currentState = m.getPositions().isEmpty() ? ClientControllerState.GAME_SOFT_LOCKED : currentState;
        synchronized (viewLock) {
            if (gameNotSoftLocked()) {
                gameView.updateAvailablePositions(m.getPositions());
                gameView.printOwnerField();
            } else {
                gameView.printOwnerField();
                GameView.showText("\nThere are no more available positions! Your turn will be skipped form now on!");
            }
        }
    }

    /**
     * @return false if the game is soft-locked, true if the player can continue playing
     */
    private boolean gameNotSoftLocked() {
        return !ClientControllerState.GAME_SOFT_LOCKED .equals( currentState);
    }

    /**
     * @return true if the controller is in a state where the player can correctly request information about the game state
     */
    private boolean hasGameBegun() {
        return !(currentState .equals( ClientControllerState.CONNECTING) || currentState .equals( ClientControllerState.ENDING_CONNECTION) ||
                currentState .equals( ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS) || currentState .equals( ClientControllerState.CHOOSING_NAME) ||
                currentState .equals( ClientControllerState.CHOOSING_OBJECTIVE) || currentState .equals( ClientControllerState.CHOOSING_STARTING_CARD_FACE) ||
                currentState .equals( ClientControllerState.WAITING_FOR_START)||currentState.equals(ClientControllerState.INIT)||
                currentState.equals(ClientControllerState.WAITING_FOR_NAME_CONFIRMATION));
    }

    /**
     * @param cmd is used to show the field of the user
     */
    @Override
    public void receiveCommand(ShowFieldCommand cmd) {
        if (hasGameBegun()) gameView.printOwnerField();
        else { GameView.showText("\nGame has not yet begun");}
    }

    /**
     * @param cmd is used to show the field of a player
     */
    @Override
    public void receiveCommand(ShowOtherFieldCommand cmd) {
        if (hasGameBegun()) gameView.printOpponentField(cmd.getOpponentName());
        else { GameView.showText("\nGame has not yet begun");}
    }

    /**
     * The view will show the leaderboard at that moment
     */
    @Override
    public void receiveCommand(ShowLeaderboardCommand cmd) {
        if(hasGameBegun()) gameView.printScoreTrack();
        else { GameView.showText("\nGame has not yet begun");}
    }

    /**
     * The view will sho the player their hand
     */
    @Override
    public void receiveCommand(ShowHandCommand cmd) {
        if(hasGameBegun()) gameView.printHand();
        else { GameView.showText("\nGame has not yet begun");}
    }

    /**
     * @param cmd is used to display the common field
     */
    @Override
    public void receiveCommand(ShowCommonFieldCommand cmd) {
        if(hasGameBegun()) gameView.printCommonField();
        else { GameView.showText("\nThe game has not yet begun");}
    }

    /**
     * @param cmd is used to display the player's objectives
     */
    @Override
    public void receiveCommand(ShowObjectivesCommand cmd) {
        if(hasGameBegun()) {
            gameView.showSecretObjectives();
            gameView.showCommonObjectives();
        }
        else { GameView.showText("\nThe game has not yet begun");}
    }

    /**
     * @param cmd is used when the player choose to leave the lobby
     */
    @Override
    public void receiveCommand(EndGameCommand cmd) {
        sendMessage(new ClientDisconnectedVoluntarilyMessage());
        try {
            serverConnection.stopConnection();
        } catch (IOException e) {
            System.err.println("Error closing the connection");
            throw new RuntimeException();
        }
    }

    /**
     * @param m is a message that contains information of interest to the player.
     */
    @Override
    public void handle(GenericMessage m) {
        GameView.showText(m.getMessage());
    }

    @Override
    public ClientControllerState getListenerState() {
        return currentState;
    }
}
