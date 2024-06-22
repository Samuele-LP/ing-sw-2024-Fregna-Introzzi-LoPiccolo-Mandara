package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.TokenType;

import java.util.HashMap;
import java.util.List;

/**
 * Class representing a simplified version of a playing field.
 * It contains the cards on the field, the count of different symbols, and the name of the field owner.
 */
public class SimpleField {

    private final List<SimpleCard> cards;

    private final HashMap<TokenType,Integer> symbols;

    private final String name;

    /**
     * Constructor for the SimpleField class.
     *
     * @param cards   the list of cards on the field
     * @param symbols the count of different symbols on the field
     * @param name    the name of the field owner
     */
    public SimpleField(List<SimpleCard> cards, HashMap<TokenType, Integer> symbols, String name) {
        this.cards = cards;
        this.symbols = symbols;
        this.name = name;
    }

    /**
     * Gets the list of cards on the field.
     *
     * @return the list of cards on the field
     */
    public List<SimpleCard> getCards() {
        return cards;
    }

    /**
     * Gets the count of different symbols on the field.
     *
     * @return a hashmap with the count of different symbols on the field
     */
    public HashMap<TokenType, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Gets the name of the field owner.
     *
     * @return the name of the field owner
     */
    public String getName() {
        return name;
    }
}
