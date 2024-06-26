package it.polimi.ingsw.model;

import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.HashMap;

/**
 * Class that represents the game's physical Score Track. It's updated using the observer design pattern.
 */
public class ScoreTrack {

    /**
     * The key is the player's name, the value is the player's color expressed as ANSI escape codes.
     */
    private final HashMap<String, String> playersColor = new HashMap<>();

    private boolean isFinalPhase;

    private final HashMap<String, Integer> players = new HashMap<>();

    /**
     * Constructor for ScoreTrack when there are two players.
     *
     * @param name1 first player's name
     * @param name2 second player's name
     */
    public ScoreTrack(String name1, String name2) {
        isFinalPhase = false;
        players.put(name1, 0);
        players.put(name2, 0);
    }

    /**
     * Constructor for ScoreTrack when there are three players.
     *
     * @param name1 first player's name
     * @param name2 second player's name
     * @param name3 third player's name
     */
    public ScoreTrack(String name1, String name2, String name3) {
        isFinalPhase = false;
        players.put(name1, 0);
        players.put(name2, 0);
        players.put(name3, 0);
    }

    /**
     * Constructor for ScoreTrack when there are four players.
     *
     * @param name1 first player's name
     * @param name2 second player's name
     * @param name3 third player's name
     * @param name4 fourth player's name
     */
    public ScoreTrack(String name1, String name2, String name3, String name4) {
        isFinalPhase = false;
        players.put(name1, 0);
        players.put(name2, 0);
        players.put(name3, 0);
        players.put(name4, 0);
    }

    /**
     * Sets the player's pawn color.
     *
     * @param playerName   the player's name
     * @param chosenColour the pawn color chosen by the player
     */
    public void setPawnColor(String playerName, String chosenColour) {
        playersColor.put(playerName, chosenColour);

    }

    /**
     * Copies the scoreTrack into another object with methods to view the information, to be sent to the client.
     *
     * @return an immutable version of the scoreTrack
     */
    public synchronized ImmutableScoreTrack copyScoreTrack() {
        return new ImmutableScoreTrack(new HashMap<>(players), new HashMap<>(playersColor));
    }

    /**
     * Updates the score track with the player's points.
     *
     * @param name   the player's name
     * @param points the points scored by the player
     */
    public synchronized void updateScoreTrack(String name, int points) {
        players.put(name, points);
        if (points >= 20) {
            isFinalPhase = true;
        }
    }

    /**
     * Determines if the game has started its final phase based on the condition that a player has reached 20 points.
     *
     * @return true if the final phase has started, false otherwise
     */
    public synchronized boolean doesFinalPhaseStart() {
        return isFinalPhase;
    }
}