package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

/**
 * Sends a player the card they have drawn and the changes made to the common field made by the draw.
 */
public class SendDrawncardMessage extends ServerToClientMessage {
    private final SharedFieldUpdateMessage sharedField;
    private final List<Integer> updatedPlayerHand;

    public SendDrawncardMessage(SharedFieldUpdateMessage sharedField, List<Integer> updatedPlayerHand) {
        this.sharedField = sharedField;
        this.updatedPlayerHand = updatedPlayerHand;
    }

    /**
     * @return the ID of the card that has been drawn
     */
    public List<Integer> getPlayerHand() {
        return updatedPlayerHand;
    }

    /**
     *
     * @return an update on the sharedField
     */
    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
