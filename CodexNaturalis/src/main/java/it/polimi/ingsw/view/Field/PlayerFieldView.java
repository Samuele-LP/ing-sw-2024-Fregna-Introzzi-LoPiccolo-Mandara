package it.polimi.ingsw.view.Field;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.view.GameViewCli;
import it.polimi.ingsw.SimpleCard;

import java.util.*;

/**
 * Contains information about a layer's field and provides methods to show it to the user
 */
public abstract class PlayerFieldView {
     List<Point> availablePositions=null;
     final List<SimpleCard> simpleCards;
     Map<TokenType,Integer> visibleSymbols= new HashMap<>();
     int lowestX=0, lowestY=0;
     int highestX=0, highestY=0;
    public PlayerFieldView(){
        this.simpleCards = new ArrayList<>();
    }
    /**
     * Constructor used in case of a reconnection
     * @param cards the list of cards, already ordered
     * @param visibleSymbols is the list of visible symbols on a player's field
     */
    public PlayerFieldView(List<SimpleCard> cards,HashMap<TokenType,Integer> visibleSymbols){
        this.simpleCards=new ArrayList<>(cards);
        this.visibleSymbols= new HashMap<>(visibleSymbols);
        for(SimpleCard c: simpleCards){
            highestX= Math.max(c.getX(),highestX);
            highestY= Math.max(c.getX(),highestY);
            lowestX= Math.min(c.getX(),lowestX);
            lowestY= Math.min(c.getX(),lowestY);
        }
    }
    /**
     * Adds a card to the player's playing field and resets the list of all available positions; it will be necessary to ask it again to the server
     * @param placeID ID of the placed card
     * @param placedX x position
     * @param placedY y position
     * @param isFacingUp side of the card to be shown
     */
    public void updateField(int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType,Integer> visibleSymbols) {
        highestX= Math.max(placedX, highestX);
        highestY= Math.max(placedY, highestY);
        lowestX= Math.min(placedX, lowestX);
        lowestY= Math.min(placedY, lowestY);
        simpleCards.add(new SimpleCard(placedX,placedY,isFacingUp,placeID));
        this.visibleSymbols=new HashMap<>(visibleSymbols);
        availablePositions=null;
    }

    /**
     * Updates the lis of available positions.
     * The list is set to null if it hasn't been updated after a call to updateField
     * @param availablePositions the list of all positions available for the next move
     */
    public void updateAvailablePositions(List<Point> availablePositions){
        this.availablePositions=availablePositions;
    }

    /**
     * Prints the field
     */
    public abstract ArrayList<String> printField();
    /**
     * From the cards memorized as SimpleCards it returns an Optional containing information on whether a card is present
     * @param x is the x position of the card
     * @param y is the y position of the cards
     * @return an Optional containing null if the card isn't present, an Optional containing the reference to the SimpleCard otherwise
     */
    Optional<SimpleCard> getCardAtPosition(int x, int y){
        return simpleCards.stream().filter(card->card.getX()==x&&card.getY()==y).findFirst();
    }

}
