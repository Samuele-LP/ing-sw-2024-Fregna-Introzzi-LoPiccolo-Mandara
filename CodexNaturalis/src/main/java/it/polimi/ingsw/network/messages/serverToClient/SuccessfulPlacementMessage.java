package it.polimi.ingsw.network.messages.serverToClient;

/**
 * Message sent to the client after a valid move has been made
 * This message also serves to inform the player that they now have
 * to draw a card
 */
public class SuccessfulPlacementMessage extends TurnUpdateMessage{
    public SuccessfulPlacementMessage(PlayerFieldMessage playerField, SharedFieldUpdateMessage sharedField) {
        super(playerField, sharedField);
    }
}
