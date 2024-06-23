package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

/**
 * The AvailablePositionsMessage class contains all the possible positions on which a card could be placed
 * at the moment of the request.
 */
public class AvailablePositionsMessage extends ServerToClientMessage {
    /**
     * The list of possible positions where a card could be placed.
     */
    private final List<Point> positions;

    /**
     * Constructs an AvailablePositionsMessage with the specified positions.
     *
     * @param positions the list of possible positions where a card could be placed.
     */
    public AvailablePositionsMessage(List<Point> positions) {
        this.positions = positions;
    }

    /**
     * Returns the list of possible positions where a card could be placed.
     *
     * @return a list of points representing the possible positions.
     */
    public List<Point> getPositions() {
        return positions;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the AvailablePositionsMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
