package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent if a player wants to draw from an empty deck
 */
public class EmptyDeckMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
