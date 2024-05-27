package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.HashMap;
import java.util.List;

/**
 * Message sent when the game has to end after a player has been disconnected
 */
public class GameEndingAfterDisconnectionMessage extends ServerToClientMessage {

    private final ImmutableScoreTrack finalPlayerScore ;
    private final List<String > winners ;

    public GameEndingAfterDisconnectionMessage(ImmutableScoreTrack scoreTrack, List<String> winners) {
        finalPlayerScore=scoreTrack;
        this.winners=winners;
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
