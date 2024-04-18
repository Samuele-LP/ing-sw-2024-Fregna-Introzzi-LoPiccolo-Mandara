package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Controller of multiple GameControllers. Used in order to manage multiple games from a single player
 * NB: THIS IS A ADVANCED FUNCTIONALITY
 */
public class AllGamesController implements Serializable {
    /**
     * List of joined games
     */
    private List<GameController> games;

    public AllGamesController(){
        games = new ArrayList<>();
    }

    /**
     * Create a new game
     * @return
     */
    public GameController createGame(){
        GameController createdGame = new GameController();

        return createdGame;
    }

    /**
     * Used when a player wants to close the app (ie, exiting alla games)
     */
    public void leaveAllGames(){
        //
    }

}
