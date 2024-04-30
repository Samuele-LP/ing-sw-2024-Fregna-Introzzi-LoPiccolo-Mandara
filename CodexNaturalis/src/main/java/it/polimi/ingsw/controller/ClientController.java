package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.userCommands.UserCommand;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
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

    public ClientController() {
        this.gameView = null;
        serverConnection =null;
        //Set clientIP
    }

    /**
     * After the player has chosen IP and port of the serer he connection is started
     */
    public void begin(){
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

    }

    @Override
    public void handle(ChooseHowManyPlayersMessage m) {
        //TODO interaction with the user to get the necessary number between 2 and 4
        sendMessage(new NumberOfPlayersMessage(0));
    }

    @Override
    public void handle(ClientCantStartGameMessage m) {
        System.out.println("You don't have the permission to do this!");
    }

    @Override
    public void handle(ClientFieldCheckValidityMessage m) {

    }

    @Override
    public void handle(EmptyDeckMessage m) {

    }

    @Override
    public void handle(EmptyDrawnCardPositionMessage m) {

    }

    @Override
    public void handle(EndPlayerTurnMessage m) {

    }

    @Override
    public void handle(GameAlreadyStartedMessage m) {
        System.out.println("This game has already started, you can't participate!");
        System.out.println("You will be disconnected from the server.");
        try {
            serverConnection.stopConnection();
        }catch (IOException e){
            System.err.println("IOException while terminating the connection.");
        }
    }

    @Override
    public void handle(GameEndingAfterDisconnectionMessage m) {

    }

    @Override
    public void handle(GameEndingMessage m) {

    }

    @Override
    public void handle(GameStartingMessage m) {
        gameView= new GameView(m.getPlayerHand(),m.getPlayersInfo(),clientName,m.getStartingCard());
        gameView.printCommonField();
    }

    @Override
    public void handle(IllegalPlacementPositionMessage m) {
        //TODO change the command name to what has been chosen to be the relative command
        System.out.println("Invalid placement position. Try asking where you could place a card with the TEMPORARY_NAME command.");
    }

    @Override
    public void handle(LobbyFoundMessage m) {
        //TODO: using the user interaction class to get the name string.And temporarily set it as clientName
    }

    @Override
    public void handle(LobbyFullMessage m) {
        //TODO: decide how to display this message
    }

    @Override
    public void handle(NameChosenSuccessfullyMessage m) {

    }

    @Override
    public void handle(NameNotAvailableMessage m){
        //TODO: using the user interaction class to get the name string. And temporarily set it as clientName
    }

    @Override
    public void handle(NotEnoughResourcesMessage m) {

    }

    @Override
    public void handle(OtherPlayerTurnUpdateMessage m) {

    }

    @Override
    public void handle(PlayerCantPlayAnymoreMessage m) {

    }

    @Override
    public void handle(SecretObjectiveChoiceMessage m) {

    }

    @Override
    public void handle(SendDrawncardMessage m) {

    }

    @Override
    public void handle(ServerCantStartGameMessage m) {

    }

    @Override
    public void handle(SharedFieldUpdateMessage m) {

    }

    @Override
    public void handle(StartPlayerTurnMessage m) {

    }

    @Override
    public void handle(SuccessfulPlacementMessage m) {

    }

    @Override
    public void receiveCommand(UserCommand c) {

    }
}
