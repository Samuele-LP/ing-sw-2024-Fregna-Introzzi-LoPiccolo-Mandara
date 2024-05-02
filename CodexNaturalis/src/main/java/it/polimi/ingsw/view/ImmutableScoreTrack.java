package it.polimi.ingsw.view;

import java.util.Comparator;
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
     * Prints the current scoreTrack
     */
    public synchronized void printTable() {
        System.out.println();
        System.out.println("ScoreTrack:");
        for (Map.Entry<String, Integer> obj : playerPoints.entrySet().stream().sorted((o1, o2) -> {
            if(o1.getValue()>o2.getValue()){
                return -1;
            }
            else if(o1.getValue()<o2.getValue()){
                return 1;
            }else {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey(),o2.getKey());
            }
        }).toList()) {
            System.out.println("Player: " + obj.getKey() + ", Points: " + obj.getValue());
        }
        System.out.println();
    }
}
