package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.serverToClient.GenericMessage;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;

import static it.polimi.ingsw.network.commonData.ConstantValues.maxMessagesInQueue;
import static it.polimi.ingsw.network.commonData.ConstantValues.socketTimeout_seconds;

public class ClientHandler extends Thread {

    /**
     * Debugging: name of this class
     */
    String className = ClientHandler.class.getName();

    /**
     * ServerSideMessageListener of the related game
     */
    private final ServerSideMessageListener messageListener = GameController.getInstance();

    /**
     * ObjectInputStream in
     */
    private final ObjectInputStream in;

    /**
     * ObjectOutputStream out
     */
    private final ObjectOutputStream out;

    /**
     * Client Socket, unchangeable
     */
    private final Socket clientSocket;

    /**
     * Create a queue for messages that can take up to maxMessagesInQueue messages.
     * NB: not more than maxMessagesInQueue in order to limit the spam ability of clients
     */
    private final ArrayBlockingQueue<ClientToServerMessage> queue = new ArrayBlockingQueue<>(maxMessagesInQueue);

    /**
     * Used to handle Client needs
     *
     * @param clientSocket is the connection to the client
     * @throws IOException if an error occurs during input/output stream creation
     */
    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientSocket.setSoTimeout(1000 * socketTimeout_seconds);//sets the socket timeout to 30 seconds  --java.net.SocketTimeoutException
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Receives messages by client to server and adds them to a queue
     */
    public void receiveMessage() {
        try {
            ClientToServerMessage message;
            while (!clientSocket.isClosed()) {
                message = (ClientToServerMessage) in.readObject();
                queue.add(message);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("\n\nSocket timer expired. Flagging clientHandler as disconnected and closing the socket\n\n");
            messageListener.disconnectHandler(this);
            this.closeSocket();
        } catch (IOException e1) {

            System.out.print("\n\n!!! IOException thrown at " + className + new Exception().getStackTrace()[0].getLineNumber() + "\nRemoving clientHandler from connected clients\n\n");
            messageListener.disconnectHandler(this);

        } catch (IllegalStateException e2) {

            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Client is spamming messages!\n\n");

        } catch (ClassCastException e3) {//If an invalid object is passed to the server a response is sent to the client

            try {
                this.sendMessage(new GenericMessage("\n\nInvalid message received! The only objects that can be sent to the server must be of ClientToServerMessage type\n\n"));
            } catch (IOException ex) {
                System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Failed for some reasons!\n\n");
                messageListener.disconnectHandler(this);
            }
        } catch (ClassNotFoundException e4) {
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Threw a ClasNotFoundException for some reasons!\n\n");
        }
    }

    /**
     * Passes queued messages to the listener
     */
    public void passMessage() {
        try {
            ClientToServerMessage message;
            while (!clientSocket.isClosed()) {
                //NB: ".take()" with an ArrayBlockingQueue does the following:
                //Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
                message = queue.take();
                message.execute(this.messageListener, this);
            }
        } catch (InterruptedException e) {
            System.out.print("\n\n!!! Error !!! (" + className + new Exception().getStackTrace()[0].getLineNumber() + ") Unable to pass message to Server!\n\n");
            throw new RuntimeException(e);
        }
    }

    /**
     * Interrupt the thread
     */
    public void interruptSelf() {
        this.interrupt();
    }

    /**
     * Method used for debugging. It prints the number of elements that are inside the queue
     */
    private void printQueueNumberOfElements() {
        System.out.println("Elements in queue: " + queue.size());
    }

    /**
     * Sends a message to the client
     *
     * @param mes is the message generated by the controller
     * @throws IOException if there are problems sending the message
     */
    public void sendMessage(ServerToClientMessage mes) throws IOException {
        synchronized (out) {
            out.writeObject(mes);
        }
    }

    /**
     * Closes the input and output streams and ends the socket connection
     */
    public void closeSocket() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error while closing the ClientHandler socket");
        }
    }
}