package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent when the player tries to play a gold card when the placing  conditions aren't met
 */
public class NotEnoughResourcesMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
