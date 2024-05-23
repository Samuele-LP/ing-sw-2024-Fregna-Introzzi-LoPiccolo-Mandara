package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent when there are already 4 connected Players
 */
public class LobbyFullMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
