package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.List;
import java.util.ArrayList;

public class Server extends Thread{

    /**
     * Debugging
     */
    String className = Server.class.getName();

    /**
     * Socket of Server
     */
    private ServerSocket serverSocket;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * List of ClientHandler, one for each player
     */
    private List<ClientHandler> handlers;

    private boolean gameStarted = false;

    /**
     * Starts Server
     *
     * @param server_port
     * @throws IOException
     */
    public void start(int server_port) throws IOException{
        try{
            serverSocket = new ServerSocket(server_port);
            handlers = new ArrayList<>();
            System.out.println("Server Socket started!");
        } catch(IOException e){
            System.out.print("\n\n!!! ERROR !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed Server Socket begin\n\n");
        }
    }

    /**
     * Accepts connection and starts ClientHandler for each of them
     */
    public void run(){
        acceptConnections();
        new Thread(this::messageHandle).start();
    }

    /**
     * Try to accept connections with all clients
     */
    private void acceptConnections(){
        try {
            while(!gameStarted) {
                handlers.add(new ClientHandler(serverSocket.accept()));
                System.out.println("Connection accepted and Handler started!");
            }
        } catch (IOException e) {
            System.out.print("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed accepting socket connection\n\n");
            System.err.print("(" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() +"): " + e);
        }
    }

    private void messageHandle(){
        while(!this.isInterrupted()){
            handlers.getLast().receiveMessage();
            handlers.getLast().passMessage();
            handlers.getLast().start();
        }
    }

    /**
     * Calls endClientHandlers() and endServer() in order to close end everything
     */
    public void endAll(){
        endClientHandlers();
        endServerSocket();
        endServer();
    }

    private void endServerSocket(){
        try{
            serverSocket.close();
        } catch (IOException e){
            System.out.println("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed attempt to close serverSocket\n\n");
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops Server Socket connection with each ClientHandler
     */
    private void endClientHandlers(){
        if(!handlers.isEmpty())
            for (ClientHandler i : handlers) i.interruptSelf();
    }

    /**
     * Stops Server
     */
    private void endServer(){
        this.interrupt();
    }

    /**
     * Sets the flag gameStarted to true
     * This method has to be called when the game begins
     */
    public void setGameStarted(){
        this.gameStarted = true;
    }

    /**
     * Sends a message to the client
     */
    public void sendToClient(ServerToClientMessage messaggio) throws IOException {
        output.writeObject(messaggio);
    }
}

