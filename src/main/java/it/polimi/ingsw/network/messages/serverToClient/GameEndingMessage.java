package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Message that contains the information about how the game ended and the leaderboard
 */
public class GameEndingMessage extends ServerToClientMessage {

    ImmutableScoreTrack finalPlayerScore ;
    private final List<String> winners;

    public GameEndingMessage(ImmutableScoreTrack finalPlayerScore, List<String> winners) {
        this.finalPlayerScore = finalPlayerScore;
        this.winners= winners;
    }

    public ImmutableScoreTrack getFinalPlayerScore() {
        return finalPlayerScore;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }

    public List<String> getWinners() {
        return winners;
    }
}
