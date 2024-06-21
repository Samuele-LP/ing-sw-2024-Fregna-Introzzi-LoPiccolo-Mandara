package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.Map;

/**
 * The SuccessfulPlacementMessage class represents a message sent to the client after a valid move has been made.
 * This message also serves to inform the player that they now have to draw a card.
 */
public class SuccessfulPlacementMessage extends TurnUpdateMessage{

    /**
     * Constructs a SuccessfulPlacementMessage with the specified information.
     *
     * @param visibleSymbols        the visible symbols after the move.
     * @param placedCardInformation the information of the placed card.
     * @param sharedField           the shared field update message.
     */
    public SuccessfulPlacementMessage(Map<TokenType, Integer> visibleSymbols, SimpleCard placedCardInformation, SharedFieldUpdateMessage sharedField) {
        super(visibleSymbols,placedCardInformation, sharedField);
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the SuccessfulPlacementMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis){
        lis.handle(this);
    }
}
