package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Pong message sent by the server to the client
 */
public class Pong extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
