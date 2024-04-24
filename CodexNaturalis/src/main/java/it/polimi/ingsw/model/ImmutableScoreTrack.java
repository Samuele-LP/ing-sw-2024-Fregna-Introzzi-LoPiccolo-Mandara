package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable version of the ScoreTrack class, its purpose is to be sent to the client every time there is an update in the ScoreTrack
 */
public class ImmutableScoreTrack {
    private final HashMap<String,Integer> playerPoints;
    //TODO: implement player colour
    public ImmutableScoreTrack(HashMap<String, Integer> playerPoints) {
        this.playerPoints = playerPoints;
    }
    public HashMap<String, Integer> getPlayerPoints() {
        return playerPoints;
    }
    /**
     *  Printing the table_score
     */
    public synchronized void printTable() {
        for (Map.Entry<String, Integer> obj : playerPoints.entrySet()) {
            System.out.println("Player: " + obj.getKey() + ", Points: " + obj.getValue());
        }
    }
}
