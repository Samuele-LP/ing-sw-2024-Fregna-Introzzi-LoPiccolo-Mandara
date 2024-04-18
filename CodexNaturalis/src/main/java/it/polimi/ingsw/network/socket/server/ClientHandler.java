package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        Thread tmp_thread = new Thread(this);
        tmp_thread.start();

        try{
            Message message;
            while(!this.isInterrupted()){
                message = (Message) in.readObject();
                //
                //  COME GESTIRE UN MESSAGGIO GENERITO D'ORA IN POI ?!?!?!
                //
            }
        } catch(IOException | ClassNotFoundException e){
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Failed for some reasons!\n\n");
        }
    }

    public void start(){
        //
    }

    /**
     * Interrupt the thread
     */
    public void interruptSelf(){
        this.interrupt();
    }
}