package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.messages.Message;

import java.util.List;

/**
 * Contains all the possible positions on which a card could be placed at the moment of the request
 */
public class AvailablePositionsMessage extends Message {
    //TODO: maybe also get the visible symbols?
    private final List<Point> positions;
    public AvailablePositionsMessage(List<Point> positions) {
        this.positions = positions;
    }

    /**
     * @return an ArrayList containing the points on which a card could be placed, in no particular order
     */
    public List<Point> getPositions() {
        return positions;
    }
}
