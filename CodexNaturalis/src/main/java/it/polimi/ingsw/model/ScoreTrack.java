package it.polimi.ingsw.model;

import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.HashMap;
import java.util.Map;

/**
 *Class that represents the game's physical Score Track. It's updated by using the observer design pattern
 */
public class ScoreTrack {
    private enum color{red,green,blue,black}  /* color of each pawn */
    private final int Num_player;
    private ImmutableScoreTrack immutableScoreTrack ;
    private boolean isFinalPhase;

    Map<String, Integer> gamers = new HashMap<>();

    /**
     *
     * scoreTrack if there are two players
     *
     * @param name1: first player name
     * @param name2: second player name
     */
    public ScoreTrack(String name1, String name2){
        isFinalPhase=false;
        this.Num_player = 2;
        gamers.put(name1, 0);
        gamers.put(name2, 0);
        immutableScoreTrack=new ImmutableScoreTrack(new HashMap<>(gamers));
    }

    /**
     *
     * scoreTrack if there are three players in the game
     *
     * @param nome1 first player username
     * @param nome2 second player username
     * @param nome3 third player username
     */
    public ScoreTrack(String nome1, String nome2, String nome3){
        isFinalPhase=false;
        this.Num_player = 3;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
        immutableScoreTrack=new ImmutableScoreTrack(new HashMap<>(gamers));
    }

    /**
     *
     * scoreTrack if there are four players in the game
     *
     * @param nome1 first player username
     * @param nome2 second player username
     * @param nome3 third player username
     * @param nome4 fourth player username
     */
    public ScoreTrack(String nome1, String nome2, String nome3, String nome4){
        isFinalPhase=false;
        this.Num_player = 4;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
        gamers.put(nome4, 0);
        immutableScoreTrack=new ImmutableScoreTrack(new HashMap<>(gamers));
    }

    /**
     *Copies the scoreTrack in another object with methods to view the information, to be sent to the client
     * @return an immutable version of the scoreTrack
     */
    public synchronized ImmutableScoreTrack copyScoreTrack(){
        return immutableScoreTrack;
    }
    /**
     *
     * This method associates every player to his points on the scoreTrack
     *
     * @param name player username
     * @param POINTS points that the player scored
     */
    public synchronized void updateScoreTrack(String name, int POINTS){
        gamers.put(name,POINTS);
        immutableScoreTrack=new ImmutableScoreTrack(new HashMap<>(gamers));
        int currPoints = gamers.get(name);
        if(currPoints>=20){
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