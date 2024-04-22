package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 *Class that represents the game's physical Score Track. It's updated by using the observer design pattern
 */
public class ScoreTrack {
    private enum color{red,green,blue,black}  /* color of each pawn */
    private int Num_player;
    private Game game;

    Map<String, Integer> gamers = new HashMap<>();

    /**
     *
     * scoreTrack if there are two players
     *
     * @param name1: first player name
     * @param name2: second player name
     */
    public ScoreTrack(String name1, String name2, Game game){
        this.Num_player = 2;
        gamers.put(name1, 0);
        gamers.put(name2, 0);
        this.game = game;
    }

    /**
     *
     * scoreTrack if there are three players in the game
     *
     * @param nome1 first player username
     * @param nome2 second player username
     * @param nome3 third player username
     */
    public ScoreTrack(String nome1, String nome2, String nome3, Game game){
        this.Num_player = 3;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
        this.game = game;
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
    public ScoreTrack(String nome1, String nome2, String nome3, String nome4, Game game){
        this.Num_player = 4;
        gamers.put(nome1, 0);
        gamers.put(nome2, 0);
        gamers.put(nome3, 0);
        gamers.put(nome4, 0);
        this.game = game;
    }

    /**
     *
     * @return
     */
    public synchronized ScoreTrack copyScoreTrack(){
        String[] names =new String[gamers.keySet().size()];
        int i=0;
        for(String name: gamers.keySet()){
            names[i]=name;
            i++;
        }
        if(Num_player==2){
            return new ScoreTrack(names[0],names[1], game);
        }
        if(Num_player==3){
            return new ScoreTrack(names[0],names[1],names[2], game);
        }
        if(Num_player==4){
            return new ScoreTrack(names[0],names[1],names[2],names[3], game);
        }
        return null;
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

        int currPoints = gamers.get(name);
        if(currPoints>=20)
            game.gameOver();
    }

    /**
     *  Printing the table_score
     */
    public synchronized void printTable() {
        for (Map.Entry<String, Integer> obj : gamers.entrySet()) {
            System.out.println("Player: " + obj.getKey() + ", Points: " + obj.getValue());
        }
    }
}