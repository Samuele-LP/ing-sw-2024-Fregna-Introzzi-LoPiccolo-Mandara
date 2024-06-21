package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The ChooseColourMessage class represents a message sent from the server to the client
 * to prompt the client to choose a color.
 */
public class ChooseColourMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the ChooseColourMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
