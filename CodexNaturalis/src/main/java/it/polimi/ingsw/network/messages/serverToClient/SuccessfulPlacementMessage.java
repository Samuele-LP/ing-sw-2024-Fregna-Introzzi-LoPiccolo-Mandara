package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;

import java.util.Map;

/**
 * Message sent to the client after a valid move has been made
 * This message also serves to inform the player that they now have
 * to draw a card
 */
public class SuccessfulPlacementMessage extends TurnUpdateMessage{
    public SuccessfulPlacementMessage(Map<TokenType, Integer> visibleSymbols, PlayerPlacedCardInformation placedCardInformation, SharedFieldUpdateMessage sharedField) {
        super(visibleSymbols,placedCardInformation, sharedField);
    }
    @Override
    public void execute(ClientSideMessageListener lis){

    }
}
