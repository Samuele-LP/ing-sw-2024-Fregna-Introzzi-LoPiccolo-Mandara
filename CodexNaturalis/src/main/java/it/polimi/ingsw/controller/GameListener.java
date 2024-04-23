package it.polimi.ingsw.controller;

public interface GameListener {

    /**
     *
     */
    void addedPlayer();

    /**
     *
     */
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
