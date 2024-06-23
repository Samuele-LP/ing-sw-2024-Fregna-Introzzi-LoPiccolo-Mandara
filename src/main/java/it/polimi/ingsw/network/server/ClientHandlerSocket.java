package it.polimi.ingsw.network.server;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.Pong;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.serverToClient.GenericMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.ConstantValues.maxMessagesInQueue;

/**
 * The ClientHandlerSocket class manages the interaction between the server and a single client
 * over a socket connection. It handles receiving, processing, and sending messages to the client.
 */
public class ClientHandlerSocket implements ClientHandler {

    /**
     * The name of this class, used for debugging purposes.
     */
    String className = ClientHandlerSocket.class.getName();

    /**
     * The ServerSideMessageListener of the related game.
     */
    private final ServerSideMessageListener serverSideMessageListener = GameController.getInstance();

    /**
     * The ObjectInputStream for receiving data from the client.
     */
    private final ObjectInputStream in;

    /**
     * The ObjectOutputStream for sending data to the client.
     */
    private final ObjectOutputStream out;

    /**
     * The client's socket, unchangeable.
     */
    private final Socket clientSocket;

    /**
     * Flag to indicate if a ping was received.
     */
    private boolean receivedPing = false;

    /**
     * Lock object used for synchronizing ping operations.
     */
    private final Object pingLock = new Object();

    /**
     * Queue for messages, with a capacity of maxMessagesInQueue, to limit client spam.
     */
    private final ArrayBlockingQueue<ClientToServerMessage> queue = new ArrayBlockingQueue<>(maxMessagesInQueue);

    /**
     * Constructs a ClientHandlerSocket to handle client interactions over a socket connection.
     *
     * @param clientSocket the socket connected to the client.
     * @throws IOException if an I/O error occurs when creating the input or output streams.
     */
    public ClientHandlerSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Receives messages from the client and adds them to the queue.
     */
    public void receiveMessage() {
        try {
            ClientToServerMessage message;
            while (!clientSocket.isClosed()) {
                message = (ClientToServerMessage) in.readObject();
                queue.add(message);
            }
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Failed for some reasons!\n\n");
        } catch (IllegalStateException illegalStateException) {
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Client is spamming messages!\n\n");
        } catch (IOException e) {
            System.out.print("\n\n!!! Disconnection detected !!! IOException while receiving messages!\n\n");
            serverSideMessageListener.disconnectionHappened(this);
        } catch (ClassCastException castException) {
            System.out.println("\n\nA client sent an invalid object!\n\n");
            synchronized (this) {
                try {
                    out.writeObject(new GenericMessage("An invalid object was received by the server"));
                } catch (IOException ioException) {
                    System.err.println("\n\nError while sending a generic response to the client\n\n");
                    serverSideMessageListener.disconnectionHappened(this);
                }
            }
        }
    }

    /**
     * Processes messages from the queue and executes them using the server-side message listener.
     */
    public void passMessage() {
        try {
            ClientToServerMessage message;
            while (!clientSocket.isClosed()) {
                //NB: ".take()" with an ArrayBlockingQueue does the following:
                //Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
                try {
                    message = queue.take();
                    message.execute(this.serverSideMessageListener, this);
                }catch (ClassCastException e){
                    throw new RuntimeException();
                }
            }
        } catch (InterruptedException e) {
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Unable to pass message to Server!\n\n");
            throw new RuntimeException(e);
        }
    }
    /**
     * Sends a message to the client.
     *
     * @param mes the message generated by the controller.
     * @throws IOException if an I/O error occurs when sending the message.
     */
    public synchronized void sendMessage(ServerToClientMessage mes)  throws IOException{
            out.writeObject(mes);
    }

    /**
     * Notifies the client handler of a received ping and sends a Pong response.
     */
    public void pingWasReceived() {
        synchronized (pingLock) {
            receivedPing = true;
        }
        synchronized (this) {
            try {
                out.writeObject(new Pong());
            } catch (IOException e) {
                System.err.println("\n\nAn error has occurred while sending a Pong response\n\n");
            }
        }
    }

    /**
     * Periodically checks if the connection is open and if the ping is received.
     */
    public void checkConnectionStatus() {
        while (!clientSocket.isClosed()) {
            try {
                TimeUnit.SECONDS.sleep(ConstantValues.connectionTimeout_seconds);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException while waiting for a Pong");
                throw new RuntimeException(e);
            }
            synchronized (pingLock) {
                if (!receivedPing&&!clientSocket.isClosed()) {
                    System.out.println("!! Disconnection detected !! A pong was not received in time");
                    serverSideMessageListener.disconnectionHappened(this);
                } else {
                    receivedPing = false;
                }
            }
        }
    }

    /**
     * Ends the connection between client and server by closing the input and output streams and the socket.
     */
    public void stopConnection() {
        if(clientSocket.isClosed()){
            return;
        }
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection streams to" + clientSocket.getInetAddress().toString());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection to" + clientSocket.getInetAddress().toString());
        }
    }
}