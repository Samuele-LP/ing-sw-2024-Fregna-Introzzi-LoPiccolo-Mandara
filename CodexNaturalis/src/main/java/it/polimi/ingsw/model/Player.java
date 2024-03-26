package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a player
 * */
public class Player{
    private final String name;  //Attribute that can't be changed (or should we add this option?), reffering to the declared name of the player
    private int currentPoints; //Attribute used to keep track of current points
    private int currentTurn;   //Attribute used to keep track of the current turn
    private ObjectiveCard secretObjective;
    private List<PlayableCard> personalHandCards; //Attribute for listing actual cards in a players hand
    public Player(String name){
        this.name = name;
        currentPoints=0;
        currentTurn=0;
    }

    /**
     * Puts received card in player's hand,this method is called only if there are two cards in the player's hand
     * @param card
     */
    public void receiveDrawnCard(PlayableCard card){
        personalHandCards.add(card);
    }

    public void printAvailableCornerCoords() throws NotPlacedException {
        // Dubbio: come mostrare le carte (e quindi gli angoli disponibili) senza la GUI ?
    }

    private void placeCard(PlayableCard card, int xCoordinate, int yCoordinate){
        // Assignment of the coordinates of a playableCard
    }

    private boolean isPlacingPointValid(Point point, int chosenCorner){
        // Check if in a point (X,Y) there is an empty available corner of a card

        return false; // return false if position is invalid
    }

    private int getPoints(){
        return currentPoints;
    }

    // Select one of the two possible personale secret objective

    /**
     * Sets the secret objective after it has been chosen
     * @param secretObjective
     */
    private void setSecretObjective(ObjectiveCard secretObjective){
        this.secretObjective=secretObjective;
    }

    // Calculate points for current player given from the common objective
    private int calculateCommonObjectiveGivenPoint(ObjectiveCard objective){

        return (-1);
    }

    private int calculatePointsOnPlace(PlayableCard card, Point placedIn) throws NotPlacedException {
        //Check if card conditions are easy or hard type

        //Add points on easy condition (the one for number of TokenType)

        //Add points on hard condition (the one for objective of card combinations)

        return (-1);
    }

    // Check if the number of cards in the current hand is valid (in order to check if other parts of the code made some mistakes)
    private int quantityOfCards(){
        return personalHandCards.size();
    }
}
