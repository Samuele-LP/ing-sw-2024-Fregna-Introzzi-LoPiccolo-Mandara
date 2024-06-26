package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer{

    /**
     * The name of the class, used for debugging purposes.
     */
    private final String className = SocketServer.class.getName();

    /**
     * The server socket used to listen for incoming client connections.
     */
    private ServerSocket serverSocket;
    /**
     * The list of ClientHandlerSocket objects, each managing a connection with a client.
     */
    private List<ClientHandlerSocket> handlers;
    /**
     * Starts the server by creating a server socket bound to the specified port.
     *
     * @param server_port the port on which the server listens for incoming connections.
     */
    public void start(int server_port) {
        try{
            serverSocket = new ServerSocket(server_port);
            handlers = new ArrayList<>();
            System.out.println("Server Socket started!");
        } catch(IOException e){
            System.out.print("\n\n!!! ERROR !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed Server Socket begin\n\n");
        }
        this.acceptConnections();
    }
    /**
     * Continuously accepts connections from clients until the game starts.
     * For each new connection, a new ClientHandlerSocket is created and three threads
     * are started to handle message receiving, message passing, and connection status checking.
     */
    private void acceptConnections(){
        try {
            while(!serverSocket.isClosed()) {
                System.out.println("Waiting for connections...");
                Socket connection;
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
}

