package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.SimpleCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a player with their points, their name, their playing field and their current hand
 */
public class Player{
    private final String name;
    private int currentPoints;
    private final Object pointsLock;
    private int currentTurn;
    private int numberOfScoredObjectives;
    private ObjectiveCard secretObjective;
    private final PlayingField playingField;
    private final PlayableCard startingCard;
    private final List<PlayableCard> personalHandCards; //Attribute for listing actual cards in a players hand

    /**
     * @param name         player's name
     * @param startingHand the initial 2 resource + 1 gold cards that a player receives when starting a game
     * @throws IllegalStartingCardException if the card is not a starting card
     */
    public Player(String name, PlayableCard startingCard, PlayableCard[] startingHand) throws IllegalStartingCardException {
        this.name = name;
        currentPoints = 0;
        pointsLock = new Object();
        numberOfScoredObjectives = 0;
        currentTurn = 0;
        personalHandCards= new ArrayList<>();
        personalHandCards.add(startingHand[0]);
        personalHandCards.add(startingHand[1]);
        personalHandCards.add(startingHand[2]);
        if(!(startingCard.getID() <= 86 && startingCard.getID() >= 81)){
            throw new IllegalStartingCardException();
        }
        this.startingCard = startingCard;
        playingField = new PlayingField();
        secretObjective = null;
    }

    /**
     * Constructor used to make a backup of a player's field
     */
    private Player(Player other){
        pointsLock= new Object();
        this.name=other.name;
        this.currentPoints= other.currentPoints;
        this.currentTurn=other.currentTurn;
        this.numberOfScoredObjectives=other.numberOfScoredObjectives;
        this.secretObjective=other.secretObjective;
        this.playingField=new PlayingField(other.playingField);
        this.startingCard=other.startingCard;
        this.personalHandCards=other.personalHandCards;
    }
    /**
     * Method used to update the score track's information whenever points are scored.
     * The score track does not update during the objectives calculations.
     */
    private void notifyScoreTrack(ScoreTrack scoreTrack){
        scoreTrack.updateScoreTrack(name,currentPoints);
    }

    /**
     *
     * @return the player's randomly assigned starting card
     */
    public PlayableCard getStartingCard(){
        return startingCard;
    }

    /**
     * Getter for the player's name
     * @return the player's name
     */
    public String getName(){
        return name;
    }

    /**
     * @return how many times in total an objective card has been scored, used in the final phase of the game if there is a draw
     */
    public int getNumberOfScoredObjectives() {
        synchronized (pointsLock) { return numberOfScoredObjectives;    }
    }

    /**
     * Getter method for the player's points
     * @return the player's current points
     */
    public int getPoints(){
        synchronized (pointsLock) { return currentPoints;   }
    }

    /**
     * Puts received card in player's hand,this method is called only if there are two cards in the player's hand
     * @param card
     */
    public void receiveDrawnCard(PlayableCard card) throws HandAlreadyFullException {
        if(personalHandCards.size() == 3){
            throw new HandAlreadyFullException();
        }
        synchronized (personalHandCards) {
            personalHandCards.add(card);
        }
        currentTurn++;
    }

    /**
     *
     * @return a list containing all possible positions on which a card could be placed
     * @throws NotPlacedException if an error has occurred during the placement of a card
     * @throws PlayerCantPlaceAnymoreException if the player has blocked all possible future moves
     */
    public List<Point> getAvailablePositions() throws NotPlacedException, PlayerCantPlaceAnymoreException {
        List<Point> availablePositions;
        synchronized (playingField) {
            availablePositions = playingField.getAvailablePositions();
        }
        if(availablePositions.isEmpty()){
            throw new PlayerCantPlaceAnymoreException();
        }   return availablePositions;
    }

    /**
     *Places the card in the player's area
     * @param cardID to be placed on (xCoordinate,yCoordinate)
     * @param xCoordinate
     * @param yCoordinate
     * @throws CardNotInHandException if the card chosen isn't among the player's current hand
     * @throws NotEnoughResourcesException if the requirements to play the gold card are not met
     * @throws InvalidPositionException if the chosen point isn't valid
     * @throws AlreadyPlacedException if the chosen card has already been placed by one of the players
     */
    public void placeCard(int cardID, int xCoordinate, int yCoordinate, boolean isFacingUp, ScoreTrack scoreTrack) throws CardNotInHandException, NotEnoughResourcesException, InvalidPositionException, AlreadyPlacedException, NotPlacedException {
        PlayableCard toBePlaced = null;
        int previousPoints = currentPoints;
        synchronized (playingField) {
            synchronized (personalHandCards) {
                for(PlayableCard c: personalHandCards){
                    if(c.getID() == cardID){
                        toBePlaced = c;
                    }
                }
                if (toBePlaced == null) {
                    throw new CardNotInHandException();
                }
                Point position = new Point(xCoordinate, yCoordinate);
                if (!isPlacingPointValid(position)) {
                    throw new InvalidPositionException();
                }
                if (cardID <= 80 && cardID >= 41 &&isFacingUp) {
                    if (!playingField.isGoldCardPlaceable((GoldCard) toBePlaced)) throw new NotEnoughResourcesException();
                }
                currentTurn++;
                //sets up the card's position and removes it from the player's hand
                toBePlaced.placeCard(position, currentTurn, isFacingUp);
                personalHandCards.remove(toBePlaced);
            }
            //adds the card to the playing field and updates the player's points
            playingField.addPlacedCard(toBePlaced);
        }
        synchronized (pointsLock) {
            currentPoints = currentPoints + calculatePointsOnPlacement(toBePlaced);
        }
        if(previousPoints != currentPoints){
            notifyScoreTrack(scoreTrack);
        }
    }

    /**Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     * @param point - point (z,w) in which the card will be placed if it is a valid position. Every point such that z+w is odd will be rejected because it's like saying that the player wants to cover two corners of a same card.
     * @return return false if position is invalid
     */
    private boolean isPlacingPointValid(Point point) throws NotPlacedException {
        if((point.getX() + point.getY()) % 2 != 0){
            return false;
        }
        synchronized (playingField) {return playingField.isPositionAvailable(point);}
    }

    /**
     * Sets up the player's secret objective
     * @param secretObjective is chosen from two random objectives
     */
    public void setSecretObjective(ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
        if(this.secretObjective!= null){
            throw new ObjectiveAlreadySetException();
        }
        this.secretObjective=secretObjective;
    }

    /**
     * Called after the player chooses on which side to place the startin card
     * @param isFacingUp chosen side
     * @throws AlreadyPlacedException if the card has already been initialized elsewhere
     * @throws NotPlacedException if the initialization failed
     */
    public void placeStartingCard(boolean isFacingUp) throws AlreadyPlacedException, NotPlacedException {
        startingCard.placeCard(new Point(0,0),0,isFacingUp);
        synchronized (playingField) {
            playingField.addPlacedCard(this.startingCard);
        }
    }

    /**
     * Calculates how many points are made with the common objectives
     * @param firstVisibleObjective
     * @param secondVisibleObjective
     */
    public void calculateCommonObjectives(ObjectiveCard firstVisibleObjective,ObjectiveCard secondVisibleObjective){
        synchronized (pointsLock) {
            int scoredPoints;
            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(firstVisibleObjective);
            }
            if(firstVisibleObjective!=null){
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / firstVisibleObjective.getPoints();
            }
            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(secondVisibleObjective);
            }
            if(secondVisibleObjective!=null){
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / secondVisibleObjective.getPoints();
            }
        }
    }

    /**
     * Awards the secret objective points
     */
    public void calculateSecretObjective(){
        synchronized (pointsLock) {
            int scoredPoints;
            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(secretObjective);
            }
            if(secretObjective!=null) {
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / secretObjective.getPoints();
            }
        }
    }

    /**
     *
     * @param card is the card that has just been placed
     * @return how many points are awarded by this move
     * @throws NotPlacedException if a placement error has occurred for the input card
     */
    private int calculatePointsOnPlacement(PlayableCard card) throws NotPlacedException {
        int placedPoints= 0;
        if(!card.isFacingUp()){
            return placedPoints;
        }
        if(card.getID() >= 1 && card.getID() <= 40){
            ResourceCard resourceCard= (ResourceCard) card;
            placedPoints= resourceCard.getPointsOnPlacement();
        }
        else if(card.getID() >= 41 && card.getID() <= 80){
            synchronized (playingField) {
                placedPoints= playingField.calculateGoldPoints((GoldCard) card);
            }
        }
        return placedPoints;
    }

    /**
     *  Used to check if the number of cards in the current hand is valid (in order to check if other parts of the code made some mistakes)
     * @return size of the player's hand
     * @deprecated may not be used at all
     */
    @Deprecated
    public int quantityOfCards(){
        synchronized (personalHandCards) {return personalHandCards.size();}
    }

    /**
     * @return a copy of the player's hand, to be seen by the client
     */
    public List<PlayableCard> viewCurrentHand(){
        synchronized (personalHandCards) {return new ArrayList<>(personalHandCards);}
    }

    /**
     * @return how many of each visible symbols are there
     */
    public Map<TokenType,Integer> viewVisibleSymbols(){
        synchronized (playingField) {return playingField.getVisibleSymbols();}
    }

    /**
     *
     * @param request symbol which number is requested
     * @return how many requested token type are visible
     */
    public int viewVisibleTokenType(TokenType request){
        synchronized (playingField) {return playingField.getVisibleTokenType(request);}
    }

    /**
     * @return a copy of the Player object to be used in case of a disconnection during the player's turn.
     */
    public Player getBackup(){
        return new Player(this);
    }

    public List<SimpleCard> getCardsAsSimpleCards() {
        return playingField.getCardsAsSimpleCards();
    }
}