package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

/**
 * Message used to inform a player that their turn has ended.
 */
public class EndPlayerTurnMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
