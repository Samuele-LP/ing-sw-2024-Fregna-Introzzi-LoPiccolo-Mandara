package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.TokenType;

import java.util.HashMap;
import java.util.List;

public class SimpleField {

    private final List<SimpleCard> cards;

    private final HashMap<TokenType,Integer> symbols;

    private final String name;

    public SimpleField(List<SimpleCard> cards, HashMap<TokenType, Integer> suymbols, String name) {
        this.cards = cards;
        this.symbols = suymbols;
        this.name = name;
    }

    public List<SimpleCard> getCards() {
        return cards;
    }

    public HashMap<TokenType, Integer> getSymbols() {
        return symbols;
    }

    public String getName() {
        return name;
    }
}
