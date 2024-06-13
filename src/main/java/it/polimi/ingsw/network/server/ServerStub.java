package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;

public interface ServerStub extends Runnable {

    void start(int serverPort) throws RemoteException;

    void run();

    void endAll();

}
