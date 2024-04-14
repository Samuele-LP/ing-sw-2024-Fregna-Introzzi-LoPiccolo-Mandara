package it.polimi.ingsw;

public class Triplet {
    private final int cardID;
    private final boolean isFacingUp;
    private final Point position;
    public Triplet(int cardID, boolean isFacingUp, Point position) {
        this.cardID = cardID;
        this.isFacingUp = isFacingUp;
        this.position = position;
    }

    public boolean isFacingUp() {
        return isFacingUp;
    }

    public int getCardID() {
        return cardID;
    }

    public Point getPosition() {
        return position;
    }
}
