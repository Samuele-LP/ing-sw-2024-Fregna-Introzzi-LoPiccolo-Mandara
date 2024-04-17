package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.messages.Message;

import java.util.List;

/**
 * Contains all the possible positions on which a card could be placed at the moment of the request
 */
public class AvailablePositionsMessage extends Message {
    private final List<Point> positions;
    public AvailablePositionsMessage(List<Point> positions) {
        this.positions = positions;
    }
    public List<Point> getPositions() {
        return positions;
    }
}
