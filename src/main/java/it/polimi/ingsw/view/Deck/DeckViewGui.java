package it.polimi.ingsw.view.Deck;

import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;

/**
 * Concrete class used to represent the deck view in a graphical user interface.
 */
public class DeckViewGui extends DeckView{

    /**
     * Constructor for DeckViewGui.
     *
     * @param type the type of the deck (e.g., "Gold", "Resource")
     * @throws IOException if there is an error initializing the deck view
     */
    public DeckViewGui(String type) throws IOException {
        super(type);
    }

    /**
     * Gets the ID of the first visible card.
     *
     * @return the ID of the first visible card, or null if there is no first visible card
     */
    public Integer getFirstVisible(){
        return firstVisible;
    }

    /**
     * Gets the ID of the second visible card.
     *
     * @return the ID of the second visible card, or null if there is no second visible card
     */
    public Integer getSecondVisible(){
        return secondVisible;
    }

    /**
     * Gets the color of the top card of the deck.
     *
     * @return the color of the top card of the deck, or null if the deck is empty
     */
    public CardType getTopColour(){
        return topCard;
    }
}
