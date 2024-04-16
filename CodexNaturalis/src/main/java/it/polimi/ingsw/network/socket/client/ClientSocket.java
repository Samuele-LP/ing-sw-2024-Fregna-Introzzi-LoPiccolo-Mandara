package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.model.DefaultValues;
import it.polimi.ingsw.network.messages.Message;

import java.io.*;
import java.net.*;

public class ClientSocket {

    public Socket clientSocket;

    public ObjectInputStream input;
    public ObjectOutputStream output;

    public ClientSocket() {
        startConnection(DefaultValues.serverIp, DefaultValues.socketPort);
    }

    /**
     * Method used to read incoming messages and do what they want
     * Note: "run" is a commonly used name. Confusion may happen. Maybe rename it ?
     */
    public void run(){
        while(true){
            try{
                Message message = (Message) input.readObject();
            } catch (IOException | ClassNotFoundException e){ // AGGIUNGERE ALTRE ECCEZIONI !?
                // NB: since there are multiple catch, "e" is final (imposed by Java)

                System.out.println("Error with connection!");
                System.exit(-1);
            }
        }
    }

    /**
     * Starts the connection between Client and Server
     *
     * @param serverIP
     * @param socketPort
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
                System.out.println("\n\nError during connection with Server!\n\n");


                //Wait secondsBeforeRetryReconnection seconds. It's been put in a try-catch due to possible errors
                // in the sleep method
                for(int i = 0; i <DefaultValues.secondsBeforeRetryReconnection ;)
                    try {
                        Thread.sleep(1000); // = 1 [s]
                        i++;
                    } catch(InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }

                if(connectionFailedAttempts >= DefaultValues.maxReconnectionAttempts) {
                    System.out.println("/n/nconnectionFailedAttempts exceeded!");
                    System.exit(-1);
                }

                connectionFailedAttempts++;
            }
        } while (!connectionEstablished);
    }

    /**
     * Ends the connection between Client and Server
     *
     * @throws IOException
     */
    public void stopConnection() throws IOException{
        input.close();
        output.close();
        clientSocket.close();
        System.out.println("Connection ended successfully!");
    }


}
