package it.polimi.ingsw.controller;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.Gui.controllers.ChatController;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Pong;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.view.GameView;
import it.polimi.ingsw.view.GameViewCli;
import it.polimi.ingsw.view.GameViewGui;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClientController class manages the client's interactions with the server and user inputs.
 * It handles all messages that can be received and user commands.
 * This class implements the Singleton design pattern.
 */
public class ClientController implements ClientSideMessageListener, UserListener {

    /**
     * ID of the card the player last tried to play.
     */
    private int lastPlayed;

    /**
     * Singleton instance of the ClientController.
     */
    private static volatile ClientController instance = null;

    /**
     * The view associated with the client.
     */
    private final GameView gameView;

    /**
     * The name of the client.
     */
    private String clientName = "";

    /**
     * Connection to the server.
     */
    private ClientConnection serverConnection;

    /**
     * Current state of the ClientController.
     */
    private ClientControllerState currentState;

    /**
     * Indicates if the game starting message has been received.
     */
    private boolean receivedGameStarting = false;

    /**
     * Indicates if the connection to the server has failed.
     */
    private boolean connectionFailed = false;

    /**
     * Attribute used to synchronize the gameView attribute
     */
    private final Object viewLock = new Object();

    /**
     * List containing chat logs sent and received by the player after they joined the server.
     */
    private final List<String> chatLogs = new ArrayList<>();

    /**
     * Creates a new ClientController object. To start connecting to the server a joinLobbyCommand must be received.
     */
    public ClientController() {
        if (ConstantValues.usingCLI) {
            gameView = new GameViewCli();
        } else {
            gameView = new GameViewGui();
        }
        serverConnection = null;
        currentState = ClientControllerState.INIT;
    }

    /**
     * Implementing the singleton design pattern, this method creates the instance of the controller
     * the first time it is called, then it returns that instance.
     * To start connecting to the server a joinLobbyCommand must be received.
     *
     * @return the instance of the controller.
     */
    public static ClientController getInstance() {
        if (instance == null) {
            synchronized (ClientController.class) {//Checks that after acquiring the lock no other class has already created an instance
                if (instance == null) {
                    instance = new ClientController();
                }
            }
        }
        return instance;
    }

    /**
     * After the player has chosen IP and port of the server, by creating a JoinLobbyCommand, the connection is started.
     * Four new threads are created: one to receive messages from the server and queue them,
     * one to extract them from the queue and execute them, and two additional threads for a Ping-Pong system.
     */
    private void begin() {
        serverConnection = new ClientSocket(this);
        if (connectionFailed) {
            gameView.display("Could not connect to the server!\nTry changing the ip or your network settings!");
            serverConnection = null;
            currentState = ClientControllerState.INIT;
            connectionFailed = false;
            return;
        }
        new Thread(() -> serverConnection.receiveMessages()).start();//starts the reception of messages from the server
        new Thread(() -> serverConnection.passMessages()).start();//starts passing messages to the ClientController
        new Thread(() -> serverConnection.sendPing()).start();//Starts the sending of pings to the server
        new Thread(() -> serverConnection.checkConnectionStatus()).start();//starts checking if a disconnection has happened
        sendMessage(new FindLobbyMessage());
    }

    @Override
    public void couldNotConnect() {
        connectionFailed = true;
    }

    /**
     * Sends a message to the server and handles any IOExceptions that may arise.
     *
     * @param mes the message to be sent to the server.
     */
    private void sendMessage(ClientToServerMessage mes) {
        try {
            serverConnection.send(mes);
        } catch (IOException e) {
            System.err.println("\nYou were disconnected!!\n");
            serverConnection.stopConnection();
        }
    }

    /**
     * Handles unsupported messages by printing an error.
     *
     * @param m the unsupported message.
     */
    @Override
    public void handle(ServerToClientMessage m) {
        System.err.println("\nUnhandled message received\n");
    }

    /**
     * Receives the JoinLobbyCommand to connect to the lobby.<br>
     * If the command is received at the right time then the connection will be started
     *
     * @param cmd the command used to connect to the lobby.
     */
    @Override
    public void receiveCommand(JoinLobbyCommand cmd) {
        if (currentState.equals(ClientControllerState.INIT)) {
            ConstantValues.setServerIp(cmd.getIp());
            currentState = ClientControllerState.CONNECTING;
            this.begin();
        } else if (!currentState.equals(ClientControllerState.DISCONNECTED)) {//This two ifs can only be entered by the CLI
            gameView.display("\nYou are already connected to " + ConstantValues.getServerIp() + ":" + ConstantValues.socketPort + "\n");
        } else {
            gameView.display("\nYou were disconnected as " + clientName + " from " + ConstantValues.getServerIp() + ":" + ConstantValues.socketPort + "\n" +
                    "That game has now ended\n");
        }
    }

    /**
     * Handles the LobbyFullMessage indicating that the lobby is full and the connection will be interrupted<br>
     * The program will be terminated if it's in CLI mode, if it's using the GUI then an appropriate scene wil be loaded
     *
     * @param m the LobbyFullMessage received from the server.
     */
    @Override
    public void handle(LobbyFullMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        gameView.connectionRefused();
        serverConnection.stopConnection();
        if (ConstantValues.usingCLI) {
            System.exit(1);
        }
    }

    /**
     * Handles the GameAlreadyStartedMessage indicating that the user tried to connect to a game that has already started.<br>
     * The program will be terminated if it's in CLI mode, if it's using the GUI then an appropriate scene wil be loaded
     *
     * @param m the GameAlreadyStartedMessage received from the server.
     */
    @Override
    public void handle(GameAlreadyStartedMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        gameView.connectionRefused();
        serverConnection.stopConnection();
        if (ConstantValues.usingCLI) {
            System.exit(1);
        }
    }

    /**
     * Handles the LobbyFoundMessage indicating that the player has successfully connected to the lobby and can now choose their name.
     *
     * @param m the LobbyFoundMessage received from the server.
     */
    @Override
    public void handle(LobbyFoundMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;
        gameView.nameChoice();
    }

    /**
     * Receives the NameCommand to choose a name.<br>
     * If received in an inappropriate state the command will do nothing
     *
     * @param cmd the command used to choose a name.
     */
    @Override
    public void receiveCommand(NameCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_NAME)) {
            //Should only be reached in the CLI
            gameView.display("\nYou can't choose the name now!\n");
        } else {
            clientName = cmd.getName();
            currentState = ClientControllerState.WAITING_FOR_NAME_CONFIRMATION;
            sendMessage(new ChooseNameMessage(clientName));
        }
    }

    /**
     * Handles the NameNotAvailableMessage indicating that the chosen name is not available.<br>
     * The player will have to choose a new name.
     *
     * @param m the NameNotAvailableMessage received from the server.
     */
    @Override
    public void handle(NameNotAvailableMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;
        gameView.nameNotAvailable(clientName);
        clientName = "";
    }

    /**
     * Handles the NameChosenSuccessfullyMessage indicating that the player has chosen a valid name.<br>
     * If the player qualifies for it then they will be senta a message that notifies them that they have to choose the number of players,
     * otherwise they will have to wait for the start of the game
     *
     * @param m the NameChosenSuccessfullyMessage received from the server.
     */
    @Override
    public void handle(NameChosenSuccessfullyMessage m) {
        currentState = ClientControllerState.WAITING_FOR_START;
        gameView.waitingForStart();
    }

    /**
     * Handles the ChooseHowManyPlayersMessage allowing the player to choose the number of players for the game.<br>
     * The ClientController can now accept the NumberOfPlayersCommand
     *
     * @param m the ChooseHowManyPlayersMessage received from the server.
     */
    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        currentState = ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS;
        gameView.chooseNumPlayers();
    }

    /**
     * Receives the NumberOfPlayersCommand to set the number of players for the game.<br>
     * The game will start when the chosen number, between 2 and 4, is reached
     *
     * @param cmd the command used to set the number of players.
     */
    @Override
    public void receiveCommand(NumberOfPlayersCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS)) {
            gameView.display("\nAt the moment you can't choose the number of players\n");
        } else if (cmd.getNumPlayers() < 2) {
            gameView.display("\nThe minimum number of players is 2\n");
        } else if (cmd.getNumPlayers() > 4) {
            gameView.display("\nThe maximum number of players is 4\n");
        } else {
            currentState = ClientControllerState.WAITING_FOR_START;
            sendMessage(new NumberOfPlayersMessage(cmd.getNumPlayers()));
        }
    }

    /**
     * Handles the ClientCantStartGameMessage indicating that the client doesn't have permission to start the game.
     *
     * @param m the ClientCantStartGameMessage received from the server.
     */
    @Override
    public void handle(ClientCantStartGameMessage m) {
        gameView.display("\nYou don't have permission to start the game\n");
    }

    /**
     * Handles the GameStartingMessage indicating that the game has started and updates the view with the initial information.<br>
     * When this message is received the ClientController will expect the choice of the starting card side by th player
     *
     * @param m the GameStartingMessage received from the server.
     */
    @Override
    public void handle(GameStartingMessage m) {
        currentState = ClientControllerState.CHOOSING_STARTING_CARD_FACE;
        receivedGameStarting = true;
        synchronized (viewLock) {
            try {
                gameView.gameStarting(m.getPlayersInfo(), clientName, m.getStartingCard(), m.getFirstCommonObjective(), m.getSecondCommonObjective(), m.firstPlayerName());
                SharedFieldUpdateMessage tmp = m.getSharedFieldData();
                gameView.updateDecks(tmp.getGoldBackside(), tmp.getResourceBackside(), tmp.getVisibleCards());
                gameView.updateScoreTrack(tmp.getScoreTrack());
                gameView.updatePlayerHand(m.getPlayerHand());
            } catch (IOException e) {
                System.err.println("\nError initializing the view\n");
                System.exit(-1);
            }
            gameView.printStartingInfo();
        }
    }

    /**
     * Receives the StartingCardSideCommand used by the player to choose the side of the initial card.<br>
     * The next expected message by the server is the ChooseColourMessage for the choice of the pawn
     *
     * @param cmd the command used to choose the side of the initial card.
     */
    @Override
    public void receiveCommand(StartingCardSideCommand cmd) {
        if (currentState.equals(ClientControllerState.CHOOSING_STARTING_CARD_FACE)) {
            currentState = ClientControllerState.INITIAL_PHASE;
            sendMessage(new ChooseStartingCardSideMessage(cmd.getSide()));
        } else {
            gameView.display("\nYou can't place your starting card now!\n");
        }
    }

    /**
     * Handles the ChooseColourMessage indicating that the player should choose a colour.
     *
     * @param m the ChooseColourMessage received from the server.
     */
    @Override
    public void handle(ChooseColourMessage m) {
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.colourChoice(false);
    }

    /**
     * Receives the ColourCommand containing the player's chosen colour.<br>
     * The available colours are red, blue, green and yellow. They are represented by their Ansi Colour Codes escape codes
     *
     * @param cmd the command used to choose the colour.
     */
    @Override
    public void receiveCommand(ColourCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_COLOUR)) {
            gameView.display("\nYou can't choose the colour now\n");
            return;
        }
        currentState = ClientControllerState.AFTER_COLOUR_CHOICE;
        sendMessage(new ChosenColourMessage(cmd.getChosenColour()));
    }

    /**
     * Handles the ColourAlreadyChosenMessage indicating that the chosen colour is not available, and they have to make another choice.
     *
     * @param m the ColourAlreadyChosenMessage received from the server.
     */
    @Override
    public void handle(ColourAlreadyChosenMessage m) {
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.colourChoice(true);
    }

    /**
     * Handles the NotAColourMessage indicating that the chosen colour is invalid.<br>
     * This message is received only by the cli when an invalid colour input is sent
     *
     * @param m the NotAColourMessage received from the server.
     */
    @Override
    public void handle(NotAColourMessage m) {//Should only be received by the CLI
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.display("\nPlease send a valid colour\n");
    }

    /**
     * Handles the GameEndingAfterDisconnectionMessage indicating that another player has been disconnected and displays the winner.
     *
     * @param m the GameEndingAfterDisconnectionMessage received from the server.
     */
    @Override
    public void handle(GameEndingAfterDisconnectionMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        gameView.displayWinners(m.getFinalPlayerScore(), m.getWinners(), true);
        serverConnection.stopConnection();
        ClientMain.stop = true;
        if (ConstantValues.usingCLI) {
            System.exit(1);
        }
    }

    /**
     * Handles the GameEndingMessage indicating that the game has ended and updates the view with the final information.
     *
     * @param m the GameEndingMessage received from the server.
     */
    @Override
    public void handle(GameEndingMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        gameView.displayWinners(m.getFinalPlayerScore(), m.getWinners(), false);
        serverConnection.stopConnection();
        ClientMain.stop = true;
        if (ConstantValues.usingCLI) {
            System.exit(1);
        }
    }

    /**
     * Handles the ServerCantStartGameMessage indicating that there are problems with the start of the game.
     *
     * @param m the ServerCantStartGameMessage received from the server.
     */
    @Override
    public void handle(ServerCantStartGameMessage m) {
        gameView.display("\nThe game can't be started right now.\n");
    }

    /**
     * Handles the SecretObjectiveChoiceMessage indicating that the player should now choose their secret objective.
     *
     * @param m the SecretObjectiveChoiceMessage received from the server.
     */
    @Override
    public void handle(SecretObjectiveChoiceMessage m) {
        currentState = ClientControllerState.CHOOSING_OBJECTIVE;
        synchronized (viewLock) {
            gameView.secretObjectiveChoice(m.getFirstChoice(), m.getSecondChoice());
            gameView.showSecretObjectives();
        }
    }

    /**
     * Receives the SecretObjectiveCommand used by the player to choose the secret objective.
     *
     * @param cmd the command used to choose the secret objective.
     */
    @Override
    public void receiveCommand(SecretObjectiveCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_OBJECTIVE)) {
            gameView.display("\nNow is not the moment for the choice of an objective\n");
            return;
        }
        if (gameView.setSecretObjective(cmd.getObjective())) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
            sendMessage(new ChosenSecretObjectiveMessage(cmd.getObjective()));
        } else {
            gameView.display("\n" + cmd.getObjective() + " is not between your objective choices!\n");
        }
    }

    /**
     * Handles the StartPlayerTurnMessage indicating that the player's turn has begun.<br>
     * The next expected command will be a PlaceCardCommand
     *
     * @param m the StartPlayerTurnMessage received from the server.
     */
    @Override
    public void handle(StartPlayerTurnMessage m) {
        if (gameNotSoftLocked()) {
            currentState = ClientControllerState.REQUESTING_PLACEMENT;
            gameView.placingACard();
        } else {
            gameView.display("\nYour field has no more available corners! Your turn will be skipped\n");
        }
    }

    /**
     * Handles the PlayerCantPlayAnymoreMessage indicating that there are no more available positions to play a card on.<br>
     * The player's turn will be skipped form now until the end of the game
     *
     * @param m the PlayerCantPlayAnymoreMessage received from the server.
     */
    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {
        currentState = ClientControllerState.GAME_SOFT_LOCKED;
        gameView.display("\nYour field has no more available corners! Your turn will be skipped\n");
    }

    /**
     * Handles the OtherPlayerTurnUpdateMessage indicating an opponent's move and updates the view.
     *
     * @param m the OtherPlayerTurnUpdateMessage received from the server.
     */
    @Override
    public void handle(OtherPlayerTurnUpdateMessage m) {
        if (gameNotSoftLocked()) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
        }
        String opponent = m.getPlayerName();
        SimpleCard info = m.getPlacedCardInformation();
        synchronized (viewLock) {
            gameView.updateScoreTrack(m.getSharedField().getScoreTrack());
            gameView.updateOtherPlayerField(opponent, info.getID(), info.getX(), info.getY(), info.isFacingUp(), m.getVisibleSymbols());
            gameView.opponentMadeAMove(opponent);
        }
    }

    /**
     * Receives the PlaceCardCommand containing the information about the player's move.
     *
     * @param cmd the command containing the information about the player's move.
     */
    @Override
    public void receiveCommand(PlaceCardCommand cmd) {
        if (!gameNotSoftLocked()) {
            gameView.display("\nYou can't place a card. There are no more points where you could do that.\n");
        } else if (!currentState.equals(ClientControllerState.REQUESTING_PLACEMENT)) {
            gameView.display("\nYou can't place a card now\n");
        } else {
            currentState = ClientControllerState.WAITING_FOR_PLACEMENT_CONFIRMATION;
            lastPlayed = cmd.getCardID();
            synchronized (viewLock) {
                if (gameView.getPlayerHand().contains(lastPlayed)) {
                    sendMessage(new PlaceCardMessage(cmd.getXPosition(), cmd.getYPosition(), cmd.isFacingUP(), cmd.getCardID()));
                } else {
                    gameView.display("\nYou do not have a card with id:" + lastPlayed + " in your hand\n");
                    currentState = ClientControllerState.REQUESTING_PLACEMENT;
                }
            }
        }
    }

    /**
     * Handles the IllegalPlacementPositionMessage indicating that the player has made an illegal move and will be asked to place again
     *
     * @param m the IllegalPlacementPositionMessage received from the server.
     */
    @Override
    public void handle(IllegalPlacementPositionMessage m) {
        currentState = ClientControllerState.REQUESTING_PLACEMENT;
        gameView.display("\nThis position is not available for placement!\n");
    }

    /**
     * Handles the NotEnoughResourcesMessage indicating that a gold card was placed face up without enough visible symbols; the player will be asked to place again
     *
     * @param m the NotEnoughResourcesMessage received from the server.
     */
    @Override
    public void handle(NotEnoughResourcesMessage m) {
        currentState = ClientControllerState.REQUESTING_PLACEMENT;
        gameView.display("\nYou don't have enough resources to play this gold card!\n");
    }

    /**
     * Handles the SuccessfulPlacementMessage indicating that the player has placed a card successfully.<br>
     * The player will now have to draw a card from the decks/visible positions
     *
     * @param m the SuccessfulPlacementMessage received from the server.
     */
    @Override
    public void handle(SuccessfulPlacementMessage m) {
        if (!currentState.equals(ClientControllerState.INITIAL_PHASE)) {
            currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        }
        synchronized (viewLock) {
            gameView.updatePlayerHand(lastPlayed);

            SimpleCard info = m.getPlacedCardInformation();
            gameView.updateOwnerField(info.getID(), info.getX(), info.getY(), info.isFacingUp(), m.getVisibleSymbols());

            SharedFieldUpdateMessage tmp = m.getSharedField();
            gameView.updateDecks(tmp.getGoldBackside(), tmp.getResourceBackside(), tmp.getVisibleCards());
            gameView.updateScoreTrack(tmp.getScoreTrack());
            gameView.drawingACard(currentState.equals(ClientControllerState.INITIAL_PHASE));
        }
    }

    /**
     * Receives the DrawCardCommand used to choose a card to draw.
     *
     * @param cmd the command used to choose a card to draw.
     */
    @Override
    public void receiveCommand(DrawCardCommand cmd) {
        if (!currentState.equals(ClientControllerState.REQUESTING_DRAW_CARD)) {
            gameView.display("\nIt's not time to draw!\n");
        } else {
            currentState = ClientControllerState.WAITING_FOR_DRAW_CONFIRMATION;
            sendMessage(new DrawCardMessage(cmd.getChoice()));
        }
    }

    /**
     * Handles the EmptyDeckMessage indicating that the player tried to draw from an empty deck, they will have to draw again
     *
     * @param m the EmptyDeckMessage received from the server.
     */
    @Override
    public void handle(EmptyDeckMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.display("\nYou tried to draw from an empty deck. Change your choice.\n");
    }

    /**
     * Handles the EmptyDrawnCardPositionMessage indicating that the player tried to draw from an empty visible card position, they will have to draw again<br>
     * Note that this can only happen in the final phase of the game.
     *
     * @param m the EmptyDrawnCardPositionMessage received from the server.
     */
    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.display("\nYou tried to draw from an empty card position. Change your choice.\n");
    }

    /**
     * Handles the SendDrawnCardMessage and updates the view with the information about the drawn card.
     *
     * @param m the SendDrawnCardMessage received from the server.
     */
    @Override
    public void handle(SendDrawncardMessage m) {
        SharedFieldUpdateMessage temp = m.getSharedField();
        synchronized (viewLock) {
            gameView.updateDecks(temp.getGoldBackside(), temp.getResourceBackside(), temp.getVisibleCards());
            gameView.updateScoreTrack(temp.getScoreTrack());
            gameView.updatePlayerHand(m.getPlayerHand());
            gameView.receivedDrawnCard();
        }
    }

    /**
     * Handles the EndPlayerTurnMessage indicating the end of the player's turn and updates the view accordingly.
     *
     * @param m the EndPlayerTurnMessage received from the server.
     */
    @Override
    public void handle(EndPlayerTurnMessage m) {
        currentState = ClientControllerState.OTHER_PLAYER_TURN;
        gameView.display("\nYour turn has ended\n");
    }

    /**
     * Handles the SharedFieldUpdateMessage received at the end of an opponent's turn,
     * containing information about changes in the common field.
     *
     * @param m the SharedFieldUpdateMessage received from the server.
     */
    @Override
    public void handle(SharedFieldUpdateMessage m) {
        synchronized (viewLock) {
            gameView.updateDecks(m.getGoldBackside(), m.getResourceBackside(), m.getVisibleCards());
            gameView.updateScoreTrack(m.getScoreTrack());
            gameView.sharedFieldUpdate();
        }
    }


    /**
     * Handles the AvailablePositionsMessage and updates the view to show available positions.
     * This message is displayed only by the cli to help with the choice of an available position
     *
     * @param m the AvailablePositionsMessage received from the server.
     */
    @Override
    public void handle(AvailablePositionsMessage m) {
        currentState = (m.getPositions().isEmpty() || m.getPositions() == null) ? ClientControllerState.GAME_SOFT_LOCKED : currentState;
        synchronized (viewLock) {
            if(!gameNotSoftLocked()){
                gameView.display("There are no more available positions! Your turn will be skipped form now on!");
            }
            else if (gameNotSoftLocked() && ConstantValues.usingCLI) {
                ((GameViewCli) gameView).updateAvailablePositions(m.getPositions());
            }
        }
    }

    /**
     * Checks if the game is not soft-locked.
     *
     * @return false if the game is soft-locked, true if the player can continue playing.
     */
    private boolean gameNotSoftLocked() {
        return !ClientControllerState.GAME_SOFT_LOCKED.equals(currentState);
    }

    /**
     * Checks if the game is ongoing, helps other methods to determine whether to go on with their execution
     *
     * @return true if the game is ongoing and the game starting message has been received.
     */
    private boolean isGameOngoing() {
        return currentState.gameOngoing && receivedGameStarting;
    }

    /**
     * Receives the ShowFieldCommand to show the field of the user.
     *
     * @param cmd the command used to show the field of the user.
     */
    @Override
    public void receiveCommand(ShowFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                if (currentState.equals(ClientControllerState.REQUESTING_PLACEMENT)) {
                    gameView.placingACard();
                } else if (currentState.equals(ClientControllerState.REQUESTING_DRAW_CARD)) {
                    gameView.drawingACard(false);
                } else {
                    gameView.goToOwnerField();
                }
            }
        } else if (!ConstantValues.usingCLI) {
            Platform.runLater(GuiApplication::loadWaitingScreen);
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the ShowOtherFieldCommand to show the field of another player.
     *
     * @param cmd the command used to show the field of another player.
     */
    @Override
    public void receiveCommand(ShowOtherFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                if (cmd.getOpponentName() == null || cmd.getOpponentName().equals(clientName)) {
                    if (currentState.equals(ClientControllerState.REQUESTING_PLACEMENT)) {
                        gameView.placingACard();
                    } else if (currentState.equals(ClientControllerState.REQUESTING_DRAW_CARD)) {
                        gameView.drawingACard(false);
                    } else {
                        gameView.goToOwnerField();
                    }
                } else {
                    gameView.goToOpponentField(cmd.getOpponentName());
                }
            }
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the ShowLeaderboardCommand to show the leaderboard.<br>
     * This command is only generated in the CLI.
     *
     * @param cmd the command used to show the leaderboard.
     */
    @Override
    public void receiveCommand(ShowLeaderboardCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                ((GameViewCli) gameView).showLeaderBoard();
            }
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the ShowHandCommand to show the player's hand.<br>
     * This command is only generated in the CLI.
     *
     * @param cmd the command used to show the player's hand.
     */
    @Override
    public void receiveCommand(ShowHandCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                ((GameViewCli) gameView).printHand();
            }
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the ShowCommonFieldCommand to display the common field.<br>
     * This command is only generated in the CLI.
     *
     * @param cmd the command used to display the common field.
     */
    @Override
    public void receiveCommand(ShowCommonFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                ((GameViewCli) gameView).printCommonField();
            }
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the ShowObjectivesCommand to display the player's objectives.
     *
     * @param cmd the command used to display the player's objectives.
     */
    @Override
    public void receiveCommand(ShowObjectivesCommand cmd) {
        if (isGameOngoing() && !currentState.equals(ClientControllerState.CHOOSING_STARTING_CARD_FACE) && !currentState.equals(ClientControllerState.CHOOSING_COLOUR)) {
            synchronized (viewLock) {
                gameView.showSecretObjectives();
            }
        } else {
            gameView.display("\nYou can't do that now!\n");
        }
    }

    /**
     * Receives the EndGameCommand used when the player chooses to leave the lobby.
     * The connection to the server is closed and the program terminates.
     *
     * @param cmd the command used to leave the lobby.
     */
    @Override
    public void receiveCommand(EndGameCommand cmd) {
        if (currentState.equals(ClientControllerState.ENDING_CONNECTION) || currentState.equals(ClientControllerState.INIT) ||
                currentState.equals(ClientControllerState.CONNECTING) || currentState.equals(ClientControllerState.DISCONNECTED)) {
            gameView.display("Terminating the program");
            ClientMain.stop = true;
            return;
        }
        gameView.display("\nTerminating the connection\n");
        sendMessage(new ClientDisconnectedVoluntarilyMessage());
        currentState = ClientControllerState.DISCONNECTED;
        serverConnection.stopConnection();
        gameView.display("\nTerminating the program\n");
        ClientMain.stop = true;
        System.exit(1);
    }

    /**
     * Handles the GenericMessage containing information of interest to the player.
     *
     * @param m the GenericMessage received from the server.
     */
    @Override
    public void handle(GenericMessage m) {
        gameView.display("\n" + m.getMessage() + "\n");
    }

    /**
     * Handles the Pong message sent by the server in response to a Ping.
     *
     * @param pong the Pong message received from the server.
     */
    @Override
    public void handle(Pong pong) {
        serverConnection.pongWasReceived();
    }

    /**
     * Handles the disconnection from the server and terminates the program.
     */
    @Override
    public void disconnectionHappened() {
        if (currentState.equals(ClientControllerState.DISCONNECTED) || currentState.equals(ClientControllerState.GAME_ENDING)) {
            return;
        }
        currentState = ClientControllerState.DISCONNECTED;
        if (ConstantValues.usingCLI) {
            gameView.display("You were disconnected from the server!\n\nThe program wil now close...");
            System.exit(1);
        } else {
            gameView.disconnection();
        }
    }

    /**
     * Receives the CardDetailCommand to display details of a card with the specified ID.
     * The view will print the card information extracted with {@link Card}'s printCardInfo method.<br>
     * This command can only be generated in the CLI.
     *
     * @param cmd the command containing the card ID.
     */
    public void receiveCommand(CardDetailCommand cmd) {
        if (cmd.getId() < 1 || cmd.getId() > 102) {
            gameView.display("\nNot a card ID!!!\n");
            return;
        }
        GameViewCli.printCardDetailed(cmd.getId());
    }

    /**
     * Handles the InitialPhaseDisconnectionMessage indicating that someone was disconnected during the initial phase.
     * The program will be closed
     *
     * @param m the InitialPhaseDisconnectionMessage received from the server.
     */
    @Override
    public void handle(InitialPhaseDisconnectionMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        gameView.disconnection();
        serverConnection.stopConnection();
        ClientMain.stop = true;
        if (ConstantValues.usingCLI) { //The cli closes immediately because the information can still be accessed by the terminal
            gameView.display("\n\nClosing the program...\n");
            System.exit(1);
        }
    }

    /**
     * Receives the ChatCommand containing information about the chat message's body and its recipients.
     *
     * @param cmd the command containing the chat message details.
     */
    @Override
    public void receiveCommand(ChatCommand cmd) {
        if (currentState.equals(ClientControllerState.INIT) || currentState.equals(ClientControllerState.CHOOSING_NAME) || currentState.equals(ClientControllerState.CONNECTING)
                || currentState.equals(ClientControllerState.ENDING_CONNECTION) || currentState.equals(ClientControllerState.DISCONNECTED)) {
            gameView.display("\nYou can't chat now!!!\n");
            return;
        }
        synchronized (chatLogs) {
            String msg = "You said to " + (cmd.isGlobal() ? "everyone" : cmd.getHead()) + ": <" + cmd.getBody() + ">";
            chatLogs.add(msg);
            if (!ConstantValues.usingCLI) {
                Platform.runLater(() ->
                        ((ChatController) GuiApplication.getCurrentController()).updateChat(msg));
            }
        }
        sendMessage(new ChatMessage(cmd.isGlobal(), cmd.getHead(), cmd.getBody()));
    }

    /**
     * Handles the ReceivedChatMessage, which can be either a private chat message or a global chat message.
     *
     * @param m the ReceivedChatMessage received from the server.
     */
    @Override
    public void handle(ReceivedChatMessage m) {
        synchronized (chatLogs) {
            chatLogs.add(m.getBody());
        }
        gameView.receivedChat(m.getBody());
    }

    /**
     * Receives the ChatLogCommand to show the chat logs, containing messages that were sent to non-existent players.
     *
     * @param cmd the command to show the chat logs.
     */
    @Override
    public void receiveCommand(ChatLogCommand cmd) {
        gameView.displayChat(chatLogs);
    }
}
