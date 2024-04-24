package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.clientToServer.ChosenSecretObjectiveMessage;
import it.polimi.ingsw.network.messages.clientToServer.DrawCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.StartGameMessage;

/**
 * The message listener interface contains methods that are necessary to pass messages to controller to manage the game logic
 */
public interface MessageListener {

    /**
     * @param mes is the message containing infos about the card the player wants to draw
     */
    void handleDrawCardMessage(DrawCardMessage mes);

    /**
     * @param mes is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     */
    void handlePlaceCardMessage(PlaceCardMessage mes);

    /**
     * @param mes is the message with the secretObjective card the player chose between the twos dealt
     */
    void handleChosenSecretObjectiveMessage(ChosenSecretObjectiveMessage mes);

    /**
     * @param mes that allows the starting of the game
     */
    void handleStartGameMessage(StartGameMessage mes);

}
