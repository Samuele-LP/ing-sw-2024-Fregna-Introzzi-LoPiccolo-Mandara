package it.polimi.ingsw.model.enums;

/**
 * Enum used to represent the nine possible Tokens that can appear as a card corner or, in case of a starting card, in the center of the card
 */
public enum TokenType {
    empty("□"),
    blocked("■"),
    fungi("\uD83C\uDF44"),
    insect("\uD83D\uDC1C"),
    animal("\uD83D\uDC36"),
    plant("\uD83C\uDF3F"),
    scroll("\uD83D\uDCDC")
    ,quill("\uD83D\uDD8C")
    ,ink("⛾");
    private String symbol;
    TokenType(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
