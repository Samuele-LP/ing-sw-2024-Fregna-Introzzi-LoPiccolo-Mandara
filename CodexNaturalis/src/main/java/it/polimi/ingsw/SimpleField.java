package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.TokenType;

import java.util.List;
import java.util.Map;

public class SimpleField {
    private final List<SimpleCard> cards;
    private final Map<TokenType,Integer> suymbols;
    private final String name;

    public SimpleField(List<SimpleCard> cards, Map<TokenType, Integer> suymbols, String name) {
        this.cards = cards;
        this.suymbols = suymbols;
        this.name = name;
    }
}
