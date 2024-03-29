package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player
 * */
public class Player{
    private final String name;  //Attribute that can't be changed (or should we add this option?), reffering to the declared name of the player
    private int currentPoints; //Attribute used to keep track of current points
    private int currentTurn;   //Attribute used to keep track of the current turn
    private ObjectiveCard secretObjective;
    private PlayingField playingField;
    private final PlayableCard startingCard;
    private List<PlayableCard> personalHandCards; //Attribute for listing actual cards in a players hand
    public Player(String name,PlayableCard startingCard)throws  IllegalStartingCradException{
        this.name = name;
        currentPoints=0;
        currentTurn=0;
        if(startingCard.getID()<=86&&startingCard.getID()>=81){
            throw new IllegalStartingCradException();
        }
        this.startingCard=startingCard;
        playingField=new PlayingField();
    }

    /**
     * Getter for the player's name
     * @return the player's name
     */
    public String getName(){
        return name;
    }

    /**
     * Getter method for the player's points
     * @return the player's current points
     */
    public int getPoints(){
        return currentPoints;
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
     * @param card to be placed on (xCoordinate,yCoordinate)
     * @param xCoordinate
     * @param yCoordinate
     * @throws CardNotInHand if the card chosen isn't among the player's current hand
     * @throws NotEnoughResourcesException if the requirements to play the gold card are not met
     * @throws InvalidPositionException if the chosen point isn't valid
     * @throws AlreadyPlacedException if the chosen card has already been placed by one of the players
     */
    public void placeCard(PlayableCard card, int xCoordinate, int yCoordinate,boolean isFacingUp) throws CardNotInHand, NotEnoughResourcesException, InvalidPositionException, AlreadyPlacedException, NotPlacedException {
        if(!personalHandCards.contains(card)){
            throw new CardNotInHand();
        }
        Point position= new Point(xCoordinate,yCoordinate);
        if(!isPlacingPointValid(position)){
            throw new InvalidPositionException();
        }
        if(card.getID()<=80 && card.getID()>=41) {
            if(!playingField.isGoldCardPlaceable(card)) throw new NotEnoughResourcesException();
        }
        card.placeCard(position,currentTurn,isFacingUp);
        personalHandCards.remove(card);
        playingField.addPlacedCard(card);
    }

    /**Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     * @param point - point (z,w) in which the card will be placed if it is a valid position. Every point such that z+w is odd will be rejected because it's like saying that the player wants to cover two corners of a same card.
     * @return return false if position is invalid
     */
    private boolean isPlacingPointValid(Point point) throws NotPlacedException {
        if(point.getX()+point.getY()%2!=0){
            return false;
        }
        return playingField.isPositionAvailable(point);
    }

    /**
     * Sets the secret objective after it has been chosen
     * @param secretObjective
     */
    public void setSecretObjective(ObjectiveCard secretObjective){
        this.secretObjective=secretObjective;
    }
    public void placeStartingCard(boolean isFacingUp)throws AlreadyPlacedException{
        startingCard.placeCard(new Point(0,0),0,isFacingUp);
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
        List<PlayableCard> hand = new ArrayList<>(personalHandCards);
        return hand;
    }
}
