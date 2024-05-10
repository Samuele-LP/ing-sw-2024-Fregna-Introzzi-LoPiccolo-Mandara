package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.commonData.ConstantValues;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.clientToServer.Ping;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ClientSocket{

    /**
     * Debugging: name of this class
     */
    String className = ClientSocket.class.getName();

    private boolean connectionActive;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final LinkedList<ServerToClientMessage> messageQueue= new LinkedList<>();
    private final ClientSideMessageListener listener;


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
            } catch (IOException | ClassNotFoundException e){
                connectionActive = false;
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
    public void startConnection(String serverIP, int socketPort){
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
        new Thread(this::sendPing).start();
    }

    /**
     * Ends the connection between Client and Server
     * @throws IOException if an error has occurred while ending the connection
     */
    public synchronized void stopConnection(){
        try {
            connectionActive = false;
            input.close();
            output.close();
            clientSocket.close();
        }catch (IOException e){
            System.err.println("Error while terminating the connection");
            throw new RuntimeException();
        }
        System.out.println("Connection ended successfully!");
    }

    /**
     * Sends a message to the server
     */
    public synchronized void send(ClientToServerMessage mes) throws IOException {
        output.writeObject(mes);
    }

    /**
     * Sends a ping to the server every half socketTimeout period.
     */
    public synchronized void sendPing()  {
        while(!clientSocket.isClosed()){
            try {
                TimeUnit.SECONDS.sleep(ConstantValues.socketTimeout_seconds/2);
            } catch (InterruptedException e) {
                throw new RuntimeException("\nPing thread was interrupted\n");
            }
            try {
                output.writeObject(new Ping());
            } catch (IOException e) {
                System.err.println("\nError while sending a ping.\n");
                stopConnection();
            }
        }
    }
}
