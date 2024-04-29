package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Message used to notify a user that his Field is valid and that the Client can be reconnected to the game
 */

public class ClientFieldCheckValidityMessage extends ServerToClientMessage {

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
