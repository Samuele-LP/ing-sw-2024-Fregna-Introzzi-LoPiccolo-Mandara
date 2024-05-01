package it.polimi.ingsw.model.enums;

/**
 * Enum used to determine the colour of a card
 */
public enum CardType {
    fungi("\uD83C\uDF44"),insect("\uD83D\uDC1C"),
    animal("\uD83D\uDC36"),
    plant("\uD83C\uDF3F")
    ,starter("S");
    private String symbol;
    CardType(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
