package it.polimi.ingsw.network.messages.serverToClient;

import java.io.Serializable;

/**
 * This class is used to send information about a player's last placement
 */
public class PlayerPlacedCardInformation implements Serializable {
    private final int cardId;
    private final int xPos;
    private final int yPos;
    /**
     * parameter used to determine whose field data has been received
     */

    public PlayerPlacedCardInformation(int cardId, int xPos, int yPos) {
        this.cardId = cardId;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public int getYPos() {
        return yPos;
    }
    public int getXPos() {
        return xPos;
    }
    public int getCardId() {
        return cardId;
    }
}
