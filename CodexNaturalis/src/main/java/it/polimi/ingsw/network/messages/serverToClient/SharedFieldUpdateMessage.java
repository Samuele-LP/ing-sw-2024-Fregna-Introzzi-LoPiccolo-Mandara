package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import it.polimi.ingsw.model.enums.CardType;

import java.util.List;
/**
 * Message that contains information on the visible cards, the decks' top cards' back colour, and the scores of the players
 * It is sent to all the players when a card is drawn and when points are scored
 */
public class SharedFieldUpdateMessage extends ServerToClientMessage {

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
     *The values of the list are set to 0 if there is no visibleCard in that position
     * @return the four visible cards that can be drawn, in this order resource first visible -> resource second visible -> gold first visible-> gold second visible
     */
    public List<Integer> getVisibleCards() {
        return visibleCards;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
