package it.polimi.ingsw.controller;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.ClientRMI;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Pong;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.view.GameView;
import it.polimi.ingsw.view.GameViewCli;
import it.polimi.ingsw.view.GameViewGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Controller for the client, it handles all messages that can be received and handles the user input.
 * Implements the design pattern singleton.
 */
public class ClientController implements ClientSideMessageListener, UserListener {
    private int lastPlayed;
    private static ClientController instance = null;
    private final GameView gameView;
    private String clientName = "";
    private ClientConnection serverConnection;
    private ClientControllerState currentState;
    /**
     * Attribute used to synchronize the gameView attribute
     */
    private final Object viewLock = new Object();
    private final List<String> chatLogs = new ArrayList<>();

    /**
     * Creates a new ClientController object. To start connecting to the server a joinLobbyCommand must be received
     */
    private ClientController() {
        if (ConstantValues.usingCLI) {
            gameView = new GameViewCli();
        } else {
            gameView = new GameViewGui();
        }
        serverConnection = null;
        currentState = ClientControllerState.INIT;
    }

    /**
     * Implementing the singleton design pattern this method creates the instance of the controller
     * the first time it is called, then it returns that instance<br>
     * To start connecting to the server joinLobbyCommand must be received
     *
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
        if (ConstantValues.usingSocket) {
            serverConnection = new ClientSocket(this);
        } else {
            serverConnection = new ClientRMI(this);
        }
        new Thread(() -> serverConnection.receiveMessages()).start();//starts the reception of messages from the server
        new Thread(() -> serverConnection.passMessages()).start();//starts passing messages to the ClientController
        new Thread(() -> serverConnection.sendPing()).start();//Starts the sending of pings to the server
        new Thread(() -> serverConnection.checkConnectionStatus()).start();//starts checking if a disconnection has happened
        sendMessage(new FindLobbyMessage());
    }

    /**
     * Sends a message to the server and handles any IOExceptions that may arise
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
     * When a message that is not supported is received an error is printed
     */
    @Override
    public void handle(ServerToClientMessage m) {
        System.err.println("\nUnhandled message received\n");
    }

    /**
     * @param cmd is used to connect to the lobby
     */
    @Override
    public void receiveCommand(JoinLobbyCommand cmd) {
        //If a connection has ended the player could want to play again/ try another server connection
        if (currentState.equals(ClientControllerState.INIT) || currentState.equals(ClientControllerState.ENDING_CONNECTION)) {
            ConstantValues.setServerIp(cmd.getIp());
            ConstantValues.setSocketPort(cmd.getPort());
            currentState = ClientControllerState.CONNECTING;
            this.begin();
        } else if (!currentState.equals(ClientControllerState.DISCONNECTED)) {//This two ifs can only be entered by the CLI
            gameView.display("\nYou are already connected to " + ConstantValues.serverIp + ":" + ConstantValues.socketPort + "\n",currentState);
        } else {
            gameView.display("\nYou were disconnected as " + clientName + " from " + ConstantValues.serverIp + ":" + ConstantValues.socketPort + "\n" +
                    "That game has now ended\n",currentState);
        }
    }

    /**
     * The player can't connect to the lobby as it is full. The connection will be interrupted
     */
    @Override
    public void handle(LobbyFullMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        gameView.display("\nThe lobby was already full! Your connection will be terminated!\n",currentState);
        serverConnection.stopConnection();
    }

    /**
     * The user tried to connect to a game that was already started, they will be disconnected
     */
    @Override
    public void handle(GameAlreadyStartedMessage m) {
        currentState = ClientControllerState.ENDING_CONNECTION;
        gameView.display("""
                This game has already started, you can't participate!
                 You will be disconnected from the server.
                """,currentState);
        serverConnection.stopConnection();
    }

    /**
     * The player has successfully connected to the lobby, they can now choose their name
     */
    @Override
    public void handle(LobbyFoundMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;//TODO: information about who's present ecc
        gameView.nameChoice();
    }

    /**
     * @param cmd is used to choose a name
     */
    @Override
    public void receiveCommand(NameCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_NAME)) {
            //Should only be reached in the CLI
            gameView.display("\nYou can't choose the name now!\n",currentState);
        } else {
            clientName = cmd.getName();
            currentState = ClientControllerState.WAITING_FOR_NAME_CONFIRMATION;
            sendMessage(new ChooseNameMessage(clientName));
        }
    }

    /**
     * A new name must be chosen by the user.
     */
    @Override
    public void handle(NameNotAvailableMessage m) {
        currentState = ClientControllerState.CHOOSING_NAME;
        gameView.nameNotAvailable(clientName);
        clientName = "";
    }

    /**
     * The player has chosen a valid name. They will now wait for either a message that signals
     * the start of the game or for a ChooseHowManyPlayersMessage
     */
    @Override
    public void handle(NameChosenSuccessfullyMessage m) {
        currentState = ClientControllerState.WAITING_FOR_START;
        gameView.waitingForStart();
    }

    /**
     * The controller now waits for a UserCommand that tells how many players will pay the game;
     * most of the other commands are rejected
     */
    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        currentState = ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS;
        gameView.chooseNumPlayers();
    }

    /**
     * The user sent a command with the number of players they want to have in that game.
     * If they do not have the right to do that the command is refused
     */
    @Override
    public void receiveCommand(NumberOfPlayerCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS)) {
            gameView.display("\nAt the moment you can't choose the number of players\n",currentState);
        } else if (cmd.getNumberOfPlayer() < 2) {
            gameView.display("\nThe minimum number of players is 2\n",currentState);
        } else if (cmd.getNumberOfPlayer() > 4) {
            gameView.display("\nThe maximum number of players is 4\n",currentState);
        } else {
            currentState = ClientControllerState.WAITING_FOR_START;
            sendMessage(new NumberOfPlayersMessage(cmd.getNumberOfPlayer()));
        }
    }

    /**
     * A command to choose how many players would be playing was sent to the server that has answered with a
     * ClientCantStartGameMessage as the client didn't receive a ChooseHowManyPlayersMessage.
     */
    @Override
    public void handle(ClientCantStartGameMessage m) {
        gameView.display("\nYou don't have permission to start the game\n",currentState);
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
                gameView.gameStarting(m.getPlayersInfo(), clientName, m.getStartingCard(), m.getFirstCommonObjective(), m.getSecondCommonObjective(), m.firstPlayerName());
                SharedFieldUpdateMessage tmp = m.getSharedFieldData();
                gameView.updateDecks(tmp.getGoldBackside(), tmp.getResourceBackside(), tmp.getVisibleCards());
                gameView.updatePlayerHand(m.getPlayerHand());
            } catch (IOException e) {
                System.err.println("\nError initializing the view\n");
                System.exit(-1);
            }
            gameView.printStartingInfo();
        }
    }

    /**
     * @param cmd is used by the player to choose the side of the initial card
     */
    @Override
    public void receiveCommand(StartingCardSideCommand cmd) {
        if (currentState.equals(ClientControllerState.CHOOSING_STARTING_CARD_FACE)) {
            currentState = ClientControllerState.INITIAL_PHASE;
            sendMessage(new ChooseStartingCardSideMessage(cmd.getSide()));
        } else {
            gameView.display("\nYou can't place your starting card now!\n",currentState);
        }
    }

    /**
     * The listener is notified that the next move by the player should be the choice of the colour
     */
    @Override
    public void handle(ChooseColourMessage m) {
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.colourChoice(false);
    }

    /**
     * The listener is sent information on the choice of the player
     */
    @Override
    public void receiveCommand(ColourCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_COLOUR)) {
            gameView.display("\nYou can't choose the colour now\n",currentState);
            return;
        }
        currentState = ClientControllerState.AFTER_COLOUR_CHOICE;
        sendMessage(new ChosenColourMessage(cmd.getChosenColour()));
    }

    /**
     * Response to a ChosenColourMessage if the colour was not available
     */
    @Override
    public void handle(ColourAlreadyChosenMessage m) {
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.colourChoice(true);
    }

    /**
     * Response to a ChosenColourMessage if the message did not contain a valid colour
     */
    @Override
    public void handle(NotAColourMessage m) {//Should only be received by the CLI
        currentState = ClientControllerState.CHOOSING_COLOUR;
        gameView.display("\nPlease send a valid colour\n",currentState);
    }

    /**
     * Another player has been disconnected, now the winner of the incomplete game will be displayed
     */
    @Override
    public void handle(GameEndingAfterDisconnectionMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        printSpacer(100);
        gameView.displayWinners(m.getFinalPlayerScore(), m.getWinners(),true);
        serverConnection.stopConnection();
        ClientMain.stop=true;
    }

    /**
     * The game has ended, the view will be updated with the final information
     */
    @Override
    public void handle(GameEndingMessage m) {
        printSpacer(100);
        currentState = ClientControllerState.GAME_ENDING;
        gameView.displayWinners(m.getFinalPlayerScore(), m.getWinners(),false);
        serverConnection.stopConnection();
        ClientMain.stop=true;
    }

    /**
     * Message sent when there are problems with the start of the game. The client will continue waiting
     */
    @Override
    public void handle(ServerCantStartGameMessage m) {
        gameView.display("\nThe game can't be started right now.\n",currentState);
    }

    /**
     * The view is updated by showing the player their objective choice. Any command
     * other than their choice will be rejected.
     */
    @Override
    public void handle(SecretObjectiveChoiceMessage m) {
        currentState = ClientControllerState.CHOOSING_OBJECTIVE;
        printSpacer(100);
        synchronized (viewLock) {
            gameView.secretObjectiveChoice(m.getFirstChoice(), m.getSecondChoice());
        }
    }

    /**
     * @param cmd is used by the player to choose the secret objective
     */
    @Override
    public void receiveCommand(SecretObjectiveCommand cmd) {
        if (!currentState.equals(ClientControllerState.CHOOSING_OBJECTIVE)) {
            gameView.display("\nNow is not the moment for the choice of an objective\n",currentState);
            return;
        }
        if (gameView.setSecretObjective(cmd.getObjective())) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
            sendMessage(new ChosenSecretObjectiveMessage(cmd.getObjective()));
        } else {
            gameView.display("\n" + cmd.getObjective() + " is not between your objective choices!\n",currentState);
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
            gameView.placingACard();
        } else {
            gameView.display("\nYour field has no more available corners! Your turn will be skipped\n",currentState);
        }
    }

    /**
     * When there are no more available positions to play a card in, this message is received.
     * The player turn will now be skipped
     */
    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {
        currentState = ClientControllerState.GAME_SOFT_LOCKED;
        gameView.display("\nYour field has no more available corners! Your turn will be skipped\n",currentState);
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
        SimpleCard info = m.getPlacedCardInformation();
        synchronized (viewLock) {
            printSpacer(100);
            gameView.updateOtherPlayerField(opponent, info.getID(), info.getX(), info.getY(), info.isFacingUp(), m.getVisibleSymbols());
            gameView.opponentMadeAMove(opponent);
        }
    }

    /**
     * @param cmd contains the information about the player's move
     */
    @Override
    public void receiveCommand(PlaceCardCommand cmd) {
        if (!gameNotSoftLocked()) {
            gameView.display("\nYou can't place a card. There are no more points where you could do that.\n",currentState);
        } else if (!currentState.equals(ClientControllerState.REQUESTING_PLACEMENT)) {
            gameView.display("\nYou can't place a card now\n",currentState);
        } else {
            currentState = ClientControllerState.WAITING_FOR_PLACEMENT_CONFIRMATION;
            lastPlayed = cmd.getCardID();
            synchronized (viewLock) {
                if (gameView.getPlayerHand().contains(lastPlayed)) {
                    sendMessage(new PlaceCardMessage(cmd.getXPosition(), cmd.getYPosition(), cmd.isFacingUP(), cmd.getCardID()));
                } else {
                    gameView.display("\nYou do not have a card with id:" + lastPlayed + " in your hand\n",currentState);
                    currentState = ClientControllerState.REQUESTING_PLACEMENT;
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
        gameView.display("\nThis position is not available for placement!\n",currentState);
    }

    /**
     * A gold card was placed face up when there weren't enough visible symbols. So the player must make another move.
     */
    @Override
    public void handle(NotEnoughResourcesMessage m) {
        currentState = ClientControllerState.REQUESTING_PLACEMENT;
        gameView.display("\nYou don't have enough resources to play this gold card!\n",currentState);
    }

    /**
     * The player has placed a card in an available position. They will now be requested to draw a card.
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
            gameView.successfulPlacement(currentState.equals(ClientControllerState.INITIAL_PHASE));
        }
    }

    /**
     * @param cmd is used to choose a card to draw
     */
    @Override
    public void receiveCommand(DrawCardCommand cmd) {
        if (!currentState.equals(ClientControllerState.REQUESTING_DRAW_CARD)) {
            gameView.display("\nIt's not time to draw!\n",currentState);
        } else {
            currentState = ClientControllerState.WAITING_FOR_DRAW_CONFIRMATION;
            sendMessage(new DrawCardMessage(cmd.getChoice()));
        }
    }

    /**
     * The player tried to draw from an empty deck, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDeckMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.display("\nYou tried to draw from an empty deck. Change your choice.\n",currentState);
    }
//TODO:make it so that in the guy the unavailable draw choices cannot be selected
    /**
     * The player tried to draw from an empty visible card position, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {
        currentState = ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.display("""

                This card position is empty. Draw again.
                Type 'd' or 'draw' followed by
                'g' for the top card of the gold deck
                'g1' for the first visible gold card
                'g2' for the second visible gold card
                'r' for the top card f the gold deck
                'r1' for the first visible resource card
                'r2' for the second visible resource card
                                        
                """,currentState);
    }

    /**
     * The controller updates the view with the information about the drawn card
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
     * Signals the end of a player's turn and updates the view according to the received information
     */
    @Override
    public void handle(EndPlayerTurnMessage m) {
        currentState = ClientControllerState.OTHER_PLAYER_TURN;
        gameView.display("\nYour turn has ended\n",currentState);
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
            gameView.sharedFieldUpdate();
        }
    }

    /**
     * @param cmd requests the available position for a card
     */
    @Override
    public void receiveCommand(AvailablePositionCommand cmd) {//TODO: remove available positions command as they are now sent after every move and not shown immediately
        if (isGameOngoing())
            sendMessage(new RequestAvailablePositionsMessage());
        else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * When an AvailablePositionsMessage is received the view is updated to show where the available positions are
     */
    @Override
    public void handle(AvailablePositionsMessage m) {
        currentState = (m.getPositions().isEmpty() || m.getPositions() == null) ? ClientControllerState.GAME_SOFT_LOCKED : currentState;
        synchronized (viewLock) {
            if (gameNotSoftLocked()) {
                gameView.updateAvailablePositions(m.getPositions());
            } else {
                gameView.display("\nThere are no more available positions! Your turn will be skipped form now on!\n",currentState);
            }
        }
    }

    /**
     * @return false if the game is soft-locked, true if the player can continue playing
     */
    private boolean gameNotSoftLocked() {
        return !ClientControllerState.GAME_SOFT_LOCKED.equals(currentState);
    }

    /**
     * @return true if the controller is in a state where the player can correctly request information about the game state
     */
    private boolean isGameOngoing() {
        return currentState.gameOngoing;
    }

    /**
     * @param cmd is used to show the field of the user
     */
    @Override
    public void receiveCommand(ShowFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                printSpacer(100);
                gameView.goToOwnerField();
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * @param cmd is used to show the field of a player
     */
    @Override
    public void receiveCommand(ShowOtherFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                if (cmd.getOpponentName().equals(clientName)) {
                    gameView.goToOwnerField();
                } else {
                    gameView.goToOpponentField(cmd.getOpponentName());
                }
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * The view will show the leaderboard at that moment<br>
     * This command is only generated in the CLI
     */
    @Override
    public void receiveCommand(ShowLeaderboardCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                ((GameViewCli)gameView).showLeaderBoard();
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * The view will sho the player their hand<br>
     * This command is only generated in the CLI
     */
    @Override
    public void receiveCommand(ShowHandCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                printSpacer(5);
                ((GameViewCli)gameView).printHand();
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * This command is only generated in the CLI
     * @param cmd is used to display the common field
     */
    @Override
    public void receiveCommand(ShowCommonFieldCommand cmd) {
        if (isGameOngoing()) {
            synchronized (viewLock) {
                printSpacer(100);
                ((GameViewCli)gameView).printCommonField();
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * @param cmd is used to display the player's objectives
     */
    @Override
    public void receiveCommand(ShowObjectivesCommand cmd) {
        if (isGameOngoing() && !currentState.equals(ClientControllerState.CHOOSING_STARTING_CARD_FACE) && !currentState.equals(ClientControllerState.CHOOSING_COLOUR)) {
            synchronized (viewLock) {
                printSpacer(1);
                gameView.showSecretObjectives();
            }
        } else {
            gameView.display("\nYou can't do that now!\n",currentState);
        }
    }

    /**
     * @param cmd is used when the player choose to leave the lobby. The connection to the server is closed and the program terminates
     */
    @Override
    public void receiveCommand(EndGameCommand cmd) {
        if (currentState.equals(ClientControllerState.ENDING_CONNECTION) || currentState.equals(ClientControllerState.INIT) ||
                currentState.equals(ClientControllerState.CONNECTING)) {
            gameView.display("Terminating the program",currentState);
            ClientMain.stop=true;
            return;
        }
        if (currentState.equals(ClientControllerState.DISCONNECTED)) {
            gameView.display("Terminating the program",currentState);
            ClientMain.stop=true;
            return;
        }
        gameView.display("\nTerminating the connection\n",currentState);
        sendMessage(new ClientDisconnectedVoluntarilyMessage());
        currentState = ClientControllerState.DISCONNECTED;
        serverConnection.stopConnection();
        gameView.display("\nTerminating the program\n",currentState);
        //TODO: decide on a method to close the gui application
        ClientMain.stop=true;
    }

    /**
     * @param m is a message that contains information of interest to the player.
     */
    @Override
    public void handle(GenericMessage m) {
        gameView.display("\n" + m.getMessage() + "\n",currentState);
    }

    /**
     * @param pong is the message sent by the server in response to a Ping
     */
    @Override
    public void handle(Pong pong) {
        serverConnection.pongWasReceived();
    }

    /**
     * The client was disconnected from the server, the program will be terminated
     */
    @Override
    public void disconnectionHappened() {
        if (currentState.equals(ClientControllerState.DISCONNECTED)) {
            return;
        }
        currentState = ClientControllerState.DISCONNECTED;
        gameView.display("\n\nYou are now disconnected from the server\n",currentState);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO: close the program?
    }

    /**
     * The controller is asked details on a card with the id specified in the command<br>
     * The view will print the card information extracted with {@link Card}'s printCardInfo method<br>
     * The command can only be generated in the cli
     */
    public void receiveCommand(CardDetailCommand cmd) {
        if (cmd.getId() < 1 || cmd.getId() > 102) {
            gameView.display("\nNot a card ID!!!\n",currentState);
            return;
        }
        GameViewCli.printCardDetailed(cmd.getId());
    }

    private void printSpacer(int n) {
        System.out.println("\n".repeat(n));
    }


    /**
     * Terminates the connection to the server after someone was disconnected
     */
    @Override
    public void handle(InitialPhaseDisconnectionMessage m) {
        currentState = ClientControllerState.GAME_ENDING;
        gameView.display("\n\nA disconnection has occurred, the game will be terminated!!\n\n",currentState);
        serverConnection.stopConnection();
        ClientMain.stop=true;
        //TODO: close the program?
    }

    /**
     *
     * @param cmd contains information about the chat message's body and its recipients
     */
    @Override
    public void receiveCommand(ChatCommand cmd) {
        if (currentState.equals(ClientControllerState.INIT) || currentState.equals(ClientControllerState.CHOOSING_NAME) || currentState.equals(ClientControllerState.CONNECTING)
                || currentState.equals(ClientControllerState.ENDING_CONNECTION) || currentState.equals(ClientControllerState.DISCONNECTED)) {
            gameView.display("\nYou can't chat now!!!\n",currentState);
            return;
        }
        synchronized (chatLogs) {
            chatLogs.add("You said to " + (cmd.isGlobal() ? "everyone" : cmd.getHead()) + ": <" + cmd.getBody()+">");
        }
        sendMessage(new ChatMessage(cmd.isGlobal(), cmd.getHead(), cmd.getBody()));
    }

    /**
     *
     * @param m is the received chat message, it can be either a private chat message or a global chat message
     */
    @Override
    public void handle(ReceivedChatMessage m) {
        synchronized (chatLogs) {
            chatLogs.add(m.getBody());
        }
        gameView.receivedChat("\n" + m.getBody() + "\n");
    }

    /**
     * Shows the chat logs, containing messages that were sent to non-existent players<br>
     */
    @Override
    public void receiveCommand(ChatLogCommand cmd) {
        gameView.display("Here is the chat history:\n\n",currentState);
        gameView.displayChat(chatLogs);
    }
}
