package it.polimi.ingsw.network.messages.serverToClient;

/**
 * Class used to send to other players information about the move made this turn
 */
public class OtherPlayerTurnUpdateMessage extends TurnUpdateMessage {
    private final String playerName;
    public OtherPlayerTurnUpdateMessage(PlayerPlacedCardInformation playerField, SharedFieldUpdateMessage sharedField, String playerName) {
        super(playerField, sharedField);
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
}
