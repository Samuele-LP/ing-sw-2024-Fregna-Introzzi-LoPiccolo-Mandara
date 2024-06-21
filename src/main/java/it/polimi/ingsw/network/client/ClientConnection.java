package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.ClientToServerMessage;

import java.io.*;
import java.rmi.RemoteException;

/**
 * The ClientConnection class represents the connection between a client and a server.
 * It is an abstract class that defines methods for managing the connection, sending and receiving messages,
 * and checking the connection status.
 */
public abstract class ClientConnection {

    /**
     * Method to read incoming messages. This method runs indefinitely as a thread until the connection is closed.
     */
    public abstract void receiveMessages();

    /**
     * Runs indefinitely as a thread to pass messages onto the ClientController and handle them until the connection is closed.
     */
    public abstract void passMessages();

    /**
     * Starts the connection between the client and the server. If an error occurs during connection,
     * it tries again a pre-set number of times before giving up.
     */
    abstract void startConnection();

    /**
     * Ends the connection between the client and the server.
     */
    public abstract void stopConnection();

    /**
     * Sends a message to the server.
     *
     * @param mes the message to be sent to the server.
     * @throws IOException     if an I/O error occurs when sending the message.
     * @throws RemoteException if a remote error occurs when sending the message.
     */
    public abstract void send(ClientToServerMessage mes) throws IOException , RemoteException;

    /**
     * Sends a Ping message to the server at regular intervals.
     */
    public abstract void sendPing();

    /**
     * Notifies the connection when a Pong message is received.
     */
    public abstract void pongWasReceived();

    /**
     * Periodically checks if a Pong message has been received. If a Pong has not been received
     * within a specified timeout period, the connection is closed.
     */
    public abstract void checkConnectionStatus();
}
