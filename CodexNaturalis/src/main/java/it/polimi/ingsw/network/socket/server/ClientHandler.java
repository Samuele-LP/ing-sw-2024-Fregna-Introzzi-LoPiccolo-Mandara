package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.commonData.ConstantValues;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientHandler extends Thread {

    /**
     * Debugging
     */
    String className = ClientHandler.class.getName();

    /**
     * GameController of the related game
     */
    private GameController gameController;

    /**
     * ObjectInputStream in
     */
    private ObjectInputStream in;

    /**
     * ObjectOutputStream out
     */
    private ObjectOutputStream out;

    /**
     * AudiencesHandlerServerSide of the related Game
     */
    private AudiencesHandlerServerSide audiencesHandlerServerSide;

    /**
     * Client Socket, unchangeable
     */
    private final Socket clientSocket;

    /**
     * Creare a queue for messages that can take up to maxMessagesInQueue messages.
     * NB: not more than maxMessagesInQueue in order to limit the spam ability of clients
     */
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(maxMessagesInQueue);

    /**
     * Used to handle Client needs
     *
     * @param clientSocket
     * @throws IOException
     */
    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        AudiencesHandlerServerSide audiencesHandlerServerSide = new AudiencesHandlerServerSide(out);
    }

    /**
     * Manages messages by client to server
     */
    public void run(){
        Thread tmp_thread = new Thread(this::executeMessage);
        tmp_thread.start();

        try{
            Message message;
            while(!this.isInterrupted()){
                addMessageToQueue();
            }
        } catch(IOException | ClassNotFoundException e){
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Failed for some reasons!\n\n");
        } catch(IllegalStateException){
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Client is spamming messages!\n\n");
        }finally{
            tmp_thread.interrupt();
        }
    }

    public void executeMessage(){
        try{
            Message message;
            while(!this.isInterrupted()){
                message = queue.take();

                //
                // HOW TO CALL GAMECONTROLLER WITH A GENERIC MESSAGE !?!?!
                //

            }
        }
    }

    /**
     * Add a message to queue of messages
     */
    private void addMessageToQueue(){
        message = (Message) in.readObject();
        queue.add(message);
    }

    /**
     * Interrupt the thread
     */
    public void interruptSelf(){
        this.interrupt();
    }

    /**
     * Method used for debugging. It prints the number of elements that are inside the queue
     */
    private void printQueueNumberOfElements(){
        System.out.println("Elements in queue: " + queue.size());
    }
}