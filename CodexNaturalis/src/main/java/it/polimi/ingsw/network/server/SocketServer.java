package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class SocketServer extends Thread{

    /**
     * Debugging
     */
    String className = SocketServer.class.getName();

    /**
     * Socket of Server
     */
    private ServerSocket serverSocket;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * List of ClientHandler, one for each player
     */
    private List<ClientHandlerSocket> handlers;

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

    public void run(){
        acceptConnections();
    }

    /**
     * Try to accept connections with all clients
     */
    private void acceptConnections(){
        try {
            while(!gameStarted) {
                System.out.println("Waiting for connections...");
                Socket connection=null;
                connection = serverSocket.accept();
                handlers.add(new ClientHandlerSocket(connection));
                new Thread (()-> handlers.getLast().receiveMessage()).start();
                new Thread (()-> handlers.getLast().passMessage()).start();
                new Thread (()-> handlers.getLast().checkConnectionStatus()).start();
            }
        } catch (IOException e) {
            System.out.print("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed accepting socket connection\n\n");
            System.err.print("(" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() +"): " + e);
        }
    }

    /**
     * Calls endClientHandlers() and endServer() in order to close end everything
     */
    public void endAll(){
        endClientHandlers();
        endServerSocket();
        endServer();
        System.out.println("Connections ended!");
    }

    private void endServerSocket(){
        try{
            serverSocket.close();
            System.out.println("Server Socket closed!");
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
            for (ClientHandlerSocket i : handlers) i.interruptSelf();
    }

    /**
     * Stops Server
     */
    private void endServer(){
        this.interrupt();
        System.out.println("Server ended!");
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
    public synchronized void sendToClient(ServerToClientMessage message) throws IOException {
        output.writeObject(message);
    }
}

