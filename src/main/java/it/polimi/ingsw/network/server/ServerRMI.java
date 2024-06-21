package it.polimi.ingsw.network.server;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.controller.ClientController;

import java.io.IOException;
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

    /**
     * Flag used to keep track of the fact that the game is already started
     */
    private boolean gameStarted = false;

    /**
     * Registry used to store remote objects for the connection
     */
    private Registry registry;

    /**
     * RMIServer object
     */
    private static ServerRMI serverObject = null;

    /**
     * Creates the RMI Server
     *
     * @throws RemoteException if any error occurs in the remote object creation
     */
    public ServerRMI() throws RemoteException {
        super();
        handlers = new ArrayList<>();
    }

    /**
     * Starts the RMI server
     *
     * @param serverPort port used to instantiate the connection with the server
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

    /**
     * Getter of registry
     *
     * @return registry
     * @throws RemoteException if any error occurs with the registry
     */
    public Registry getRegistry() throws RemoteException {
        return registry;
    }

    /**
     * Runs the RMI Server
     */
    public void run() {
        System.out.println("Server is running and waiting for clients to register...");
        while (!gameStarted) {
            /*try {
                registerClient();
            } catch (InterruptedException e) {
                System.out.println("Server interrupted: " + e.getMessage());
                e.printStackTrace();
            }*/
        }
    }

    /**
     * Registers a new client to connect with the server
     *
     * @param clientController needed to pass information between client and server
     * @param gameListener needed to pass information between client and server
     * @throws RemoteException if any error occurs with the remote object inside the registry
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
     *
     * @throws NoSuchObjectException if registry has something wrong inside
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

