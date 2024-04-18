package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;

/**
 * Message sent to the client after a valid move has been made
 * This message also serves to inform the player that they now have
 * to draw a card
 */
public class SuccessfulPlacementMessage extends TurnUpdateMessage{
    public SuccessfulPlacementMessage(PlayerPlacedCardInformation placedCardInformation, SharedFieldUpdateMessage sharedField) {
        super(placedCardInformation, sharedField);
    }
}
