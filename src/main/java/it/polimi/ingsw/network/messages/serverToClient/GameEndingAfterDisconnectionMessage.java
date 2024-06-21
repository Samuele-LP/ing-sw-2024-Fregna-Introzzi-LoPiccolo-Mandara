package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.List;

/**
 * The GameEndingAfterDisconnectionMessage class represents a message sent when the game
 * has to end after a player has been disconnected.
 */
public class GameEndingAfterDisconnectionMessage extends ServerToClientMessage {

    /**
     * The final score track of the players.
     */
    private final ImmutableScoreTrack finalPlayerScore ;

    /**
     * The list of winners.
     */
    private final List<String > winners ;

    /**
     * Constructs a GameEndingAfterDisconnectionMessage with the specified final score track and list of winners.
     *
     * @param scoreTrack the final score track of the players.
     * @param winners    the list of winners.
     */
    public GameEndingAfterDisconnectionMessage(ImmutableScoreTrack scoreTrack, List<String> winners) {
        finalPlayerScore=scoreTrack;
        this.winners=winners;
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
     * @param lis the client-side message listener that handles the GameEndingAfterDisconnectionMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
