package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class SocketServer implements ServerStub {

    /**
     * Debugging
     */
    String className = SocketServer.class.getName();

    /**
     * Socket of Server
     */
    private ServerSocket serverSocket;

    /**
     * Object used for input as a stream
     */
    private ObjectInputStream input;

    /**
     * Object used for output as a stream
     */
    private ObjectOutputStream output;

    /**
     * List of ClientHandler, one for each player
     */
    private List<ClientHandlerSocket> handlers;

    /**
     * Flag used to keep track of the fact that the game is already started
     */
    private boolean gameStarted = false;

    /**
     * Starts Server
     *
     * @param server_port port used to instantiate the socket with the server
     * @throws IOException if socket don't get created successfully
     */
    public void start(int server_port) {
        try{
            serverSocket = new ServerSocket(server_port);
            handlers = new ArrayList<>();
            System.out.println("Server Socket started!");
        } catch(IOException e){
            System.out.print("\n\n!!! ERROR !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed Server Socket begin\n\n");
        }
    }

    /**
     * Accept connections with clients
     */
    public void run(){ acceptConnections(); }

    /**
     * Try to accept connections with all clients
     *
     * @throws IOException if an error occurs in socket connection
     */
    private void acceptConnections(){
        try {
            while(!gameStarted) {
                System.out.println("Waiting for connections...");
                Socket connection = null;
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

    /**
     * Ends socket of the server
     *
     * @throws IOException if there is an error in the closure of the server socket
     */
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
        Thread.currentThread().interrupt();
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

