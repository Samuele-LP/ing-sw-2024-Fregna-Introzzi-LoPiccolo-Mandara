package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * The Ping class represents a Ping message sent by the client to the server.
 */
public class Ping extends ClientToServerMessage{

    /**
     * Executes the Ping message using the provided server-side message listener.
     *
     * @param lis    the server-side message listener that handles the Ping message.
     * @param sender the client handler that sent the Ping message.
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this,sender);
    }
}
