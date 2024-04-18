package it.polimi.ingsw.network.socket.server;

import java.io.IOException;
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

    /**
     * List of ClientHandler, one for each player
     */
    private List<ClientHandler> handlers;

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
        try{
            while(!this.isInterrupted()){
                handlers.add(new ClientHandler(serverSocket.accept()));
                handlers.getLast().start();
                System.out.println("Connection accepted and Handler started!");
            }
        } catch(IOException e){
            System.out.print("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed accepting socket connection\n\n");
            System.err.print("(" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() +"): " + e);
        }

        try{
            serverSocket.close();
        } catch (IOException e){
            System.out.println("\n\n!!! Error !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed attempt to close serverSocket\n\n");
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls endClientHandlers() and endServer() in order to close end everything
     */
    public void endAll(){
        endClientHandlers();
        endServer();
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
}

