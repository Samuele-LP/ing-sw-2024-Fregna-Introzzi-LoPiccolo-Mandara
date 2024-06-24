package it.polimi.ingsw.model.enums;

/**
 * Used to determine the colour of a card:<br>
 * fungi<br>insect<br>animal<br>plant<br>starter
 */
public enum CardType {
    fungi("\u001B[31m\u001B[49mFun\u001B[0m"),
    insect("\u001B[35m\u001B[49mIns\u001B[0m"),
    animal("\u001B[34m\u001B[49mAni\u001B[0m"),
    plant("\u001B[32m\u001B[49mPla\u001B[0m")
    ,starter("\u001B[33m\u001B[49mSta\u001B[0m");
    private final String symbol;
    CardType(String symbol) {
        this.symbol = symbol;
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
                return this.toString() + "\u001B[31m\u001B[49mgi \u001B[0m";
            }
            case insect -> {
                return this.toString() + "\u001B[35m\u001B[49mect\u001B[0m";
            }
            case animal ->
            {
                return this.toString() + "\u001B[34m\u001B[49mmal\u001B[0m";
            }
            case plant -> {
                return this.toString() + "\u001B[32m\u001B[49mnt \u001B[0m";
            }
        }
        return "";
    }
}
