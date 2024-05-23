package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.ClientToServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientRMI extends ClientConnection {
    /**
     * Method used to read incoming messages, runs indefinitely as a thread until the connection is closed.
     */
    @Override
    public void receiveMessages() {

    }

    /**
     * Runs indefinitely as a thread to pass messages onto the ClientController and handle them until the connection is closed.
     */
    @Override
    public void passMessages() {

    }

    /**
     * Starts the connection between Client and Server. If an error occurs during connection it tries again
     * a pre-set number of times before giving up.
     *
     * @param serverIP
     * @param socketPort
     */
    @Override
    void startConnection(String serverIP, int socketPort) {

    }

    /**
     * Ends the connection between Client and Server
     */
    @Override
    public void stopConnection() {

    }

    /**
     * Sends a message to the server
     *
     * @param mes
     */
    @Override
    public void send(ClientToServerMessage mes) throws IOException, RemoteException {

    }

    /**
     * Every half timeout period a Ping message is sent to the server
     */
    @Override
    public void sendPing() {

    }

    /**
     * The listener who has been passed a pong will notify the connection
     */
    @Override
    public void pongWasReceived() {

    }

    /**
     * Every timeout period checks if a Pong has been received.
     * If a Pong has not been received for enough time then the connection will be closed
     */
    @Override
    public void checkConnectionStatus() {

    }
}
