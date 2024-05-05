package it.polimi.ingsw.model.enums;

/**
 * Enum used to represent the nine possible Tokens that can appear as a card corner or, in case of a starting card, in the center of the card
 */
public enum TokenType {
    empty("\u001B[48;2;255;255;255m[ ]\u001B[0m"),
    blocked("\u001B[30m\u001B[48;2;255;255;255m[X]\u001B[0m"),
    fungi("\u001B[31m\u001B[48;2;255;255;255mFun\u001B[0m"),
    insect("\u001B[35m\u001B[48;2;255;255;255mIns\u001B[0m"),
    animal("\u001B[34m\u001B[48;2;255;255;255mAni\u001B[0m"),
    plant("\u001B[32m\u001B[48;2;255;255;255mPla\u001B[0m"),
    scroll("Scr")
    ,quill("Qui")
    ,ink("Ink");
    private final String symbol;
    TokenType(String symbol) {
        this.symbol=symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
