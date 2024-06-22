package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a player with their points, name, playing field, and current hand.
 */
public class Player {

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
     * Player constructor.
     *
     * @param name         the player's name
     * @param startingCard the player's randomly assigned starting card
     * @param startingHand the initial 2 resource + 1 gold cards that a player receives when starting a game
     * @throws IllegalStartingCardException if the card is not a starting card
     */
    public Player(String name, PlayableCard startingCard, PlayableCard[] startingHand) throws IllegalStartingCardException {
        this.name = name;
        currentPoints = 0;
        pointsLock = new Object();
        numberOfScoredObjectives = 0;
        currentTurn = 0;
        personalHandCards = new ArrayList<>();
        personalHandCards.add(startingHand[0]);
        personalHandCards.add(startingHand[1]);
        personalHandCards.add(startingHand[2]);

        if (!(startingCard.getID() <= 86 && startingCard.getID() >= 81)) {
            throw new IllegalStartingCardException();
        }

        this.startingCard = startingCard;
        playingField = new PlayingField();
        secretObjective = null;
    }

    /**
     * @return the player's randomly assigned starting card
     */
    public PlayableCard getStartingCard() {
        return startingCard;
    }

    /**
     * Getter for the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the number of times an objective card has been scored, used in the final phase of the game if there is a draw
     */
    public int getNumberOfScoredObjectives() {
        synchronized (pointsLock) {
            return numberOfScoredObjectives;
        }
    }

    /**
     * Getter method for the player's points.
     *
     * @return the player's current points
     */
    public int getPoints() {
        synchronized (pointsLock) {
            return currentPoints;
        }
    }

    /**
     * Puts the received card in the player's hand. This method is called only if there are two cards in the player's hand.
     *
     * @param card the received PlayableCard
     * @throws HandAlreadyFullException if the player's hand is already full
     */
    public void receiveDrawnCard(PlayableCard card) throws HandAlreadyFullException {
        if (personalHandCards.size() == 3) {
            throw new HandAlreadyFullException();
        }

        synchronized (personalHandCards) {
            personalHandCards.add(card);
        }

        currentTurn++;
    }

    /**
     * @return a list containing all possible positions on which a card could be placed
     * @throws NotPlacedException              if an error occurs during the placement of a card
     * @throws PlayerCantPlaceAnymoreException if the player has blocked all possible future moves
     */
    public List<Point> getAvailablePositions() throws NotPlacedException, PlayerCantPlaceAnymoreException {
        List<Point> availablePositions;

        synchronized (playingField) {
            availablePositions = playingField.getAvailablePositions();
        }

        if (availablePositions.isEmpty()) {
            throw new PlayerCantPlaceAnymoreException();
        }

        return availablePositions;
    }

    /**
     * Places the card in the player's area.
     *
     * @param cardID        the ID of the card to be placed
     * @param xCoordinate   the x-coordinate of the placement
     * @param yCoordinate   the y-coordinate of the placement
     * @param isFacingUp    whether the card is facing up
     * @param scoreTrack    the score track to update
     * @throws CardNotInHandException      if the card chosen isn't among the player's current hand
     * @throws NotEnoughResourcesException if the requirements to play the gold card are not met
     * @throws InvalidPositionException    if the chosen point isn't valid
     * @throws AlreadyPlacedException      if the chosen card has already been placed by one of the players
     * @throws NotPlacedException          if an error occurs during placement
     */
    public void placeCard(int cardID, int xCoordinate, int yCoordinate, boolean isFacingUp, ScoreTrack scoreTrack) throws CardNotInHandException, NotEnoughResourcesException, InvalidPositionException, AlreadyPlacedException, NotPlacedException {
        PlayableCard toBePlaced = null;
        int previousPoints = currentPoints;
        synchronized (playingField) {
            synchronized (personalHandCards) {
                for (PlayableCard c : personalHandCards) {
                    if (c.getID() == cardID) {
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

                if (cardID <= 80 && cardID >= 41 && isFacingUp) {
                    if (!playingField.isGoldCardPlaceable((GoldCard) toBePlaced))
                        throw new NotEnoughResourcesException();
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

        if (previousPoints != currentPoints) {
            scoreTrack.updateScoreTrack(name, currentPoints);
        }
    }

    /**
     * Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     *
     * @param point the point (z,w) where the card will be placed if it is a valid position. Every point such that z+w is odd will be rejected because it's like saying that the player wants to cover two corners of a same card.
     * @return false if the position is invalid
     * @throws NotPlacedException if an error occurs during placement
     */
    private boolean isPlacingPointValid(Point point) throws NotPlacedException {
        if ((point.getX() + point.getY()) % 2 != 0) {
            return false;
        }

        synchronized (playingField) {
            return playingField.isPositionAvailable(point);
        }
    }

    /**
     * Sets up the player's secret objective.
     *
     * @param secretObjective the chosen secret objective card
     * @throws ObjectiveAlreadySetException if the secret objective has already been set
     */
    public void setSecretObjective(ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
        if (this.secretObjective != null) {
            throw new ObjectiveAlreadySetException();
        }
        this.secretObjective = secretObjective;
    }

    /**
     * Places the starting card on the chosen side.
     *
     * @param isFacingUp true if the card is facing up, false otherwise
     * @throws AlreadyPlacedException if the card has already been initialized elsewhere
     * @throws NotPlacedException     if the initialization failed
     */
    public void placeStartingCard(boolean isFacingUp) throws AlreadyPlacedException, NotPlacedException {
        startingCard.placeCard(new Point(0, 0), 0, isFacingUp);
        synchronized (playingField) {
            playingField.addPlacedCard(this.startingCard);
        }
    }

    /**
     * Calculates the points scored with the common objectives.
     *
     * @param firstVisibleObjective  the first common objective card
     * @param secondVisibleObjective the second common objective card
     */
    public void calculateCommonObjectives(ObjectiveCard firstVisibleObjective, ObjectiveCard secondVisibleObjective) {
        synchronized (pointsLock) {
            int scoredPoints;

            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(firstVisibleObjective);
            }

            if (firstVisibleObjective != null) {
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / firstVisibleObjective.getPoints();
            }

            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(secondVisibleObjective);
            }

            if (secondVisibleObjective != null) {
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / secondVisibleObjective.getPoints();
            }
        }
    }

    /**
     * Awards the secret objective points.
     */
    public void calculateSecretObjective() {
        synchronized (pointsLock) {
            int scoredPoints;
            synchronized (playingField) {
                scoredPoints = playingField.calculateObjectivePoints(secretObjective);
            }
            if (secretObjective != null) {
                currentPoints = currentPoints + scoredPoints;
                numberOfScoredObjectives = numberOfScoredObjectives + scoredPoints / secretObjective.getPoints();
            }
        }
    }

    /**
     * Calculates the points awarded by placing a card.
     *
     * @param card the card that has just been placed
     * @return the points awarded by this move
     * @throws NotPlacedException if a placement error occurs for the input card
     */
    private int calculatePointsOnPlacement(PlayableCard card) throws NotPlacedException {
        int placedPoints = 0;

        if (!card.isFacingUp()) {
            return placedPoints;
        }

        if (card.getID() >= 1 && card.getID() <= 40) {
            ResourceCard resourceCard = (ResourceCard) card;
            placedPoints = resourceCard.getPointsOnPlacement();
        } else if (card.getID() >= 41 && card.getID() <= 80) {
            synchronized (playingField) {
                placedPoints = playingField.calculateGoldPoints((GoldCard) card);
            }
        }

        return placedPoints;
    }

    /**
     * @return a copy of the player's hand, to be viewed by the client
     */
    public List<PlayableCard> viewCurrentHand() {
        synchronized (personalHandCards) {
            return new ArrayList<>(personalHandCards);
        }
    }

    /**
     * @return a map of visible symbols and their counts
     */
    public HashMap<TokenType, Integer> viewVisibleSymbols() {
        synchronized (playingField) {
            return playingField.getVisibleSymbols();
        }
    }
}