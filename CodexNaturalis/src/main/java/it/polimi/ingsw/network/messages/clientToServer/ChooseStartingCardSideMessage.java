package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;

/**
 * Message sent by the player at the start of the game to choose how to place their starting card
 */
public class ChooseStartingCardSideMessage extends ClientToServerMessage {
    private final boolean isFacingUp;

    public ChooseStartingCardSideMessage(boolean isFacingUp) {
        this.isFacingUp = isFacingUp;
    }

    /**
     * @return true if the starting card is to be placed facing up, false if facing down
     */
    public boolean facingUp() {
        return isFacingUp;
    }

    @Override
    public void execute(ServerSideMessageListener lis) {
        lis.handle(this);
    }
}
