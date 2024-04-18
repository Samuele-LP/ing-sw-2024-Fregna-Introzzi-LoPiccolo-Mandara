package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.io.Serializable;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements Serializable, Runnable {

    public int numPlayers;

    private Game game;

    /**
     * Create new game
     *
     * NB: PER GESTIRE PIU' PARTITE, AGGIUNGERE INDENTIFICATORE UNIVOCO/INCREMENTALE (?) AD OGNI PARTITA ???
     */
    public GameController(){
        game = new Game();  //cambiare come vengono gestiti i player aggiunti alla partita ??????

    }

    @Override
    public void run() {
        //
    }

    /**
     * Forse potrebbe risultare più comodo e sensato mettere in GameControlle invece che in Game questo metodo (?)
     */
    public void addPlayer(){
        //
    }

    /**
     * Used by a player to leave a game voluntarily
     */
    public void leaveGame(){
        //
    }

}
