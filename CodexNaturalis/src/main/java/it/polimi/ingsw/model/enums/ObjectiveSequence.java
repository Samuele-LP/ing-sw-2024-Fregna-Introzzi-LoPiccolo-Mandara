package it.polimi.ingsw.model.enums;

/**
 * Enum used to represent the eight possible sequence objectives
 */
public enum ObjectiveSequence {
    redDiagonal             ("|      \u001B[41m   \u001B[0m|\n" +
                                    "|   \u001B[41m   \u001B[0m   |\n"+
                                    "|\u001B[41m   \u001B[0m      |\n"),
    greenAntiDiagonal       ("|\u001B[42m   \u001B[0m      |\n" +
                                    "|   \u001B[42m   \u001B[0m   |\n"+
                                    "|      \u001B[42m   \u001B[0m|\n"),
    blueDiagonal            ("|      \u001B[44m   \u001B[0m|\n" +
                                    "|   \u001B[44m   \u001B[0m   |\n"+
                                    "|\u001B[44m   \u001B[0m      |\n"),
    purpleAntiDiagonal      ("|\u001B[45m   \u001B[0m      |\n" +
                                    "|   \u001B[45m   \u001B[0m   |\n"+
                                    "|      \u001B[45m   \u001B[0m|\n"),
    twoRedOneGreen          ("|   \u001B[41m   \u001B[0m   |\n" +
                                    "|   \u001B[41m   \u001B[0m   |\n"+
                                    "|      \u001B[42m   \u001B[0m|\n"),
    twoGreenOnePurple       ("|   \u001B[42m   \u001B[0m   |\n" +
                                    "|   \u001B[42m   \u001B[0m   |\n"+
                                    "|\u001B[45m   \u001B[0m      |\n"),
    twoPurpleOneBlue        ("|\u001B[44m   \u001B[0m      |\n" +
                                    "|   \u001B[45m   \u001B[0m   |\n"+
                                    "|   \u001B[45m   \u001B[0m   |\n"),
    twoBlueOneRed        (   "|      \u001B[41m   \u001B[0m|\n" +
                                    "|   \u001B[44m   \u001B[0m   |\n"+
                                    "|   \u001B[44m   \u001B[0m   |\n");
    private final String symbol;
    ObjectiveSequence(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}

