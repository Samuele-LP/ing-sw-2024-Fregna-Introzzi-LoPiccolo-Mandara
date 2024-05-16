package it.polimi.ingsw.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable version of the ScoreTrack class, its purpose is to be sent to the client every time there is an update in the ScoreTrack
 */
public class ImmutableScoreTrack implements Serializable {
    private final HashMap<String, Integer> playerPoints;

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
    public synchronized ArrayList<String> printTable() {
        ArrayList<String> lines = new ArrayList<>();
        int longestName = -1;
        for (String s : playerPoints.keySet()) {
            if (s.length() > longestName) {
                longestName = s.length();
            }
        }//123456 123456
        longestName = Math.max("ScoreTrack".length(), longestName);
        String separator = "|" + "-".repeat((longestName*2) )+"-|";
        lines.add(separator);
        String title="|"+" ".repeat(separator.length()/2-"ScoreTrack".length()/2)+"ScoreTrack";
        title=title+" ".repeat(2*longestName-title.length())+"  |";
        lines.add(title);
        lines.add(separator);
        lines.add("|Player" + " ".repeat(longestName - "Player".length()) + "|Points" + " ".repeat(longestName - "Player".length())+"|");
        lines.add(separator);
        for (Map.Entry<String, Integer> obj : playerPoints.entrySet().stream().sorted((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            } else if (o1.getValue() < o2.getValue()) {
                return 1;
            } else {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey(), o2.getKey());
            }
        }).toList()) {
            lines.add("|"+obj.getKey()+ " ".repeat(longestName - obj.getKey().length())+
                    "|"+obj.getValue()+" ".repeat(longestName - obj.getValue().toString().length())+"|");
        }
        lines.add(separator);
        return lines;
    }
}
