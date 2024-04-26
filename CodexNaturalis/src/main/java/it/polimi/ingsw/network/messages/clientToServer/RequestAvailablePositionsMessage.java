package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;

/**
 * Message that a client sends when they want to know where they can place a card (ignoring gold card requirements)
 */
public class RequestAvailablePositionsMessage extends ClientToServerMessage {
    @Override
    public void execute(ServerSideMessageListener lis) {
        lis.handle(this);
    }
}
