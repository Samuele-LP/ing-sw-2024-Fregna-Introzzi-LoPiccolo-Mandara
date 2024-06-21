package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;

/**
 * The ServerStub interface defines the methods that a server must implement
 * to manage client connections, start and stop the server, and handle incoming connections.
 * This interface extends the Runnable interface, allowing server implementations
 * to be executed by a thread.
 */
public interface ServerStub extends Runnable {

    /**
     * Starts the server by initializing necessary resources and binding to the specified port.
     *
     * @param serverPort the port on which the server listens for incoming connections.
     * @throws RemoteException if an error occurs while creating the server socket or binding to the port.
     */
    void start(int serverPort) throws RemoteException;

    /**
     * Continuously accepts connections from clients. This method is invoked when the server
     * is run in a separate thread.
     */
    void run();

    /**
     * Stops the server and closes all client connections by calling methods to terminate
     * client handlers and the server itself.
     */
    void endAll();

}
