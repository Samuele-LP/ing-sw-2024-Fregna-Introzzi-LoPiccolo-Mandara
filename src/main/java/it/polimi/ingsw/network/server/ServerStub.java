package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;

public interface ServerStub extends Runnable {

    /**
     * Starts Server
     *
     * @param serverPort port used to instantiate the socket with the server
     * @throws RemoteException if socket don't get created successfully
     */
    void start(int serverPort) throws RemoteException;

    /**
     * Accept connections with clients
     */
    void run();

    /**
     * Calls endClientHandlers() and endServer() in order to close end everything
     */
    void endAll();

}
