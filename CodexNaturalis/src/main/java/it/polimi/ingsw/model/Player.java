package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

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
    private PlayingField playingField;
    private List<PlayableCard> personalHandCards; //Attribute for listing actual cards in a players hand
    public Player(String name){
        this.name = name;
        currentPoints=0;
        currentTurn=0;
        playingField=new PlayingField();
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
    /**
     *Places the card in the player's area
     * @param card to be placed
     * @param xCoordinate
     * @param yCoordinate
     * @throws InvalidCardException if the card chosen isn't among the player's current hand
     * @throws NotEnoughResourcesException if the requirements to play the gold card are not met
     * @throws InvalidPositionException if the chosen point isn't valid
     * @throws AlreadyPlacedException if the chosen card has already been placed by one of the players
     */
    public void placeCard(PlayableCard card, int xCoordinate, int yCoordinate)throws InvalidCardException,NotEnoughResourcesException,InvalidPositionException,AlreadyPlacedException{
        if(!personalHandCards.contains(card)){
            throw new InvalidCardException();
        }
        Point position= new Point(xCoordinate,yCoordinate);
        if(!isPlacingPointValid(position)){
            throw new InvalidPositionException();
        }
        //Check for gold card resources will be done here
        card.placeCard(position,currentTurn);
        personalHandCards.remove(card);
    }
    /**Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     * @param point - point (z,w) in which the card will be placed if it is a valid position. Every point such that z+w is odd will be rejected because it's like saying that the player wants to cover two corners of a same card.
     * @return return false if position is invalid
     */
    private boolean isPlacingPointValid(Point point){
        return false;
    }
    private int getPoints(){
        return currentPoints;
    }

    /**
     * Sets the secret objective after it has been chosen
     * @param secretObjective
     */
    public void setSecretObjective(ObjectiveCard secretObjective){
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
    /**
     *  Used to check if the number of cards in the current hand is valid (in order to check if other parts of the code made some mistakes)
     * @return size of the player's hand
     */
    private int quantityOfCards(){
        return personalHandCards.size();
    }
    /**
     * @return a copy of the player's hand, to be seen by the client
     */
    public List<PlayableCard> viewCurrentHand(){
        List<PlayableCard> hand = new ArrayList<>();
        hand.addAll(personalHandCards);
        return hand;
    }
}
