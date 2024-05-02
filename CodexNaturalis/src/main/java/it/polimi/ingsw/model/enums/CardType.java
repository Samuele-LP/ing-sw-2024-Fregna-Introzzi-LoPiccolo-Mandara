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
}
