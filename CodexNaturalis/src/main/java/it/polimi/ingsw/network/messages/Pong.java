package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ClientSideMessageListener;

/**
 * Pong message sent as a response to a Ping
 */
public class Pong extends ServerToClientMessage {
    /**
     *
     * @param lis will handle the Ping
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
