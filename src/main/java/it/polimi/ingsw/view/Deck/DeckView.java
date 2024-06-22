package it.polimi.ingsw.view.Deck;

import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;

/**
 * Abstract class used to memorize the necessary information about the decks to be shown to the player.
 */
public abstract class DeckView {

    /**
     * The type of the deck (e.g., "Gold", "Resource").
     */
    protected final String type;

    /**
     * The color of the top card of the deck.
     */
    protected CardType topCard;

    /**
     * The ID of the first visible card.
     */
    protected Integer firstVisible;

    /**
     * The ID of the second visible card.
     */
    protected Integer secondVisible;

    /**
     * Constructor for DeckView.
     *
     * @param type the type of the deck (e.g., "Gold", "Resource")
     * @throws IOException if there is an error initializing the deck view
     */
    public DeckView(String type) throws IOException {
        this.type = type;
    }

    /**
     * Updates the deck view with the latest information about the deck.
     *
     * @param top             the color of the current top card of the deck
     * @param firstVisibleID  the ID of the current first visible card
     * @param secondVisibleID the ID of the current second visible card
     */
    public void update(CardType top, Integer firstVisibleID, Integer secondVisibleID) {
        topCard = top;
        firstVisible = firstVisibleID;
        secondVisible = secondVisibleID;
    }
}
