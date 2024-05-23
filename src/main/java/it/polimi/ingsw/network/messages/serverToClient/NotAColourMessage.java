package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent in response to a ChosenColourMessage if the contained information does not represent a colour
 */
public class NotAColourMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
