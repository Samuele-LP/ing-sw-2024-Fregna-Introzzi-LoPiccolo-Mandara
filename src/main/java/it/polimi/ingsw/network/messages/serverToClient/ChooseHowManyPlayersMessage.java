package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The ChooseHowManyPlayersMessage class represents a message sent to the first client
 * that has chosen a name, prompting them to choose how many players will play.
 */
public class ChooseHowManyPlayersMessage extends ServerToClientMessage {

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the ChooseHowManyPlayersMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
