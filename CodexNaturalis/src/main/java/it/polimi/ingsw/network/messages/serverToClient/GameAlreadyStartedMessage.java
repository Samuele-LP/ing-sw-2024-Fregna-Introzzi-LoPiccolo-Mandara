package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message sent if a client tries to connect to a game that has already begun
 */
public class GameAlreadyStartedMessage extends ServerToClientMessage {
    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
