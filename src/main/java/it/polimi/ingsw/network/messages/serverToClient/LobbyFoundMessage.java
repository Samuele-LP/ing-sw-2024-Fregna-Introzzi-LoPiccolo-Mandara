package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent to notify a client that they are now successfully connected to a game lobby
 */
public class LobbyFoundMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
