package it.polimi.ingsw.network.messages.serverToClient;


public class OtherPlayerTurnUpdateMessage extends TurnUpdateMessage {

    public OtherPlayerTurnUpdateMessage(PlayerFieldMessage playerField, SharedFieldUpdateMessage sharedField) {
        super(playerField, sharedField);
    }
}
