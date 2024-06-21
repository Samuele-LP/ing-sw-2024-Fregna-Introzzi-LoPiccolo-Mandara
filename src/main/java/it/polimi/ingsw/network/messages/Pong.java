package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ClientSideMessageListener;

/**
 * The Pong class represents a Pong message sent as a response to a Ping message.
 */
public class Pong extends ServerToClientMessage {

    /**
     * Executes the Pong message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the Pong message.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
