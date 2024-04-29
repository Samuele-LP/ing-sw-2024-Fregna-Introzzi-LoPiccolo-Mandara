package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * Sends a player the card they have drawn and the changes made to the common field made by the draw.
 */
public class SendDrawncardMessage extends ServerToClientMessage {
    private final SharedFieldUpdateMessage sharedField;
    private final int drawnCardID;

    public SendDrawncardMessage(SharedFieldUpdateMessage sharedField, int drawnCardID) {
        this.sharedField = sharedField;
        this.drawnCardID = drawnCardID;
    }

    /**
     *
     * @return the ID of the card that has been drawn
     */
    public int getDrawnCardID() {
        return drawnCardID;
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
