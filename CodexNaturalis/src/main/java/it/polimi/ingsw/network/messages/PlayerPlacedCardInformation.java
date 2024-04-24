package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * This class is used to send information about a player's last placement
 */
public class PlayerPlacedCardInformation implements Serializable {
    private final int cardId;
    private final int xPos;
    private final int yPos;
    private final boolean isFacingUp;
    /**
     * parameter used to determine whose field data has been received
     */
    public PlayerPlacedCardInformation(int cardId, int xPos, int yPos, boolean isFacingUp) {
        this.cardId = cardId;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isFacingUp = isFacingUp;
    }
    /**
     *
     * @return the vertical position of the card
     */
    public int getYPos() {
        return yPos;
    }
    /**
     *
     * @return the horizontal position of the card
     */
    public int getXPos() {
        return xPos;
    }
    /**
     *
     * @return the ID of the card
     */
    public int getCardId() {
        return cardId;
    }

    public boolean isFacingUp() {
        return isFacingUp;
    }
}
