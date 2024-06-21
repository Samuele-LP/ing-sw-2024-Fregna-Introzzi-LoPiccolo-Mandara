package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

/**
 * The SendDrawnCardMessage class represents a message that sends a player the card they have drawn
 * and the changes made to the common field as a result of the draw.
 */
public class SendDrawncardMessage extends ServerToClientMessage {

    /**
     * The update on the shared field after the draw.
     */
    private final SharedFieldUpdateMessage sharedField;

    /**
     * The updated player's hand after the draw.
     */
    private final List<Integer> updatedPlayerHand;

    /**
     * Constructs a SendDrawnCardMessage with the specified shared field update and updated player hand.
     *
     * @param sharedField       the update on the shared field.
     * @param updatedPlayerHand the updated player's hand.
     */
    public SendDrawncardMessage(SharedFieldUpdateMessage sharedField, List<Integer> updatedPlayerHand) {
        this.sharedField = sharedField;
        this.updatedPlayerHand = updatedPlayerHand;
    }

    /**
     * Returns the updated player's hand after the draw.
     *
     * @return the updated player's hand.
     */
    public List<Integer> getPlayerHand() {
        return updatedPlayerHand;
    }

    /**
     * Returns the update on the shared field.
     *
     * @return the shared field update.
     */
    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the SendDrawnCardMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
