package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.model.ImmutableScoreTrack;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.network.messages.Message;

import java.util.List;
/**
 * Message that contains information on the visible cards, the decks' top cards' back colour, and the scores of the players
 * It is sent to all the players when a card is drawn and when points are scored
 */
public class SharedFieldUpdateMessage extends Message {

    private final ImmutableScoreTrack scoreTrack;
    private final CardType resourceBackside;
    private final CardType goldBackside;
    private final List<Integer> visibleCards;
    public SharedFieldUpdateMessage(ImmutableScoreTrack scoreTrack, CardType resourceBackside, CardType goldBackside, List<Integer> visibleCards) {
        this.scoreTrack = scoreTrack;
        this.resourceBackside = resourceBackside;
        this.goldBackside = goldBackside;
        this.visibleCards = visibleCards;
    }

    /**
     * @return the score track to be displayed by the client
     */
    public ImmutableScoreTrack getScoreTrack() {
        return scoreTrack;
    }

    /**
     * @return the colour of the card on top of the resource deck
     */
    public CardType getResourceBackside() {
        return resourceBackside;
    }

    /**
     * @return the colour of the card on top of the gold deck
     */
    public CardType getGoldBackside() {
        return goldBackside;
    }

    /**
     * @return returns the four visible cards that can be drawn
     */
    public List<Integer> getVisibleCards() {
        return visibleCards;
    }
}
