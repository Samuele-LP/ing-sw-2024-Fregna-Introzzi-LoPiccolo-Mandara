package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ClientSideMessageListener;

/**
 * The ServerToClientMessage class represents a message sent from the server to a client.
 * It is an abstract class that extends the Message class and defines a method to execute
 * the message using a client-side message listener.
 */
public abstract class ServerToClientMessage extends Message{

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the execution of the message.
     */
    public abstract void execute(ClientSideMessageListener lis);
}
