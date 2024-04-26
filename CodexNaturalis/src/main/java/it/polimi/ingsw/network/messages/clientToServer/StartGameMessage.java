package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;

/**
 * Message sent by a client to start the game
 */
public class StartGameMessage extends ClientToServerMessage {
    @Override
    public void execute(ServerSideMessageListener lis) {
        lis.handle(this);
    }
}
