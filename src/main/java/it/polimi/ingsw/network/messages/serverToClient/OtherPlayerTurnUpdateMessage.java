package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.Map;

/**
 * The OtherPlayerTurnUpdateMessage class is used to send information about the move made this turn
 * by another player to all clients.
 */
public class OtherPlayerTurnUpdateMessage extends TurnUpdateMessage {

    /**
     * The name of the player who made the move.
     */
    private final String playerName;

    /**
     * Constructs an OtherPlayerTurnUpdateMessage with the specified information.
     *
     * @param visibleSymbols         the visible symbols after the move.
     * @param placedCardInformation  the information of the placed card.
     * @param sharedField            the shared field update message.
     * @param playerName             the name of the player who made the move.
     */
    public OtherPlayerTurnUpdateMessage(Map<TokenType, Integer> visibleSymbols, SimpleCard placedCardInformation, SharedFieldUpdateMessage sharedField, String playerName) {
        super(visibleSymbols,placedCardInformation, sharedField);
        this.playerName = playerName;
    }

    /**
     * Returns the name of the owner of the playing field to be updated. The owner won't be the client who receives this type of message.
     *
     * @return the name of the player who made the move.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the OtherPlayerTurnUpdateMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
