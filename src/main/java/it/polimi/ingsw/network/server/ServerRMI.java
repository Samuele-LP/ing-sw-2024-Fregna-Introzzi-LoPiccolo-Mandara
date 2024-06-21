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

/**
 * The ServerRMI class implements a Remote Method Invocation (RMI) server that handles multiple client connections,
 * manages communication with clients, and supports starting and stopping the server.
 */
public class ServerRMI extends UnicastRemoteObject implements ServerStub {

    /**
     * The name of the class, used for debugging purposes.
     */
    String className = ServerRMI.class.getName();

    /**
     * The list of ClientHandlerRmi objects, each managing a connection with a client.
     */
    private List<ClientHandlerRmi> handlers;

    /**
     * A flag indicating whether the game has started.
     */
    private boolean gameStarted = false;

    /**
     * The registry used to store remote objects for the connection.
     */
    private Registry registry;

    /**
     * The RMIServer object.
     */
    private static ServerRMI serverObject = null;

    /**
     * Creates the RMI server.
     *
     * @throws RemoteException if any error occurs in the remote object creation.
     */
    public ServerRMI() throws RemoteException {
        super();
        handlers = new ArrayList<>();
    }

    /**
     * Starts the RMI server by creating a registry and binding the server object to it.
     *
     * @param serverPort the port used to instantiate the connection with the server.
     * @throws RemoteException if an error occurs while creating the server socket or binding to the port.
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
     * Getter for the registry.
     *
     * @return the registry.
     * @throws RemoteException if any error occurs with the registry.
     */
    public Registry getRegistry() throws RemoteException {
        return registry;
    }

    /**
     * Runs the RMI server, waiting for clients to register.
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
     * Registers a new client to connect with the server.
     *
     * @param clientController the controller needed to pass information between client and server.
     * @param gameListener the listener needed to pass information between client and server.
     * @throws RemoteException if any error occurs with the remote object inside the registry.
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
     * Ends all connections and stops the server by calling endClientHandlers() and endServer() methods.
     */
    public void endAll() {
        endClientHandlers();
        endServer();
        System.out.println("Connections ended!");
    }

    /**
     * Stops the server connection with each ClientHandlerRmi.
     */
    private void endClientHandlers() {
        if (!handlers.isEmpty())
            for (ClientHandlerRmi handler : handlers) {
                handler.interruptSelf();
            }
    }

    /**
     * Stops the server by unexporting the registry and interrupting the current thread.
     *
     * @throws NoSuchObjectException if an error occurs while closing the RMI registry.
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
     * Sets the gameStarted flag to true. This method should be called when the game begins.
     */
    public void setGameStarted() {
        this.gameStarted = true;
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message the message to be sent to all clients.
     * @throws IOException if an I/O error occurs when sending the message.
     */
    public synchronized void sendToClients(ServerToClientMessage message) throws IOException {
        for (ClientHandlerRmi handler : handlers) {
            handler.sendMessage(message);
        }
    }
}

