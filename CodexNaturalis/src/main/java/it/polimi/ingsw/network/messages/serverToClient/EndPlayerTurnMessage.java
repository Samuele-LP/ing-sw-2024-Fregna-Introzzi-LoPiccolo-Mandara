package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

/**
 * Message used to inform a player that their turn has ended,it also contains the player's
 * updated hand data and the playing field update data after a card has been drawn.
 */
public class EndPlayerTurnMessage extends ServerToClientMessage {
    private final SharedFieldUpdateMessage sharedField;
    private final List<Integer> updatedPlayerHand;
    public EndPlayerTurnMessage(SharedFieldUpdateMessage sharedField, List<Integer> updatedPlayerHand) {
        this.sharedField = sharedField;
        this.updatedPlayerHand = updatedPlayerHand;
    }

    /**
     *
     * @return an ArrayList containing the ID's of the cards in a player's hand
     */
    public List<Integer> getUpdatedPlayerHand() {
        return updatedPlayerHand;
    }

    /**
     *
     * @return a SharedFieldUpdateMessage that contains information about the shared field after a card has been drawn
     */
    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
