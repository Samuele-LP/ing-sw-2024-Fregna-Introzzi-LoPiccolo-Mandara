package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;

/**
 * Message used to tell the Server that the Client decided to disconnect from the game
 */

public class ClientDisconnectedVoluntarilyMessage extends ClientToServerMessage {

    @Override
    public void execute(ServerSideMessageListener lis) {
        lis.handle(this);
    }
}
