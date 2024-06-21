package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The GenericMessage class represents a generic message sent from the server to the client.
 */
public class GenericMessage extends ServerToClientMessage {

    /**
     * The message content.
     */
    private String message;

    /**
     * Constructs a GenericMessage with the specified message content.
     *
     * @param message the content of the message.
     */
    public GenericMessage(String message){
        this.message = message;
    }

    /**
     * Returns the content of the message.
     *
     * @return the message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the GenericMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
