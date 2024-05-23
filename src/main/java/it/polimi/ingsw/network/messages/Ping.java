package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.server.ClientHandlerSocket;


/**
 * Ping message sent by the client to the server.
 */
public class Ping extends ClientToServerMessage{

    /**
     * @param lis will handle the Ping and make the necessary operations
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this,sender);
    }
}
