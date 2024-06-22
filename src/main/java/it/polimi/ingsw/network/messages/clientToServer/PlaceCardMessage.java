package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message sent that contains information on the player's placing move
 */
public class PlaceCardMessage extends ClientToServerMessage {

    private final int xCoordinate;

    private final int yCoordinate;

    private final boolean isFacingUp;

    private final int ID;

    /**
     * Constructor for the PlaceCardMessage.
     *
     * @param xCoordinate the x coordinate where the card will be placed
     * @param yCoordinate the y coordinate where the card will be placed
     * @param isFacingUp  the direction the card will be facing when placed
     * @param id          the id of the card to be placed
     */
    public PlaceCardMessage(int xCoordinate, int yCoordinate, boolean isFacingUp, int id) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isFacingUp = isFacingUp;
        ID = id;
    }

    /**
     * Returns the chosen x coordinate of the card that will be placed.
     *
     * @return the x coordinate
     */
    public int getX() {
        return xCoordinate;
    }

    /**
     * Returns the chosen y coordinate of the card that will be placed.
     *
     * @return the y coordinate
     */
    public int getY() {
        return yCoordinate;
    }

    /**
     * Returns the direction the card will be facing when placed.
     *
     * @return true if the card is facing up, false otherwise
     */
    public boolean isFacingUp() {
        return isFacingUp;
    }

    /**
     * Returns the id of the card to be placed.
     *
     * @return the card id
     */
    public int getID() {
        return ID;
    }

    /**
     * Executes the message using the given listener and sender.
     *
     * @param lis    the listener to handle the message
     * @param sender the client handler that sent the message
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
