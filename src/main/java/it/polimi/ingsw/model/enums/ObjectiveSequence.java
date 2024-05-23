package it.polimi.ingsw.model.enums;

/**
 * Enum used to represent the eight possible sequence objectives
 */
public enum ObjectiveSequence {
    redDiagonal             ("|      \u001B[41m   \u001B[0m|X" +
                                    "|   \u001B[41m   \u001B[0m   |X"+
                                    "|\u001B[41m   \u001B[0m      |X"),
    greenAntiDiagonal       ("|\u001B[42m   \u001B[0m      |X" +
                                    "|   \u001B[42m   \u001B[0m   |X"+
                                    "|      \u001B[42m   \u001B[0m|X"),
    blueDiagonal            ("|      \u001B[44m   \u001B[0m|X" +
                                    "|   \u001B[44m   \u001B[0m   |X"+
                                    "|\u001B[44m   \u001B[0m      |X"),
    purpleAntiDiagonal      ("|\u001B[45m   \u001B[0m      |X" +
                                    "|   \u001B[45m   \u001B[0m   |X"+
                                    "|      \u001B[45m   \u001B[0m|X"),
    twoRedOneGreen          ("|   \u001B[41m   \u001B[0m   |X" +
                                    "|   \u001B[41m   \u001B[0m   |X"+
                                    "|      \u001B[42m   \u001B[0m|X"),
    twoGreenOnePurple       ("|   \u001B[42m   \u001B[0m   |X" +
                                    "|   \u001B[42m   \u001B[0m   |X"+
                                    "|\u001B[45m   \u001B[0m      |X"),
    twoPurpleOneBlue        ("|\u001B[44m   \u001B[0m      |X" +
                                    "|   \u001B[45m   \u001B[0m   |X"+
                                    "|   \u001B[45m   \u001B[0m   |X"),
    twoBlueOneRed        (   "|      \u001B[41m   \u001B[0m|X" +
                                    "|   \u001B[44m   \u001B[0m   |X"+
                                    "|   \u001B[44m   \u001B[0m   |X");
    private final String symbol;
    ObjectiveSequence(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}

