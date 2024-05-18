package it.polimi.ingsw.model;

import it.polimi.ingsw.network.commonData.ConstantValues;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.HashMap;
import java.util.Map;

/**
 *Class that represents the game's physical Score Track. It's updated by using the observer design pattern
 */
public class ScoreTrack {
    /**
     * The key is the player's name, the value is the player's colour expressed as ansi escape codes
     */
    private final HashMap<String, String> playersColor = new HashMap<>();
    private boolean isFinalPhase;

    HashMap<String, Integer> players = new HashMap<>();

    /**
     *
     * scoreTrack if there are two players
     *
     * @param name1: first player name
     * @param name2: second player name
     */
    public ScoreTrack(String name1, String name2){
        isFinalPhase=false;
        players.put(name1, 0);
        players.put(name2, 0);
    }

    /**
     *
     * scoreTrack if there are three players in the game
     *
     * @param name1 first player username
     * @param name2 second player username
     * @param name3 third player username
     */
    public ScoreTrack(String name1, String name2, String name3){
        isFinalPhase=false;
        players.put(name1, 0);
        players.put(name2, 0);
        players.put(name3, 0);
    }

    /**
     *
     * scoreTrack if there are four players in the game
     *
     * @param name1 first player username
     * @param name2 second player username
     * @param name3 third player username
     * @param name4 fourth player username
     */
    public ScoreTrack(String name1, String name2, String name3, String name4){
        isFinalPhase=false;
        players.put(name1, 0);
        players.put(name2, 0);
        players.put(name3, 0);
        players.put(name4, 0);
    }


    /**
     * this method sets tha player's pawn colour
     * @param playerName is the player's username
     * @param chosenColour is the pawn colour that the player chose
     */
    public void setPawnColor(String playerName, String chosenColour){
        playersColor.put(playerName,chosenColour);

    }

    /**
     *Copies the scoreTrack in another object with methods to view the information, to be sent to the client
     * @return an immutable version of the scoreTrack
     */
    public synchronized ImmutableScoreTrack copyScoreTrack(){
        return new ImmutableScoreTrack(new HashMap<>(players),new HashMap<>(playersColor));
    }
    /**
     *
     * This method associates every player to his points on the scoreTrack
     *
     * @param name player username
     * @param points points that the player scored
     */
    public synchronized void updateScoreTrack(String name, int points){
        players.put(name,points);
        if(points>=20){
            isFinalPhase=true;
        }
    }

    /**
     * Method used to determine if the game ha started its final phase on the condition that a player has reached 20 points
     * @return true when the final phase has started, false otherwise
     */
    public synchronized boolean doesFinalPhaseStart(){
        return isFinalPhase;
    }
}