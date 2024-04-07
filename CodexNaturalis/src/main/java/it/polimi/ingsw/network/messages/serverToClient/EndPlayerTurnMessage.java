package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

import java.util.List;

/**
 * Message used to inform a player that their turn has ended,it also contains the player's
 * updated hand data and the playing field update data after a card has been drawn
 */
public class EndPlayerTurnMessage extends it.polimi.ingsw.network.messages.Message {
    private final Message sharedField;
    private final List<String> updatedPlayerHand;
    public EndPlayerTurnMessage(Message sharedField, List<String> updatedPlayerHand) {
        this.sharedField = sharedField;
        this.updatedPlayerHand = updatedPlayerHand;
    }
}
