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
 * Controller for the client, it handles all messages that can be received and handles the user input
 */
public class ClientController implements ClientSideMessageListener, UserListener {
    private String clientIP;
    private GameView gameView;
    private String clientName="";
    private ClientSocket serverConnection;
    private ClientControllerState currentState;

    /**
     * Creates a new ClientController object. To start connecting to the server the method .begin() must be called
     */
    public ClientController() {
        this.gameView = null;
        serverConnection =null;
    }

    /**
     * After the player has chosen IP and port of the serer the connection is started.
     * Two new threads are created: one to receive messages from the server and queuing them and one to extract them from the queue and executing them.
     */
    private void begin(){
        currentState =ClientControllerState.CONNECTING;
        serverConnection = new ClientSocket(this);
        new Thread(()->serverConnection.receiveMessages()).start();
        new Thread(()->serverConnection.passMessages()).start();
        sendMessage(new FindLobbyMessage());
    }

    /**
     * Sends a message to the server and handles any IOExceptions that may arise
     */
    private void sendMessage(ClientToServerMessage mes){
        try {
            serverConnection.send(mes);
        }catch (IOException e){//TODO: disconnection behaviour
            System.err.println("Error while sending a message");
            throw new RuntimeException();
        }
    }
    @Override
    public void handle(ServerToClientMessage m) {
        System.err.println("Unhandled message received");
    }
    /**
     * When an AvailablePositionsMessage is received the view is updated to show them if there are any
     */
    @Override
    public void handle(AvailablePositionsMessage m) {
        currentState=m.getPositions().isEmpty()?ClientControllerState.GAME_SOFT_LOCKED:currentState;
        if(gameNotSoftLocked()) {
            gameView.updateAvailablePositions(m.getPositions());
            gameView.printOwnerField();
        }else{
            gameView.showText("There are no more available positions!");
        }
    }

    /**
     * The controller now waits for a UserCommand that tells how many players will pay the game; most of the other commands
     * are rejected
     */
    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        currentState = ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS;
        gameView.showText("Choose the number of players that will play in this game");
    }

    /**
     * A command to choose how many players would be playing was sent to the server that has answered with a
     * ClientCantStartGameMessage as the client didn't receive a ChooseHowManyPlayersMessage.
     */
    @Override
    public void handle(ClientCantStartGameMessage m) {
        gameView.showText("You don't have permission to do this");
    }

    @Override
    public void handle(ClientFieldCheckValidityMessage m) {

    }

    /**
     * The player tried to draw from an empty deck, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDeckMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("You tried to draw from an empty deck. Change your choice.");
    }

    /**
     *The player tried to draw from an empty visible card position, the player is now asked again to draw a card.
     */
    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("This card position is empty. Change your drawing choice");
    }

    /**
     * Signals the end of a player's turn and updates the view according to the received information
     */
    @Override
    public void handle(EndPlayerTurnMessage m) {
        currentState=ClientControllerState.OTHER_PLAYER_TURN;
    }

    /**
     * The user tried to connect to a game that was already started, they will be disconnected
     */
    @Override
    public void handle(GameAlreadyStartedMessage m) {
        currentState=ClientControllerState.ENDING_CONNECTION;
        gameView.showText("This game has already started, you can't participate!");
        gameView.showText("You will be disconnected from the server.");
        try {
            serverConnection.stopConnection();
        }catch (IOException e){
            System.err.println("IOException while terminating the connection.");
        }
    }

    /**
     * Another player has been disconnected, now the winner of the incomplete game will be displayed
     */
    @Override
    public void handle(GameEndingAfterDisconnectionMessage m) {
        currentState=ClientControllerState.GAME_ENDING;
//TODO
        try {
            serverConnection.stopConnection();
        }catch (IOException e){
            System.err.println("IOException while terminating the connection.");
        }
    }

    /**
     * The game has ended, the view will be updated with the final information
     */
    @Override
    public void handle(GameEndingMessage m) {
        currentState=ClientControllerState.GAME_ENDING;
        //TODO declare winner/s
        try {
            serverConnection.stopConnection();
        }catch (IOException e){
            System.err.println("IOException while terminating the connection.");
        }
    }

    /**
     * The game has started, the view will be updated with the initial information. TYhe player now has to choose how to place their starting card
     */
    @Override
    public void handle(GameStartingMessage m){
        currentState=ClientControllerState.CHOOSING_STARTING_CARD_FACE;
        try {
            gameView = new GameView(m.getPlayerHand(), m.getPlayersInfo(), clientName, m.getStartingCard());
        }catch (IOException e){
            System.err.println("Error initializing the view");
            throw  new RuntimeException();
        }
        gameView.printCommonField();
        gameView.printHand();
        gameView.showText("Place your starting card.");
    }

    /**
     * The player has made an illegal move, they are asked to make another move
     */
    @Override
    public void handle(IllegalPlacementPositionMessage m) {
        currentState=ClientControllerState.REQUESTING_PLACEMENT;
        gameView.showText("This position is not available for placement!");
    }

    /**
     * The player has successfully connected to the lobby, they can now choose their name
     */
    @Override
    public void handle(LobbyFoundMessage m) {
        currentState=ClientControllerState.CHOOSING_NAME;
        //TODO: using the user interaction class to get the name string.And temporarily set it as clientName
        gameView.showText("Connected successfully to a game. Now choose your name");
    }

    /**
     * The player can't connect to the lobby as it is full. The connection will be interrupted
     */
    @Override
    public void handle(LobbyFullMessage m) {
        currentState=ClientControllerState.ENDING_CONNECTION;
        //TODO: decide how to display this message
    }

    /**
     * The player has chosen a valid name. They will now wait for either a message that signals the start of the game or for a
     * ChooseHowManyPlayersMessage
     */
    @Override
    public void handle(NameChosenSuccessfullyMessage m) {
        currentState=ClientControllerState.WAITING_FOR_START;
        gameView.showText("Name chosen successfully!");
    }

    /**
     * A new name must be chosen by the user.
     */
    @Override
    public void handle(NameNotAvailableMessage m){
        currentState=ClientControllerState.CHOOSING_NAME;
        gameView.showText("Your name is already chosen, choose another name.");
    }

    /**
     * A gold card was placed face up when there weren't enough visible symbols. So the player must make another move.
     */
    @Override
    public void handle(NotEnoughResourcesMessage m) {
        currentState=ClientControllerState.REQUESTING_PLACEMENT;
        gameView.showText("You don't have enough resources to play this gold card!\n");
        gameView.printOwnerField();
    }

    /**
     * A message regarding another player's move has been received. The view will be updated and shown.
     */
    @Override
    public void handle(OtherPlayerTurnUpdateMessage m) {
        if(gameNotSoftLocked()) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
        }
        String opponent=m.getPlayerName();
        PlayerPlacedCardInformation info= m.getPlacedCardInformation();
        gameView.updateOtherPlayerField(opponent, info.getCardId(), info.getXPos(), info.getYPos(), info.isFacingUp(), m.getVisibleSymbols());
        gameView.showText(opponent+" has made a move!\n");
        gameView.printOpponentField(opponent);
    }

    /**
     * When there are no more available positions to play a card in, this message is received.
     * The player turn will now be skipped
     */
    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {
        currentState=ClientControllerState.GAME_SOFT_LOCKED;
        gameView.showText("Your field has no more available corners! Your turn will be skipped");
    }

    /**
     * The view is updated by showing the player their objective choice. Any command
     * other than their choice will be rejected.
     */
    @Override
    public void handle(SecretObjectiveChoiceMessage m) {
        currentState=ClientControllerState.CHOOSING_OBJECTIVE;
        gameView.secretObjectiveChoice(m.getFirstChoice(),m.getSecondChoice());
    }

    /**
     *
     */
    @Override
    public void handle(SendDrawncardMessage m) {
        SharedFieldUpdateMessage temp = m.getSharedField();
        gameView.updateDecks(temp.getGoldBackside(),temp.getResourceBackside(),temp.getVisibleCards());
        gameView.updateScoreTrack(temp.getScoreTrack());
        gameView.updatePlayerHand(m.getPlayerHand());
        gameView.printCommonField();
        gameView.printHand();
    }
    /**
     * Message sent when there are problems with the start of the game. The client will continue waiting
     */
    @Override
    public void handle(ServerCantStartGameMessage m) {
        gameView.showText("The game can't be started right now.");
    }

    /**
     * Message received at the end of an opponent's turn, it contains information about how the common field has changed after their
     * drawing phase.
     */
    @Override
    public void handle(SharedFieldUpdateMessage m) {
        gameView.updateDecks(m.getGoldBackside(),m.getResourceBackside(),m.getVisibleCards());
        gameView.printCommonField();
    }

    /**
     * The player's turn has begun. They will now have to send
     * a command containing information on the placement they want to make
     */
    @Override
    public void handle(StartPlayerTurnMessage m) {
        if(gameNotSoftLocked()) {
            currentState = ClientControllerState.REQUESTING_PLACEMENT;
            gameView.showText("It's your turn, now place a card!");

        }else{
            gameView.showText("Your field has no more available corners! Your turn will be skipped");
        }
        gameView.printOwnerField();
    }

    /**
     * The player has placed a card in an available position. They will now be requested to draw a card.
     */
    @Override
    public void handle(SuccessfulPlacementMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("Now draw a card!");
        gameView.printCommonField();
    }

    /**
     * @return false if the game is soft-locked, true if the player can continue playing
     */
    private boolean gameNotSoftLocked() {
        return ClientControllerState.GAME_SOFT_LOCKED != currentState;
    }

    /**
     * @param cmd is used to connect to the lobby
     */
    @Override
    public void receiveCommand(JoinLobbyCommand cmd) {
        ConstantValues.setServerIp(cmd.getIp());
        ConstantValues.setSocketPort(cmd.getPort());
        this.begin();
    }

    /**
     * @param cmd requests the available position for a card
     */
    @Override
    public void receiveCommand(AvailablePositionCommand cmd) {
        sendMessage(new RequestAvailablePositionsMessage());
    }

    /**
     * @param cmd is used when the player choose to leave the lobby
     */
    @Override
    public void receiveCommand(EndGameCommand cmd) {
        sendMessage(new ClientDisconnectedVoluntarilyMessage());
        try {
            serverConnection.stopConnection();
        }catch(IOException e){
            System.err.println("Error closing the connection");
            throw new RuntimeException();
        }
    }

    /**
     * @param cmd is used to reque
     */
    @Override
    public void receiveCommand(NumberOfPlayerCommand cmd) {
        if(currentState!=ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS){
            gameView.showText("At the moment you can't choose the number of players");
        }
        else{
            sendMessage(new NumberOfPlayersMessage(cmd.getNumberOfPlayer()));
            currentState= ClientControllerState.WAITING_FOR_START;
        }
    }

    /**
     * @param cmd is used to choose a name
     */
    @Override
    public void receiveCommand(NameCommand cmd) {
        if(currentState!=ClientControllerState.CHOOSING_NAME){
            gameView.showText("You can't choose the name now!");
        }else{
            sendMessage(new ChooseNameMessage(cmd.getName()));
            currentState=ClientControllerState.WAITING_FOR_START;
        }
    }
    /**
     * @param cmd
     */
    @Override
    public void receiveCommand(PlaceCardCommand cmd) {
        if(currentState!=ClientControllerState.REQUESTING_PLACEMENT){
            gameView.showText("You can't place a card now");
        }else{
            currentState=ClientControllerState.WAITING_FOR_PLACEMENT_CONFIRMATION;
            sendMessage(new PlaceCardMessage(cmd.getXPosition(), cmd.getYPosition(),cmd.isFacingUP(), cmd.getCardID()));
        }
    }
}
