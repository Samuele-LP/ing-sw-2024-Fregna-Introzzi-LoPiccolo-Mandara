package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * The ClientToServerMessage class represents a message sent from the client to the server.
 * It is an abstract class that extends the Message class and defines a method to execute
 * the message using a server-side message listener and the client handler that sent the message.
 */
public abstract class ClientToServerMessage extends Message{

    /**
     * Executes the message using the provided server-side message listener and client handler.
     *
     * @param lis    the server-side message listener that handles the execution of the message.
     * @param sender the client handler that sent the message.
     */
    public abstract void execute(ServerSideMessageListener lis, ClientHandlerSocket sender);
}
