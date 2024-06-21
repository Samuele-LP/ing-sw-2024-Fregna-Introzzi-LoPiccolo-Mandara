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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ClientSocket extends ClientConnection {

    /**
     * Debugging: name of this class
     */
    String className = ClientConnection.class.getName();

    /**
     * Flag used to check if connection is still alive
     */
    private boolean connectionActive;

    /**
     * Socket of the client
     */
    private Socket clientSocket;

    /**
     * Object Input Stream
     */
    private ObjectInputStream input;

    /**
     * Object Output Stream
     */
    private ObjectOutputStream output;

    /**
     * List of ServerToClientMessage
     */
    private final LinkedList<ServerToClientMessage> messageQueue = new LinkedList<>();

    /**
     * ClientSideMessageListener
     */
    private final ClientSideMessageListener listener;

    /**
     * Flag to check if pong was received successfully
     */
    private boolean receivedPong = false;

    /**
     * Lock
     */
    private final Object pongLock = new Object();

    /**
     * Creates the client Socket and starts a new connection
     *
     * @param listener is the listener who will receive the messages
     */
    public ClientSocket(ClientSideMessageListener listener) {
        this.listener = listener;
        connectionActive = true;
        startConnection();
    }

    /**
     * Method used to read incoming messages, runs indefinitely as a thread until the connection is closed.
     *
     * @throws IOException if an error occurs with the cast to ServerToClientMessage
     * @throws ClassNotFoundException if an error occurs while receiving an input from the server
     * @throws ClassCastException if is received an unsupported object type from the server
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
     * Runs indefinitely as a thread to pass messages onto the ClientController and handle them until the connection is closed.
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
     * Starts the connection between Client and Server. If an error occurs during connection it tries again
     * a pre-set number of times before giving up
     *
     * @throws UnknownHostException if the listener had some problem connecting
     * @throws IOException if an error occurred in the input/output creation
     * @throws InterruptedException if the connection got interrupted before the retry
     */
     public void startConnection(){
        boolean connectionEstablished = false;
        int connectionFailedAttempts = 0;

        do {
            try{
                clientSocket = new Socket(ConstantValues.serverIp, ConstantValues.socketPort);
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                connectionEstablished = true;
            } catch (UnknownHostException e) {
                listener.couldNotConnect();
                return;
            } catch(IOException e0){
                System.out.println("\n\n!!! Error !!! (" + className + " - "
                        + new Exception().getStackTrace()[0].getLineNumber() + ") during connection with Server!\n\n");

                //Wait secondsBeforeRetryReconnection seconds. It's been put in a try-catch due to possible errors
                // in the sleep method
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
     * Ends the connection between Client and Server
     *
     * @throws IOException if an error occurred while closing input/output
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
     * Sends a message to the server
     *
     * @throws IOException if ClientToServerMessage receive a non-existing type of message
     */
    public synchronized void send(ClientToServerMessage mes) throws IOException {
        output.writeObject(mes);
    }

    /**
     * Every half timeout period a Ping message is sent to the server
     *
     * @throws InterruptedException if the connection gets interrupted while waiting to send a ping
     * @throws IOException if the client gets disconnected while sending a ping
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
     * The listener who has been passed a pong will notify the connection
     */
    public void pongWasReceived(){
        synchronized (pongLock) {
            receivedPong = true;
        }
    }

    /**
     * Every timeout period checks if a Pong has been received.
     * If a Pong has not been received for enough time then the connection will be closed
     *
     * @throws InterruptedException if an error occurs while waiting for a pong
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