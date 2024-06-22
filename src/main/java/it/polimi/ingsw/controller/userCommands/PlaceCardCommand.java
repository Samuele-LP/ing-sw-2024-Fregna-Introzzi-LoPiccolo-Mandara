package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains information about how the player wants to place a specific card.
 */
public class PlaceCardCommand extends UserCommand {

    private final int xPosition;
    private final int yPosition;
    private final int cardID;
    private final boolean isFacingUP;

    /**
     * Constructor for PlaceCardCommand.
     *
     * @param xPosition  the x-coordinate where the card will be placed
     * @param yPosition  the y-coordinate where the card will be placed
     * @param isFacingUP whether the card is facing up (true) or down (false)
     * @param cardID     the ID of the card to be placed
     */
    public PlaceCardCommand(int xPosition, int yPosition, boolean isFacingUP, int cardID) {
        this.cardID = cardID;
        this.isFacingUP = isFacingUP;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     * @return the x-coordinate where the card will be placed
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * @return the y-coordinate where the card will be placed
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * @return the ID of the card to be placed
     */
    public int getCardID(){
        return cardID;
    }

    /**
     * @return true if the card is facing up, false if it is facing down
     */
    public boolean isFacingUP(){
        return isFacingUP;
    }

    /**
     * Sends the command to the specified UserListener, providing information on how the player wants to place a specific card.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
