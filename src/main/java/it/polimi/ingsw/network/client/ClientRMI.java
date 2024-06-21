package it.polimi.ingsw.network.client;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.main.ClientMain;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * The ClientRMI class represents a client connection using RMI (Remote Method Invocation).
 * It manages the connection to the server, sends and receives messages, and checks the connection status.
 */
public class ClientRMI extends ClientConnection {

    /**
     * The name of this class, used for debugging purposes.
     */
    String className = ClientConnection.class.getName();

    /**
     * The UserListener to handle user commands.
     */
    private UserListener userListener;

    /**
     * The ClientController to handle client requests.
     */
    private ClientController requests;

    /**
     * The ClientSideMessageListener to handle messages received from the server.
     */
    private final ClientSideMessageListener listener;

    /**
     * Flag to check if the connection is still active.
     */
    private boolean connectionActive;

    /**
     * Queue for messages to be sent to the client.
     */
    private final LinkedList<ServerToClientMessage> messageQueue = new LinkedList<>();

    /**
     * Flag to keep track if a Pong was received successfully.
     */
    private boolean receivedPong = false;

    /**
     * Lock object used for synchronizing Pong operations.
     */
    private final Object pongLock = new Object();

    /**
     * The RMI registry used to make the connection possible.
     */
    private Registry registry;

    /**
     * Creates the client RMI and starts a new connection.
     *
     * @param listener the listener that will receive the messages.
     */
    public ClientRMI(ClientSideMessageListener listener) {
        this.listener = listener;
        connectionActive = true;
        startConnection();
    }

    /**
     * Method to read incoming messages. Runs indefinitely as a thread until the connection is closed.
     */
    @Override
    public void receiveMessages() {

    }

    /**
     * Runs indefinitely as a thread to pass messages to the ClientController and handle them until the connection is closed.
     */
    @Override
    public void passMessages(){
        while (connectionActive) {
            synchronized (messageQueue) {
                if (!messageQueue.isEmpty()) {
                    ServerToClientMessage message = messageQueue.pop();
                    message.execute(listener);
                }
            }
        }
    }

    /**
     * Starts the connection between the client and the server. If an error occurs during connection,
     * it tries again a pre-set number of times before giving up.
     */
    public void startConnection() {
        boolean connectionEstablished = false;
        int connectionFailedAttempts = 0;

        do {
            try {
                registry = LocateRegistry.getRegistry(ConstantValues.serverIp, ConstantValues.rmiPort);

                assert registry != null;

                System.out.print("\n1\n");

                requests = (ClientController) registry.lookup(ConstantValues.servername_RMI);

                System.out.print("\n2\n");

                userListener = (UserListener) UnicastRemoteObject.exportObject(listener, 0);

                System.out.print("\nConnection Established!\n");

                connectionEstablished = true;
            } catch (Exception e0) {
                System.out.println(e0.toString());

                System.out.println("\n\n!!! Error !!! (" + className + " - "
                        + new Exception().getStackTrace()[0].getLineNumber() + ") during connection with Server!\n\n");

                for(int i = 0; i < ConstantValues.secondsBeforeRetryReconnection ;)
                    try {
                        Thread.sleep(1000); // = 1 [s]
                        i++;
                    } catch(InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }

                if (connectionFailedAttempts >= ConstantValues.maxReconnectionAttempts) {
                    System.out.print("\n\n!!! Error !!! (" + className + " - "
                            + new Exception().getStackTrace()[0].getLineNumber() + ") connectionFailedAttempts exceeded!\n\n");
                    listener.couldNotConnect();
                    return;
                }

                connectionFailedAttempts++;
            }
        } while (!connectionEstablished);
    }

    /**
     * Ends the connection between the client and the server.
     */
    @Override
    public void stopConnection() {
        connectionActive = false;
        try {
            if (userListener != null) {
                UnicastRemoteObject.unexportObject((Remote) userListener, true);
            }
            if (registry != null) {
                UnicastRemoteObject.unexportObject((Remote) listener, true);
            }
        } catch (NoSuchObjectException e) {
            System.out.println("Error while unexporting the remote object: " + e.getMessage());
        } finally {
            listener.disconnectionHappened();
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param mes the message to be sent to the server.
     * @throws IOException     if an I/O error occurs when sending the message.
     * @throws RemoteException if a remote error occurs when sending the message.
     */
    @Override
    public synchronized void send(ClientToServerMessage mes) throws IOException, RemoteException {
        /*
        try {
            requests.handle(mes);
        } catch (RemoteException e) {
            System.err.println("RemoteException while sending message: " + e.getMessage());
            stopConnection();
        }
         */
    }

    /**
     * Sends a Ping message to the server at regular intervals.
     */
    @Override
    public void sendPing(){
        while (connectionActive) {
            try {
                for (int i = 0; i < ConstantValues.connectionTimeout_seconds / 2; i++) {
                    if (ClientMain.stop) {
                        return;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting to send a Ping");
                throw new RuntimeException(e);
            }
            if (!connectionActive) {
                return;
            }
            /*
            try {
                // TODO why the fuck it does not accept a generic one !?!?
                requests.handle(new Ping());
            } catch (RemoteException e) {
                System.err.println("Disconnection while sending a Ping, the connection will be closed");
                stopConnection();
            }
             */
        }
    }

    /**
     * Notifies the connection when a Pong message is received.
     */
    @Override
    public void pongWasReceived(){
        synchronized (pongLock) {
            receivedPong = true;
        }
    }

    /**
     * Periodically checks if a Pong message has been received. If a Pong has not been received
     * within a specified timeout period, the connection is closed.
     */
    @Override
    public void checkConnectionStatus(){
        while (connectionActive) {
            try {
                for (int i = 0; i < ConstantValues.connectionTimeout_seconds; i++) {
                    if (ClientMain.stop) {
                        return;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting for a Pong");
                throw new RuntimeException(e);
            }
            synchronized (pongLock) {
                if (!receivedPong) {
                    this.stopConnection();
                } else {
                    receivedPong = false;
                }
            }
        }
    }
}
