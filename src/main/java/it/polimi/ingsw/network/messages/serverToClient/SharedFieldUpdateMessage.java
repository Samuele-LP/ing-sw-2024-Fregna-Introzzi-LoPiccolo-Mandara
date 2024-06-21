package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import it.polimi.ingsw.model.enums.CardType;

import java.util.List;

/**
 * The SharedFieldUpdateMessage class represents a message that contains information on the visible cards,
 * the decks' top cards' back color, and the scores of the players.
 * It is sent to all players when a card is drawn and when points are scored.
 */
public class SharedFieldUpdateMessage extends ServerToClientMessage {

    /**
     * The score track to be displayed by the client.
     */
    private final ImmutableScoreTrack scoreTrack;

    /**
     * The color of the card on top of the resource deck.
     */
    private final CardType resourceBackside;

    /**
     * The color of the card on top of the gold deck.
     */
    private final CardType goldBackside;

    /**
     * The four visible cards that can be drawn.
     */
    private final List<Integer> visibleCards;

    /**
     * Constructs a SharedFieldUpdateMessage with the specified score track, resource backside, gold backside, and visible cards.
     *
     * @param scoreTrack     the score track to be displayed by the client.
     * @param resourceBackside the color of the card on top of the resource deck.
     * @param goldBackside   the color of the card on top of the gold deck.
     * @param visibleCards   the four visible cards that can be drawn.
     */
    public SharedFieldUpdateMessage(ImmutableScoreTrack scoreTrack, CardType resourceBackside, CardType goldBackside, List<Integer> visibleCards) {
        this.scoreTrack = scoreTrack;
        this.resourceBackside = resourceBackside;
        this.goldBackside = goldBackside;
        this.visibleCards = visibleCards;
    }

    /**
     * Returns the score track to be displayed by the client.
     *
     * @return the score track.
     */
    public ImmutableScoreTrack getScoreTrack() {
        return scoreTrack;
    }

    /**
     * Returns the color of the card on top of the resource deck.
     *
     * @return the resource backside color.
     */
    public CardType getResourceBackside() {
        return resourceBackside;
    }

    /**
     * Returns the color of the card on top of the gold deck.
     *
     * @return the gold backside color.
     */
    public CardType getGoldBackside() {
        return goldBackside;
    }

    /**
     * Returns the four visible cards that can be drawn, in this order: resource first visible -> resource second visible -> gold first visible -> gold second visible.
     * The values of the list are set to 0 if there is no visible card in that position.
     *
     * @return the list of visible cards.
     */
    public List<Integer> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the SharedFieldUpdateMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
