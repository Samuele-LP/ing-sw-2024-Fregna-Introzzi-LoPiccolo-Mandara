package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * The ChooseStartingCardSideMessage class represents a message sent by the player at the start of the game
 * to choose how to place their starting card.
 */
public class ChooseStartingCardSideMessage extends ClientToServerMessage {

    /**
     * Indicates whether the starting card is to be placed facing up.
     */
    private final boolean isFacingUp;

    /**
     * Constructs a ChooseStartingCardSideMessage with the specified card placement choice.
     *
     * @param isFacingUp true if the starting card is to be placed facing up, false if facing down.
     */
    public ChooseStartingCardSideMessage(boolean isFacingUp) {
        this.isFacingUp = isFacingUp;
    }

    /**
     * Returns whether the starting card is to be placed facing up.
     *
     * @return true if the starting card is to be placed facing up, false if facing down.
     */
    public boolean facingUp() {
        return isFacingUp;
    }

    /**
     * Executes the message using the provided server-side message listener.
     *
     * @param lis the server-side message listener that handles the ChooseStartingCardSideMessage.
     * @param sender the client handler that sent the message.
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
