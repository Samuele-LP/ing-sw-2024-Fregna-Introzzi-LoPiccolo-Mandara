package it.polimi.ingsw.network.client;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * The ClientSocket class manages the client's connection to the server using sockets.
 * It handles sending and receiving messages, maintaining the connection, and checking the connection status.
 */
public class ClientSocket implements ClientConnection {

    /**
     * The name of this class, used for debugging purposes.
     */
    private final String className = ClientConnection.class.getName();

    /**
     * Flag used to check if the connection is still active.
     */
    private boolean connectionActive;

    /**
     * The client's socket.
     */
    private Socket clientSocket;

    /**
     * The input stream for receiving data from the server.
     */
    private ObjectInputStream input;

    /**
     * The output stream for sending data to the server.
     */
    private ObjectOutputStream output;

    /**
     * Queue for messages to be processed.
     */
    private final LinkedList<ServerToClientMessage> messageQueue = new LinkedList<>();

    /**
     * The listener for handling messages received from the server.
     */
    private final ClientSideMessageListener listener;

    /**
     * Flag to check if a Pong message was received successfully.
     */
    private boolean receivedPong = false;

    /**
     * Lock object used for synchronizing Pong operations.
     */
    private final Object pongLock = new Object();

    /**
     * Creates the client socket and starts a new connection.
     *
     * @param listener the listener that will receive the messages.
     */
    public ClientSocket(ClientSideMessageListener listener) {
        this.listener = listener;
        connectionActive = true;
        startConnection();
    }

    /**
     * Method to read incoming messages. Runs indefinitely as a thread until the connection is closed.
     */
    public void receiveMessages(){
        while(connectionActive){
            try{
                ServerToClientMessage message = (ServerToClientMessage) input.readObject();
                synchronized (messageQueue) {
                    messageQueue.add(message);
                }
            } catch (IOException  e) {
                stopConnection();
            } catch (ClassNotFoundException e1) {
                System.err.println("Error while receiving an input from the server");
            } catch (ClassCastException e2) {
                System.err.println("Received an unsupported object type from the server");
            }
        }
    }

    /**
     * Runs indefinitely as a thread to pass messages to the ClientController and handle them until the connection is closed.
     */
    public void passMessages(){
        while (connectionActive) {
            synchronized (messageQueue) {
                if (!messageQueue.isEmpty()) {
                    ServerToClientMessage message = messageQueue.pop();
                    message.execute(listener);
                }
            }
        }
    }

    /**
     * Starts the connection between the client and the server. If an error occurs during connection,
     * it tries again a pre-set number of times before giving up.
     *
     */
     public void startConnection(){
        boolean connectionEstablished = false;
        int connectionFailedAttempts = 0;

        do {
            try{
                clientSocket = new Socket(ConstantValues.getServerIp(), ConstantValues.socketPort);
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                connectionEstablished = true;
            } catch (UnknownHostException e) {
                listener.couldNotConnect();
                return;
            } catch(IOException e0){
                System.out.println("\n\n!!! Error !!! (" + className + " - "
                        + new Exception().getStackTrace()[0].getLineNumber() + ") during connection with Server!\n\n");
                try {
                    Thread.sleep(1000*ConstantValues.secondsBeforeRetryReconnection); // = 1 [s]
                } catch(InterruptedException e1) {
                    throw new RuntimeException(e1);
                }

                if(connectionFailedAttempts >= ConstantValues.maxReconnectionAttempts) {
                    System.out.print("\n\n!!! Error !!! (" + className + " - "
                            + new Exception().getStackTrace()[0].getLineNumber() + ") connectionFailedAttempts exceeded!\n\n");
                    listener.couldNotConnect();
                    return;
                }
                connectionFailedAttempts++;
            }
        } while (!connectionEstablished);
    }

    /**
     * Ends the connection between the client and the server.
     */
    public void stopConnection(){
        connectionActive = false;
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            System.out.println("\nClosing socket streams\n");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("\nSocket connection was closed\n");
            }
        }
        listener.disconnectionHappened();
    }

    /**
     * Sends a message to the server.
     *
     * @param mes the message to be sent to the server.
     * @throws IOException if there is an error sending the message.
     */
    public synchronized void send(ClientToServerMessage mes) throws IOException {
        output.writeObject(mes);
    }

    /**
     * Sends a Ping message to the server at regular intervals.
     */
    public void sendPing(){
        while(connectionActive){
            try {
                for(int i=0;i<ConstantValues.connectionTimeout_seconds/2;i++){
                    if(ClientMain.stop){
                        return;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting to send a Ping");
                throw new RuntimeException(e);
            }
            if(!connectionActive){
                return;
            }
            synchronized (this){
                try {
                    output.writeObject(new Ping());
                } catch (IOException e) {
                    System.err.println("Disconnection while sending a Ping, the connection will be closed");
                    stopConnection();
                }
            }
        }
    }

    /**
     * Notifies the connection when a Pong message is received.
     */
    public void pongWasReceived(){
        synchronized (pongLock) {
            receivedPong = true;
        }
    }

    /**
     * Periodically checks if a Pong message has been received. If a Pong has not been received
     * within a specified timeout period, the connection is closed.
     */
    public void checkConnectionStatus(){
        while(connectionActive){
            try {
                for(int i = 0; i < ConstantValues.connectionTimeout_seconds; i++){
                    if (ClientMain.stop) {
                        return;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting for a Pong");
                throw new RuntimeException(e);
            }

            synchronized (pongLock) {
                if (!receivedPong) {
                    this.stopConnection();
                } else {
                    receivedPong = false;
                }
            }
        }
    }

}