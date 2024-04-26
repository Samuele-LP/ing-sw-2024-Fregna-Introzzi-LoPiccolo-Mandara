package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message returned when a player has chosen a name that is already being used by another player
 */
public class NameNotAvailableMessage extends ServerToClientMessage {

    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
