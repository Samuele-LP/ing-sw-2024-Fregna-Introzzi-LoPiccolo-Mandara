package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The PlayerCantPlayAnymoreMessage class represents a message sent to notify a player
 * that they have blocked all their possible placements and can no longer play.
 */
public class PlayerCantPlayAnymoreMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the PlayerCantPlayAnymoreMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
