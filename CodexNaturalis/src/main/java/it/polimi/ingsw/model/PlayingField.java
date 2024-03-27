package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPlacedException;

import java.util.HashMap;
import java.util.Map;

public class PlayingField {
    private Map<TokenType,Integer> visibleSymbols;
    private Map<Point,PlayableCard> placedCards;
    public PlayingField(){
        visibleSymbols = new HashMap<>();
        visibleSymbols.put(TokenType.fungi,0);
        visibleSymbols.put(TokenType.animal,0);
        visibleSymbols.put(TokenType.plant,0);
        visibleSymbols.put(TokenType.insect,0);
        visibleSymbols.put(TokenType.blocked,0);
        visibleSymbols.put(TokenType.empty,0);
        visibleSymbols.put(TokenType.ink,0);
        visibleSymbols.put(TokenType.quill,0);
        visibleSymbols.put(TokenType.scroll,0);
        placedCards = new HashMap<>();
    }

    /**
     *
     * @param card
     * @throws NotPlacedException when the given card input is invalid
     */
    public void addPlacedCard(PlayableCard card)throws NotPlacedException {
        placedCards.put(card.getPosition(),card);
        updateVisibleSymbols(card.getPosition(),card);
    }

    /**
     * Adds the new card's visible symbols to the counter, it then calls coverCorners to remove any symbol that was covered during the placement
     * @param p the new card's position
     * @param card the new card
     * @throws NotPlacedException when the given card input is invalid
     */
    private void updateVisibleSymbols(Point p, PlayableCard card) throws NotPlacedException {
        visibleSymbols.put( card.getPlacedTopRight(),    visibleSymbols.get( card.getPlacedTopRight())   +1);
        visibleSymbols.put( card.getPlacedBottomLeft(),  visibleSymbols.get( card.getPlacedBottomLeft()) +1);
        visibleSymbols.put( card.getPlacedBottomRight(), visibleSymbols.get( card.getPlacedBottomRight())    +1);
        visibleSymbols.put( card.getPlacedTopLeft(), visibleSymbols.get( card.getPlacedTopLeft())    +1);
        coverCorners(p);
    }

    /**
     * Removes up to four symbols that have been covered by a plyer's move from the counter
     * @param p the new card's position
     * @throws NotPlacedException when the given card input is invalid
     */
    private void coverCorners(Point p) throws NotPlacedException {
        int x=p.getX();
        int y=p.getY();
        /**The following Points represent the position of the four possible adjacent cards, if they exist, to the card that is being placed
         TopRight represents the position on the top right relative to "p", and so on for the other 3 points
         * */
        Point topRight =new Point(x+1,y+1);
        Point topLeft =new Point(x-1,y+1);
        Point bottomRight =new Point(x+1,y-1);
        Point bottomLeft =new Point(x-1,y-1);
        TokenType otherCardCoveredCorner;//Used to get the TokenType of the covered corner
        if(placedCards.containsKey(topRight)){//If there is a card on the placed card's top right then that card's bottom left is what has been covered
            otherCardCoveredCorner=placedCards.get(topRight).getPlacedBottomLeft();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsKey(topLeft)){//If there is a card on the placed card's top left then that card's bottom right is what has been covered
            otherCardCoveredCorner=placedCards.get(topLeft).getPlacedBottomRight();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsKey(bottomLeft)){//If there is a card on the placed card's bottom left then that card's top right is what has been covered
            otherCardCoveredCorner=placedCards.get(bottomLeft).getPlacedTopRight();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsKey(bottomRight)){//If there is a card on the placed card's bottom right then that card's top left is what has been covered
            otherCardCoveredCorner=placedCards.get(bottomRight).getPlacedTopLeft();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
    }

    /**
     * Checks if the given Gold card's requirements are met
     * @return true if there are enough symbols to place the card, false otherwise
     */
    public boolean isGoldCardPlaceable(PlayableCard card){
        GoldCard goldCard= (GoldCard) card;
        if(goldCard.getPlacementConditions().get(TokenType.animal)>visibleSymbols.get(TokenType.animal)){
            return false;
        }
        if(goldCard.getPlacementConditions().get(TokenType.insect)>visibleSymbols.get(TokenType.insect)){
            return false;
        }
        if(goldCard.getPlacementConditions().get(TokenType.fungi)>visibleSymbols.get(TokenType.fungi)){
            return false;
        }
        if(goldCard.getPlacementConditions().get(TokenType.plant)>visibleSymbols.get(TokenType.plant)){
            return false;
        }
        return true;
    }

}
