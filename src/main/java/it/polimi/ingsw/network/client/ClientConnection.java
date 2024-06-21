package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public abstract class ClientConnection {

    /**
     * Method used to read incoming messages, runs indefinitely as a thread until the connection is closed.
     */
    public abstract void receiveMessages();

    /**
     * Runs indefinitely as a thread to pass messages onto the ClientController and handle them until the connection is closed.
     */
    public abstract void passMessages();

    /**
     * Starts the connection between Client and Server. If an error occurs during connection it tries again
     * a pre-set number of times before giving up.
     */
    abstract void startConnection();

    /**
     * Ends the connection between Client and Server
     */
    public abstract void stopConnection();

    /**
     * Sends a message to the server
     */
    public abstract void send(ClientToServerMessage mes) throws IOException , RemoteException;

    /**
     * Every half timeout period a Ping message is sent to the server
     */
    public abstract void sendPing();

    /**
     * The listener who has been passed a pong will notify the connection
     */
    public abstract void pongWasReceived();

    /**
     * Every timeout period checks if a Pong has been received.
     * If a Pong has not been received for enough time then the connection will be closed
     */
    public abstract void checkConnectionStatus();
}
