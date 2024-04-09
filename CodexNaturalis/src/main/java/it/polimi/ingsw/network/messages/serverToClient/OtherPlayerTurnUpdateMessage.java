package it.polimi.ingsw.network.messages.serverToClient;


public class OtherPlayerTurnUpdateMessage extends TurnUpdateMessage {
    private final String playerName;
    public OtherPlayerTurnUpdateMessage(PlayerFieldMessage playerField, SharedFieldUpdateMessage sharedField, String playerName) {
        super(playerField, sharedField);
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
}
