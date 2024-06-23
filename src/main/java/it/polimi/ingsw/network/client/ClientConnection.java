package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.ClientToServerMessage;

import java.io.IOException;

/**
 * The ClientConnection interface represents the connection between a client and a server.
 * It is an interface that defines methods for managing the connection, sending and receiving messages,
 * and checking the connection status.
 */
public interface ClientConnection {

    /**
     * Method to read incoming messages. This method runs indefinitely as a thread until the connection is closed.
     */
    void receiveMessages();

    /**
     * Runs indefinitely as a thread to pass messages onto the ClientController and handle them until the connection is closed.
     */
    void passMessages();

    /**
     * Starts the connection between the client and the server. If an error occurs during connection,
     * it tries again a pre-set number of times before giving up.
     */
    void startConnection();

    /**
     * Ends the connection between the client and the server.
     */
    void stopConnection();

    /**
     * Sends a message to the server.
     *
     * @param mes the message to be sent to the server.
     * @throws IOException     if an I/O error occurs when sending the message.
     */
    void send(ClientToServerMessage mes) throws IOException;

    /**
     * Sends a Ping message to the server at regular intervals.
     */
    void sendPing();

    /**
     * Notifies the connection when a Pong message is received.
     */
    void pongWasReceived();

    /**
     * Periodically checks if a Pong message has been received. If a Pong has not been received
     * within a specified timeout period, the connection is closed.
     */
    void checkConnectionStatus();
}
