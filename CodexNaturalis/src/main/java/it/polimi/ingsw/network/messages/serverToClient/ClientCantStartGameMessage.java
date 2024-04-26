package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent if a client wants to start a game, but it hasn't the rights to do so
 */
public class ClientCantStartGameMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
