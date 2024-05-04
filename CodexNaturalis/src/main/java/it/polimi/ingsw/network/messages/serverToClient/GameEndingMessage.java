package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.HashMap;

/**
 * Message that contains the information about how the game ended and the leaderboard
 */
public class GameEndingMessage extends ServerToClientMessage {

    private final HashMap<String, Integer> finalPlayerScore ;

    public GameEndingMessage(HashMap<String, Integer> finalPlayerScore) {
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
