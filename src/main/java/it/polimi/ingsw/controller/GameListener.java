package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;

public interface GameListener {

    /**
     *
     * @param p1 is the player added to the lobby
     */
    void addedPlayer(Player p1);


    void gameStarted();

    /**
     *
     */
    void turnChanged();

    /**
     *
     */
    void roundStarted();
    /**
     *
     */
    void finalPhaseStarted();


    /**
     *
     */
    void secretObjectiveChosen();

    /**
     *
     */
    void cardPlaced();

    /**
     *
     */
    void cardDrawn();

    

}
