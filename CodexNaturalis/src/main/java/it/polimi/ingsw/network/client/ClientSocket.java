package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ClientSocket extends ClientConnection {

    /**
     * Debugging: name of this class
     */
    String className = ClientConnection.class.getName();

    private boolean connectionActive;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final LinkedList<ServerToClientMessage> messageQueue= new LinkedList<>();
    private final ClientSideMessageListener listener;
    private boolean receivedPong=false;
    private final Object pongLock=new Object();

    /**
     * Creates the client Socket and starts a new connection
     * @param listener is the listener who will receive the messages
     */
    public ClientSocket(ClientSideMessageListener listener) {
        this.listener = listener;
        connectionActive = true;
        startConnection(ConstantValues.serverIp,ConstantValues.socketPort);
    }

    /**
     * Method used to read incoming messages, runs indefinitely as a thread until the connection is closed.
     */
    public void receiveMessages(){
        while(connectionActive){
            try{
                ServerToClientMessage message = (ServerToClientMessage) input.readObject();
                synchronized (messageQueue) {
                    messageQueue.add(message);
                }
            } catch (IOException  e){
                stopConnection();
                listener.disconnectionHappened();
            }catch (ClassNotFoundException e1){
                System.err.println("Error while receiving an input from the server");
            }catch (ClassCastException e2){
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
     * a pre-set number of times before giving up.
     */
     void startConnection(String serverIP, int socketPort){
        boolean connectionEstablished = false;
        int connectionFailedAttempts = 0;

        do {
            try{
                clientSocket = new Socket(serverIP, socketPort);
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                connectionEstablished = true;
            } catch(IOException e0){
                System.out.println("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") during connection with Server!\n\n");

                //Wait secondsBeforeRetryReconnection seconds. It's been put in a try-catch due to possible errors
                // in the sleep method
                for(int i = 0; i < ConstantValues.secondsBeforeRetryReconnection ;)
                    try {
                        Thread.sleep(1000); // = 1 [s]
                        i++;
                    } catch(InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }

                if(connectionFailedAttempts >= ConstantValues.maxReconnectionAttempts) {
                    System.out.print("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") connectionFailedAttempts exceeded!");
                    System.exit(-1);
                }

                connectionFailedAttempts++;
            }
        } while (!connectionEstablished);
    }

    /**
     * Ends the connection between Client and Server
     */
    public void stopConnection(){
        connectionActive = false;
        try {
            input.close();
            output.close();
            clientSocket.close();
        }catch (IOException e){
            System.err.println("Error while terminating the Socket connection");
        }
    }

    /**
     * Sends a message to the server
     */
    public synchronized void send(ClientToServerMessage mes) throws IOException {
        output.writeObject(mes);
    }

    /**
     * Every half timeout period a Ping message is sent to the server
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
                    System.err.println("IOException while sending a Ping, the connection will be closed");
                    stopConnection();
                    listener.disconnectionHappened();
                }
            }
        }
    }

    /**
     * The listener who has been passed a pong will notify the connection
     */
    public void pongWasReceived(){
        synchronized (pongLock){
            receivedPong=true;
        }
    }
    /**
     * Every timeout period checks if a Pong has been received.
     * If a Pong has not been received for enough time then the connection will be closed
     */
    public void checkConnectionStatus(){
        while(connectionActive){
            try {
                for(int i=0;i<ConstantValues.connectionTimeout_seconds;i++){
                    if(ClientMain.stop){
                        return;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting for a Pong");
                throw new RuntimeException(e);
            }
            synchronized (pongLock){
                if(!receivedPong){
                    this.stopConnection();
                    listener.disconnectionHappened();
                }else{
                    receivedPong=false;
                }
            }
        }
    }

}
