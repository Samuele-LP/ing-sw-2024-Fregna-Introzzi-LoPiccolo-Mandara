package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The NameChosenSuccessfullyMessage class represents a message sent to the client
 * to notify them that the name they have chosen has been accepted.
 */
public class NameChosenSuccessfullyMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the NameChosenSuccessfullyMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
