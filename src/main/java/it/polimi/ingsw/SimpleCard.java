package it.polimi.ingsw;

import java.io.Serializable;

/**
 * Class used to memorize how a card has been placed in the view.
 */
public class SimpleCard implements Serializable {

    private final int x;

    private final int y;

    private final boolean isFacingUp;

    private final int ID;

    /**
     * Constructor for the SimpleCard class.
     *
     * @param id        the ID of the card
     * @param x         the x-coordinate of the card's position
     * @param y         the y-coordinate of the card's position
     * @param isFacingUp whether the card is facing up or down
     */
    public SimpleCard(int id,int x, int y, boolean isFacingUp) {
        this.x = x;
        this.y = y;
        this.isFacingUp = isFacingUp;
        ID = id;
    }

    /**
     * Gets the x-coordinate of the card's position.
     *
     * @return the x-coordinate of the card's position
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the card's position.
     *
     * @return the y-coordinate of the card's position
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if the card is facing up.
     *
     * @return true if the card is facing up, false otherwise
     */
    public boolean isFacingUp() {
        return isFacingUp;
    }

    /**
     * Gets the ID of the card.
     *
     * @return the ID of the card
     */
    public int getID() {
        return ID;
    }

    /**
     * Checks if this SimpleCard is equal to another object.
     * Two SimpleCard objects are considered equal if they have the same ID.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (obj instanceof SimpleCard other) {
            return this.ID == other.ID;
        }

        return super.equals(obj);
    }

    /**
     * Returns a hash code value for the object.
     * The hash code is based on the ID of the card.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode(){
        return ID;
    }
}
