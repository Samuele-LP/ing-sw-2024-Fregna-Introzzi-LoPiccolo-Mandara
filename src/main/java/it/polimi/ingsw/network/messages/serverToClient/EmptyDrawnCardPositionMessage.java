package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The EmptyDrawnCardPositionMessage class represents a message that is sent during the final phase
 * if a player tries to draw a visible card from a spot that has no visible card on it.
 */
public class EmptyDrawnCardPositionMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the EmptyDrawnCardPositionMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
