package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent if a player makes an illegal placement move
 */
public class IllegalPlacementPositionMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
