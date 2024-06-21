package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The LobbyFullMessage class represents a message sent to notify a client
 * that the lobby is full when there are already 4 connected players.
 */
public class LobbyFullMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the LobbyFullMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
