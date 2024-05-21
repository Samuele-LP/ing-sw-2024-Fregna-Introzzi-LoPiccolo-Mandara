package it.polimi.ingsw.view.Deck;

import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;

/**
 * Used to memorize the necessary information about the decks to be shown to the player
 */
public abstract class DeckView {
    protected final String type;
    protected CardType topCard;
    protected Integer firstVisible;
    protected Integer secondVisible;

    public DeckView(String type) throws IOException {
        this.type = type;
    }

    /**
     * @param top             colour of the current top of the deck
     * @param firstVisibleID  id of the current first visible card
     * @param secondVisibleID id of the current second visible card
     */
    public void update(CardType top, Integer firstVisibleID, Integer secondVisibleID) {
        topCard = top;
        firstVisible = firstVisibleID;
        secondVisible = secondVisibleID;
    }
}
