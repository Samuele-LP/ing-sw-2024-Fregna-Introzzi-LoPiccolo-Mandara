package it.polimi.ingsw.view;

import it.polimi.ingsw.network.commonData.ConstantValues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable version of the ScoreTrack class, its purpose is to be sent to the client every time there is an update in the ScoreTrack
 */
public class ImmutableScoreTrack implements Serializable {
    private final HashMap<String, Integer> playerPoints;
    private final HashMap<String, String> playerColours;

    public ImmutableScoreTrack(HashMap<String, Integer> playerPoints, HashMap<String, String> playerColours) {
        this.playerPoints = playerPoints;
        if(playerColours==null) this.playerColours= new HashMap<>();
        else this.playerColours = playerColours;
    }

    public HashMap<String, String> getColours(){
        return new HashMap<>(playerColours);
    }

    public HashMap<String, Integer> getPlayerPoints() {
        return playerPoints;
    }

    /**
     * Prints the current scoreTrack, as a CLI element
     */
    public synchronized ArrayList<String> printTable(){
        ArrayList<String> lines = new ArrayList<>();

        int longestName = -1;
        for (String s : playerPoints.keySet()) {
            if (s.length() > longestName) {
                longestName = s.length();
            }
        }//123456 123456
        longestName = Math.max("ScoreTrack".length(), longestName);
        String separator = "|" + "-".repeat((longestName * 2)) + "-|";
        lines.add(separator);
        String title = "|" + " ".repeat(separator.length() / 2 - "ScoreTrack".length() / 2) + "ScoreTrack";
        title = title + " ".repeat(2 * longestName - title.length()) + "  |";
        lines.add(title);
        lines.add(separator);
        lines.add("|Player" + " ".repeat(longestName - "Player".length()) + "|Points" + " ".repeat(longestName - "Player".length()) + "|");
        lines.add(separator);
        for (Map.Entry<String, Integer> entry : playerPoints.entrySet().stream().sorted((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            } else if (o1.getValue() < o2.getValue()) {
                return 1;
            } else {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey(), o2.getKey());
            }
        }).toList()) {
            lines.add("|" +playerColours.getOrDefault(entry.getKey(), "")
                    + entry.getKey()+ ConstantValues.ansiEnd + " ".repeat(longestName - entry.getKey().length()) +
                    "|" + entry.getValue() + " ".repeat(longestName - entry.getValue().toString().length()) + "|");
        }
        lines.add(separator);
        return lines;
    }

    /**
     * Method that will contain the necessary information to print the scoreTrack as a GUI element
     */
    public synchronized void showInGUI(){

    }
}
