package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Message sent that contains information on the player's placing move
 */
public class PlaceCardMessage extends ClientToServerMessage {
    private final int xCoordinate;
    private final int yCoordinate;
    private final boolean isFacingUp;
    private final int ID;

    public PlaceCardMessage(int xCoordinate, int yCoordinate, boolean isFacingUp, int id) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isFacingUp = isFacingUp;
        ID = id;
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

    /**
     *
     * @return the id of the card to be placed
     */
    public int getID() {
        return ID;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }
}
