package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.userCommands.UserCommand;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.network.socket.client.ClientSocket;
import it.polimi.ingsw.view.GameView;

import java.io.IOException;

public class ClientController implements ClientSideMessageListener, UserListener {
    private String clientIP;
    private GameView gameView;
    private String clientName="";
    private ClientSocket serverConnection;
    private ClientControllerState currentState;

    public ClientController() {
        this.gameView = null;
        serverConnection =null;
        //Set clientIP
    }

    /**
     * After the player has chosen IP and port of the serer the connection is started
     */
    public void begin(){
        currentState =ClientControllerState.CONNECTING;
        serverConnection = new ClientSocket();
        serverConnection.start();
        sendMessage(new FindLobbyMessage());
    }

    /**
     * Sends a message to the server and handles any IOExceptions that may arise
     */
    private void sendMessage(ClientToServerMessage mes){
        try {
            serverConnection.send(mes);
        }catch (IOException e){
            System.err.println("Error while sending a message");
        }
    }
    @Override
    public void handle(ServerToClientMessage m) {

    }

    @Override
    public void handle(AvailablePositionsMessage m) {
        if(!gameSoftLocked()) {
            gameView.updateAvailablePositions(m.getPositions());
            gameView.printOwnerField();
        }else{
            gameView.showText("There are no more available positions");
        }
    }

    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        currentState = ClientControllerState.CHOOSING_NUMBER_OF_PLAYERS;
        gameView.showText("Choose the number of players that will play in this game");
    }
    @Override
    public void handle(ClientCantStartGameMessage m) {
        gameView.showText("You don't have permission to do this");
    }

    @Override
    public void handle(ClientFieldCheckValidityMessage m) {

    }

    @Override
    public void handle(EmptyDeckMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("You tried to draw from an empty deck. Change your choice.");
    }

    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("This card position is empty. Change your drawing choice");
    }

    @Override
    public void handle(EndPlayerTurnMessage m) {
        currentState=ClientControllerState.OTHER_PLAYER_TURN;
    }

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

    @Override
    public void handle(GameStartingMessage m) {
        currentState=ClientControllerState.CHOOSING_STARTING_CARD_FACE;
        gameView= new GameView(m.getPlayerHand(),m.getPlayersInfo(),clientName,m.getStartingCard());
        gameView.printCommonField();
        gameView.printHand();
        gameView.showText("Place your starting card.");
    }

    @Override
    public void handle(IllegalPlacementPositionMessage m) {
        currentState=ClientControllerState.REQUESTING_PLACEMENT;
        gameView.showText("This position is not available for placement!");
    }

    @Override
    public void handle(LobbyFoundMessage m) {
        currentState=ClientControllerState.CHOOSING_NAME;
        //TODO: using the user interaction class to get the name string.And temporarily set it as clientName
        gameView.showText("Connected successfully to a game. Now choose your name");
    }

    @Override
    public void handle(LobbyFullMessage m) {
        currentState=ClientControllerState.ENDING_CONNECTION;
        //TODO: decide how to display this message
    }

    @Override
    public void handle(NameChosenSuccessfullyMessage m) {
        currentState=ClientControllerState.WAITING_FOR_START;
        gameView.showText("Name chosen successfully!");
    }

    @Override
    public void handle(NameNotAvailableMessage m){
        currentState=ClientControllerState.CHOOSING_NAME;
        gameView.showText("Your name is already chosen, choose another name.");
    }

    @Override
    public void handle(NotEnoughResourcesMessage m) {
        currentState=ClientControllerState.REQUESTING_PLACEMENT;
        gameView.showText("You don't have enough resources to play this gold card!\n");
        gameView.printOwnerField();
    }

    @Override
    public void handle(OtherPlayerTurnUpdateMessage m) {
        if(!gameSoftLocked()) {
            currentState = ClientControllerState.OTHER_PLAYER_TURN;
        }
        String opponent=m.getPlayerName();
        PlayerPlacedCardInformation info= m.getPlacedCardInformation();
        gameView.updateOtherPlayerField(opponent, info.getCardId(), info.getXPos(), info.getYPos(), info.isFacingUp(), m.getVisibleSymbols());
        gameView.showText(opponent+" has made a move!\n");
        gameView.printOpponentField(opponent);
    }
    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {
        currentState=ClientControllerState.GAME_SOFT_LOCKED;
        gameView.showText("Your field has no more available corners! Your turn will be skipped");
    }

    @Override
    public void handle(SecretObjectiveChoiceMessage m) {
        currentState=ClientControllerState.CHOOSING_OBJECTIVE;
        gameView.secretObjectiveChoice(m.getFirstChoice(),m.getSecondChoice());
    }

    @Override
    public void handle(SendDrawncardMessage m) {

    }

    @Override
    public void handle(ServerCantStartGameMessage m) {
        gameView.showText("The game can't be started right now.");
    }

    @Override
    public void handle(SharedFieldUpdateMessage m) {
        gameView.updateDecks(m.getGoldBackside(),m.getResourceBackside(),m.getVisibleCards());
        gameView.printCommonField();
    }

    @Override
    public void handle(StartPlayerTurnMessage m) {
        if(!gameSoftLocked()) {
            currentState = ClientControllerState.REQUESTING_PLACEMENT;
            gameView.showText("It's your turn, now place a card!");

        }else{
            gameView.showText("Your field has no more available corners! Your turn will be skipped");
        }
        gameView.printOwnerField();
    }

    @Override
    public void handle(SuccessfulPlacementMessage m) {
        currentState=ClientControllerState.REQUESTING_DRAW_CARD;
        gameView.showText("Now draw a card!");
        gameView.printCommonField();
    }
    private boolean gameSoftLocked() {
        return ClientControllerState.GAME_SOFT_LOCKED==currentState;
    }
    @Override
    public void receiveCommand(UserCommand c) {

    }
}
