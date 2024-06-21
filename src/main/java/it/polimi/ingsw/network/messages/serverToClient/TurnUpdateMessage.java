package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * The TurnUpdateMessage class represents a message that contains information about what happened during a placement.
 */
public class TurnUpdateMessage extends ServerToClientMessage {

    /**
     * The visible symbols after the move.
     */
    private final Map<TokenType,Integer> visibleSymbols;

    /**
     * Information about the placed card.
     */
    private final SimpleCard placedCardInformation;

    /**
     * Information about the shared field.
     */
    private final SharedFieldUpdateMessage sharedField;

    /**
     * Constructs a TurnUpdateMessage with the specified information.
     *
     * @param visibleSymbols the visible symbols after the move.
     * @param playerField the information of the field.
     * @param sharedField the shared field update message.
     */
    public TurnUpdateMessage(Map<TokenType, Integer> visibleSymbols, SimpleCard playerField, SharedFieldUpdateMessage sharedField) {
        this.visibleSymbols = new HashMap<>(visibleSymbols);
        this.placedCardInformation = playerField;
        this.sharedField = sharedField;
    }

    /**
     * Returns the information about the placed card.
     *
     * @return the placed card information.
     */
    public SimpleCard getPlacedCardInformation() {
        return placedCardInformation;
    }

    /**
     * Returns the information about the shared field.
     *
     * @return the shared field update message, or null if no points were scored during the placement.
     */
    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }

    /**
     * Returns the visible symbols after the move.
     *
     * @return a map of visible symbols.
     */
    public Map<TokenType, Integer> getVisibleSymbols() {
         return visibleSymbols;
}

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the TurnUpdateMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
            lis.handle(this);
        }
}

