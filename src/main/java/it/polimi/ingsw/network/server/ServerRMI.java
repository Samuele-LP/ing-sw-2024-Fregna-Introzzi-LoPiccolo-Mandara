package it.polimi.ingsw.network.server;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.controller.ClientController;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerStub {

    /**
     * Debugging
     */
    String className = ServerRMI.class.getName();

    /**
     * List of ClientHandlers, one for each player
     */
    private List<ClientHandlerRmi> handlers;

    private boolean gameStarted = false;

    private Registry registry;

    /**
     * RMIServer object
     */
    private static ServerRMI serverObject = null;

    public ServerRMI() throws RemoteException {
        super();
        handlers = new ArrayList<>();
    }

    /**
     * Starts the RMI server
     *
     * @param serverPort
     */
    @Override
    public void start(int serverPort) throws RemoteException {
        try {
            serverObject = new ServerRMI();
            registry = LocateRegistry.createRegistry(ConstantValues.rmiPort);

            //System.setProperty("java.rmi.server.hostname", ConstantValues.getOwnIP());

            getRegistry().rebind(ConstantValues.servername_RMI, serverObject);
            System.out.println("RMI Server started!");
        } catch (RemoteException e) {
            System.out.print("\n\n!!! ERROR !!! (" + className + " - " + new Exception().getStackTrace()[0].getLineNumber() + ") Failed to start RMI server\n\n");
            throw e;
        }// catch (UnknownHostException e) {
         //   throw new RuntimeException(e);
         //}
    }

    public Registry getRegistry() throws RemoteException {
        return registry;
    }

    public void run() {
        System.out.println("Server is running and waiting for clients to register...");
        while (!gameStarted) {
            /*try {
                //registerClient();
            } catch (InterruptedException e) {
                System.out.println("Server interrupted: " + e.getMessage());
                e.printStackTrace();
            }*/
        }
    }

    /**
     * Registers a new client with the server
     */
    public void registerClient(ClientController clientController, ServerSideMessageListener gameListener) throws RemoteException {
        ClientHandlerRmi handler = new ClientHandlerRmi(clientController, gameListener);
        handlers.add(handler);
        registry.rebind("client" + handlers.size(), (Remote) clientController);
        System.out.println("Client registered: client" + handlers.size());

        new Thread(handler::receiveMessage).start();
        new Thread(handler::passMessage).start();
        new Thread(handler::checkConnectionStatus).start();
    }

    /**
     * Calls endClientHandlers() and endServer() in order to close everything
     */
    public void endAll() {
        endClientHandlers();
        endServer();
        System.out.println("Connections ended!");
    }

    /**
     * Stops Server connection with each ClientHandler
     */
    private void endClientHandlers() {
        if (!handlers.isEmpty())
            for (ClientHandlerRmi handler : handlers) {
                handler.interruptSelf();
            }
    }

    /**
     * Stops Server
     */
    private void endServer() {
        try {
            UnicastRemoteObject.unexportObject(registry, true);
            System.out.println("RMI registry closed!");
        } catch (NoSuchObjectException e) {
            System.out.println("Error while closing the RMI registry: " + e.getMessage());
        }
        Thread.currentThread().interrupt();
        System.out.println("Server ended!");
    }

    /**
     * Sets the flag gameStarted to true
     * This method has to be called when the game begins
     */
    public void setGameStarted() {
        this.gameStarted = true;
    }

    /**
     * Sends a message to all clients
     */
    public synchronized void sendToClients(ServerToClientMessage message) throws IOException {
        for (ClientHandlerRmi handler : handlers) {
            handler.sendMessage(message);
        }
    }
}

