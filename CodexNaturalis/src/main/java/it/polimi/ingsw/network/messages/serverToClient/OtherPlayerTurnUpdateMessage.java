package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;

/**
 * Class used to send to other players information about the move made this turn
 */
public class OtherPlayerTurnUpdateMessage extends TurnUpdateMessage {
    private final String playerName;
    public OtherPlayerTurnUpdateMessage(PlayerPlacedCardInformation playerField, SharedFieldUpdateMessage sharedField, String playerName) {
        super(playerField, sharedField);
        this.playerName = playerName;
    }

    /**
     *
     * @return the name of the owner of the playing field to be updated. The owner won't be the client who receives this type of message.
     */
    public String getPlayerName() {
        return playerName;
    }
}
