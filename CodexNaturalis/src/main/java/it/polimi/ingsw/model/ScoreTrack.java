package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ScoreTrack {
    private enum color{red,green,blue,black}  /* color of each pawn */

    /**
     *          Points
     */
    private int Num_player;
    Map<String, Integer> gamers = new HashMap<>();
    /**
     *
     * @param name1: name_first_player
     * @param name2: name_second_player
     */
    ScoreTrack(String name1, String name2){
        this.Num_player = 2;
        gamers.put(name1, 0);
        gamers.put(name2, 0);
    }

    /**
     *
     * @param nome1
     * @param nome2
     * @param nome3
     */
    ScoreTrack(String nome1, String nome2, String nome3){
        this.Num_player = 3;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
    }

    /**
     *
     * @param nome1
     * @param nome2
     * @param nome3
     * @param nome4
     */
    ScoreTrack(String nome1, String nome2, String nome3, String nome4){
        this.Num_player = 4;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
        gamers.put(nome4, 0);
    }
    /**
     *
     * @param name
     * @param POINTS
     */
    public void updateScoreTrack(String name, int POINTS){
        int val = gamers.get(name);
        gamers.put(name,val+POINTS);
    }

    /**
     *          Printing the table_score
     */
    public void printTable() {
        for (Map.Entry<String, Integer> obj : gamers.entrySet()) {
            System.out.println("Player: " + obj.getKey() + ", Points: " + obj.getValue());
        }
    }
}