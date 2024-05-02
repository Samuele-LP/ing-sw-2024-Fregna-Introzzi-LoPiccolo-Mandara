package it.polimi.ingsw.model.enums;

/**
 * Enum used to determine the colour of a card
 */
public enum CardType {
    fungi("\u001B[31m\u001B[48;2;255;255;255mFun\u001B[0m"),
    insect("\u001B[35m\u001B[48;2;255;255;255mIns\u001B[0m"),
    animal("\u001B[34m\u001B[48;2;255;255;255mAni\u001B[0m"),
    plant("\u001B[32m\u001B[48;2;255;255;255mPla\u001B[0m")
    ,starter("\u001B[33m\u001B[48;2;255;255;255mSta\u001B[0m");
    private final String symbol;
    CardType(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    /**
     * Completes the toString method by adding the remaining part of the word.
     * Returns an empty string if invoked for CardType.starter because this method is intended to be used
     * for the decks.
     */
    public String fullString() {
        switch (this){
            case fungi -> {
                return this.toString()+"\u001B[31m\u001B[48;2;255;255;255mgi\u001B[0m";
            }
            case insect -> {
                return this.toString()+"\u001B[35m\u001B[48;2;255;255;255mect\u001B[0m";
            }
            case animal ->
            {
                return this.toString()+"\u001B[34m\u001B[48;2;255;255;255mmal\u001B[0m";
            }
            case plant -> {
                return this.toString()+"\u001B[32m\u001B[48;2;255;255;255mPla\u001B[0m";
            }
        }
        return "";
    }
}
