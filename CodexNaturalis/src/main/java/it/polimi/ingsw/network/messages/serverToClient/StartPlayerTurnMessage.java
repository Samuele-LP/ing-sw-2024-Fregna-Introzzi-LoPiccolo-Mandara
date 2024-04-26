package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent to a player when the player can start sending messages related to placing and drawing cards
 */
public class StartPlayerTurnMessage extends ServerToClientMessage {

    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
