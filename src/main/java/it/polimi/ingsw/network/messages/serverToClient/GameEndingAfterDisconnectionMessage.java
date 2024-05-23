package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.HashMap;

/**
 * Message sent when the game has to end after a player has been disconnected
 */
public class GameEndingAfterDisconnectionMessage extends ServerToClientMessage {

    private final HashMap<String, Integer> finalPlayerScore ;

    public GameEndingAfterDisconnectionMessage(HashMap<String, Integer> finalPlayerScore) {
        this.finalPlayerScore = finalPlayerScore;
    }

    public HashMap<String, Integer> getFinalPlayerScore() {
        return finalPlayerScore;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
