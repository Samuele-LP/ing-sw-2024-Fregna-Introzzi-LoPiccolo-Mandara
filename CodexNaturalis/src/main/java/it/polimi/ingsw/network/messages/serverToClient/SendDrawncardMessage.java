package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * Sends a player the card they have drawn and the changes made to the common field made by the draw.
 */
public class SendDrawncardMessage extends Message {
    private final SharedFieldUpdateMessage sharedField;
    private final int drawnCardID;

    public SendDrawncardMessage(SharedFieldUpdateMessage sharedField, int drawnCardID) {
        this.sharedField = sharedField;
        this.drawnCardID = drawnCardID;
    }

    public int getDrawnCardID() {
        return drawnCardID;
    }

    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }
}
