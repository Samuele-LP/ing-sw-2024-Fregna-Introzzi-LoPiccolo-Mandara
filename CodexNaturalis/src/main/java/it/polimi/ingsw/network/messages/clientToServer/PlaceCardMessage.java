package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message sent that contains information on the player's placing move
 */
public class PlaceCardMessage extends Message {
    private final int xCoordinate;
    private final int yCoordinate;
    private final boolean isFacingUp;

    public PlaceCardMessage(int xCoordinate, int yCoordinate, boolean isFacingUp) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isFacingUp = isFacingUp;
    }

    /**
     *
     * @return chosen x coordinate of the card that will be placed
     */
    public int getX() {
        return xCoordinate;
    }

    /**
     *
     * @return chosen y coordinate of the card that will be placed
     */
    public int getY() {
        return yCoordinate;
    }

    /**
     *
     * @return direction the card will be facing when placed
     */
    public boolean isFacingUp() {
        return isFacingUp;
    }
}
