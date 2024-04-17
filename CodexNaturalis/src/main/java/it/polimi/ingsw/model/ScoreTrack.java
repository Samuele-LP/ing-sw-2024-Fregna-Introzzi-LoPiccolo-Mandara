package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 *Class that represents the game's physical Score Track. It's updated by using the observer design pattern
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

    public synchronized ScoreTrack copyScoreTrack(){
        String[] names =new String[gamers.keySet().size()];
        int i=0;
        for(String name: gamers.keySet()){
            names[i]=name;
            i++;
        }
        if(Num_player==2){
            return new ScoreTrack(names[0],names[1]);
        }
        if(Num_player==3){
            return new ScoreTrack(names[0],names[1],names[2]);
        }
        if(Num_player==4){
            return new ScoreTrack(names[0],names[1],names[2],names[3]);
        }
        return null;
    }
    /**
     *
     * @param name
     * @param POINTS
     */
    public synchronized void updateScoreTrack(String name, int POINTS){
        gamers.put(name,POINTS);
    }

    /**
     *          Printing the table_score
     */
    public synchronized void printTable() {
        for (Map.Entry<String, Integer> obj : gamers.entrySet()) {
            System.out.println("Player: " + obj.getKey() + ", Points: " + obj.getValue());
        }
    }
}