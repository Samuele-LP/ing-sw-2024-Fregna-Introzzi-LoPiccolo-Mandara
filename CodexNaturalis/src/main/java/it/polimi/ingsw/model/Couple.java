package it.polimi.ingsw.model;

public class Couple {
    private final int cardID;
    private final boolean isFacingUp;

    public Couple(int cardID, boolean isFacingUp) {
        this.cardID = cardID;
        this.isFacingUp = isFacingUp;
    }

    public boolean isFacingUp() {
        return isFacingUp;
    }

    public int getCardID() {
        return cardID;
    }
}
