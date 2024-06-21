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
     * The name of the class, used for debugging purposes.
     */
    String className = SocketServer.class.getName();

    /**
     * The server socket used to listen for incoming client connections.
     */
    private ServerSocket serverSocket;

    /**
     * The input stream for receiving data from the client.
     */
    private ObjectInputStream input;

    /**
     * The output stream for sending data to the client.
     */
    private ObjectOutputStream output;

    /**
     * The list of ClientHandlerSocket objects, each managing a connection with a client.
     */
    private List<ClientHandlerSocket> handlers;

    /**
     * A flag indicating whether the game has started.
     */
    private boolean gameStarted = false;

    /**
     * Starts the server by creating a server socket bound to the specified port.
     *
     * @param server_port the port on which the server listens for incoming connections.
     * @throws IOException if an I/O error occurs when opening the socket.
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
     * Starts accepting connections from clients.
     */
    public void run(){ acceptConnections(); }

    /**
     * Continuously accepts connections from clients until the game starts.
     * For each new connection, a new ClientHandlerSocket is created and three threads
     * are started to handle message receiving, message passing, and connection status checking.
     *
     * @throws IOException if an error occurs while accepting a socket connection.
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
     * Ends all connections and stops the server by calling endClientHandlers(),
     * endServerSocket(), and endServer() methods.
     */
    public void endAll(){
        endClientHandlers();
        endServerSocket();
        endServer();
        System.out.println("Connections ended!");
    }

    /**
     * Closes the server socket.
     *
     * @throws IOException if an I/O error occurs when closing the socket.
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
     * Stops each ClientHandlerSocket, closing their connections.
     */
    private void endClientHandlers(){
        if(!handlers.isEmpty())
            for (ClientHandlerSocket i : handlers) i.interruptSelf();
    }

    /**
     * Stops the server by interrupting the current thread.
     */
    private void endServer(){
        Thread.currentThread().interrupt();
        System.out.println("Server ended!");
    }

    /**
     * Sets the gameStarted flag to true.
     * This method should be called when the game begins.
     */
    public void setGameStarted(){
        this.gameStarted = true;
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent to the client.
     * @throws IOException if an I/O error occurs when sending the message.
     */
    public synchronized void sendToClient(ServerToClientMessage message) throws IOException {
        output.writeObject(message);
    }
}

