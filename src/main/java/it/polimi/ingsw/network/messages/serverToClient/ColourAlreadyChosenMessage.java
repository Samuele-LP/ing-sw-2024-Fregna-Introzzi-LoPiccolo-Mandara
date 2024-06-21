package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The ColourAlreadyChosenMessage class represents a message sent in response to a ChosenColourMessage
 * if the colour was already chosen by an earlier player.
 */
public class ColourAlreadyChosenMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the ColourAlreadyChosenMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
