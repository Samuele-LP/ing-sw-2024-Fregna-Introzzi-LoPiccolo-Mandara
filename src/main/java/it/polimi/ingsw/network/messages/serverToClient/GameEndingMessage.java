package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.List;

/**
 * The GameEndingMessage class represents a message that contains the information about
 * how the game ended and the leaderboard.
 */
public class GameEndingMessage extends ServerToClientMessage {

    /**
     * The final score track of the players.
     */
    ImmutableScoreTrack finalPlayerScore ;

    /**
     * The list of winners.
     */
    private final List<String> winners;

    /**
     * Constructs a GameEndingMessage with the specified final score track and list of winners.
     *
     * @param finalPlayerScore the final score track of the players.
     * @param winners          the list of winners.
     */
    public GameEndingMessage(ImmutableScoreTrack finalPlayerScore, List<String> winners) {
        this.finalPlayerScore = finalPlayerScore;
        this.winners= winners;
    }

    /**
     * Returns the final score track of the players.
     *
     * @return the final score track.
     */
    public ImmutableScoreTrack getFinalPlayerScore() {
        return finalPlayerScore;
    }

    /**
     * Returns the list of winners.
     *
     * @return the list of winners.
     */
    public List<String> getWinners() {
        return winners;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the GameEndingMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
