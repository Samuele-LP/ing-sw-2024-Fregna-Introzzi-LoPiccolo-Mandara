package it.polimi.ingsw.view.Field;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.*;

/**
 * Contains information about a player's field and provides methods to show it to the user.
 */
public abstract class PlayerFieldView {

    List<Point> availablePositions=null;

    final List<SimpleCard> simpleCards;

    Map<TokenType,Integer> visibleSymbols= new HashMap<>();

    final String owner;

    int lowestX = 0, lowestY = 0;

    int highestX = 0, highestY = 0;

    /**
     * Constructor to initialize the player field view with the owner's name.
     *
     * @param owner the name of the field's owner
     */
    public PlayerFieldView(String owner) {
        this.owner = owner;
        this.simpleCards = new ArrayList<>();
    }

    /**
     * Adds a card to the player's playing field and resets the list of all available positions.
     * It will be necessary to ask for the available positions again from the server.
     *
     * @param placeID      ID of the placed card
     * @param placedX      x position of the placed card
     * @param placedY      y position of the placed card
     * @param isFacingUp   side of the card to be shown
     * @param visibleSymbols the map of visible symbols after placing the card
     */
    public void updateField(int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType,Integer> visibleSymbols) {
        highestX = Math.max(placedX, highestX);
        highestY = Math.max(placedY, highestY);
        lowestX = Math.min(placedX, lowestX);
        lowestY = Math.min(placedY, lowestY);
        simpleCards.add(new SimpleCard(placeID, placedX, placedY, isFacingUp));
        this.visibleSymbols = new HashMap<>(visibleSymbols);
        availablePositions = null;
    }

    /**
     * Updates the list of available positions.
     * The list is set to null if it hasn't been updated after a call to updateField.
     *
     * @param availablePositions the list of all positions available for the next move
     */
    public void updateAvailablePositions(List<Point> availablePositions){
        this.availablePositions = availablePositions;
    }

    /**
     * From the cards memorized as SimpleCards, it returns an Optional containing information on whether a card is present at the given coordinates.
     *
     * @param x is the x position of the card
     * @param y is the y position of the card
     * @return an Optional containing null if the card isn't present, or an Optional containing the reference to the SimpleCard otherwise
     */
    Optional<SimpleCard> getCardAtPosition(int x, int y){
        return simpleCards.stream().filter(card -> card.getX() == x && card.getY() == y).findFirst();
    }
}
