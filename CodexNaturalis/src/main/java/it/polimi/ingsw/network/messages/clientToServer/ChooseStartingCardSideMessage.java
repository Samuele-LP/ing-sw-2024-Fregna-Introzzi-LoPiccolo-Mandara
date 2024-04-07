package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message sent by the player at the start of the game to choose how to place their starting card
 */
public class ChooseStartingCardSideMessage extends Message {
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
}
