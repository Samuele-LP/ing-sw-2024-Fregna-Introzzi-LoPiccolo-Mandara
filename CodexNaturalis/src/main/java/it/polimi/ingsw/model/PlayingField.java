package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPlacedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayingField {
    private Map<TokenType,Integer> visibleSymbols;
    private Map<Point,PlayableCard> placedCards;

    /**
     *Creates the placedCrads and visbleSymbols HashMaps and sets to 0 the counter for every TokenType
     */
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
     * @param card the card that will be placed
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
     * Removes up to four symbols that have been covered by a player's move from the counter
     * @param p the new card's position
     * @throws NotPlacedException when the given card input is invalid
     */
    private void coverCorners(Point p) throws NotPlacedException {
        int x=p.getX();
        int y=p.getY();
        //The following Points represent the position of the four possible adjacent cards, if they exist, to the card that is being placed
        //TopRight represents the position on the top right relative to "p", and so on for the other 3 points
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
    /**Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     * @param point - point (z,w) in which the card will be placed if it is a valid position.
     * @return return false if position is invalid
     * @throws NotPlacedException if a card that has not been correctly updated ended up in the HashMap placedCards
     */
    public boolean isPositionAvailable(Point point) throws NotPlacedException {
        if(placedCards.containsKey(point)){
            return false;
        }
        int z=point.getX();
        int w=point.getY();
        int countAdjacenntCards=0;
        //The following Points represent the position of the four possible adjacent cards, if they exist, to the card that is being placed
        //TopRight represents the position on the top right relative to "point", and so on for the other 3 points
        Point topRight =new Point(z+1,w+1);
        Point topLeft =new Point(z-1,w+1);
        Point bottomRight =new Point(z+1,w-1);
        Point bottomLeft =new Point(z-1,w-1);
        if(placedCards.containsKey(topRight)){
            countAdjacenntCards++;
            if(placedCards.get(topRight).getPlacedBottomLeft()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsKey(topLeft)){
            countAdjacenntCards++;
            if(placedCards.get(topLeft).getPlacedBottomRight()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsKey(bottomRight)){
            countAdjacenntCards++;
            if(placedCards.get(bottomRight).getPlacedTopLeft()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsKey(bottomLeft)){
            countAdjacenntCards++;
            if(placedCards.get(bottomLeft).getPlacedTopRight()==TokenType.blocked){
                return false;
            }
        }
        return countAdjacenntCards>0;
    }
    /**
     * For every point in which there is a card this method checks the 4 possible adjacent points to determine where a valid move can be done
     * @return a List of Points that contains all possible placements for the next move
     * @throws NotPlacedException if a card that has not been correctly updated ended up in the HashMap placedCards
     */
    public List<Point> getAvailablePositions() throws NotPlacedException {
        List<Point> availablePoints = new ArrayList<>();
        List<Point> alreadyChecked = new ArrayList<>();
        int x,y;
        Point topRightAdj;
        Point topLeftAdj;
        Point bottomRightAdj;
        Point bottomLeftAdj;
        for(Point p : placedCards.keySet()){
            x = p.getX();
            y= p.getY();
            topRightAdj =new Point(x+1,y+1);
            topLeftAdj =new Point(x-1,y+1);
            bottomRightAdj =new Point(x+1,y-1);
            bottomLeftAdj =new Point(x-1,y-1);
            if(!alreadyChecked.contains(topRightAdj)&&isPositionAvailable(topRightAdj)){
                availablePoints.add(topRightAdj);
            }
            else{
                alreadyChecked.add(topRightAdj);
            }
            if(!alreadyChecked.contains(topLeftAdj)&&isPositionAvailable(topLeftAdj)){
                availablePoints.add(topLeftAdj);
            }
            else{
                alreadyChecked.add(topLeftAdj);
            }
            if(!alreadyChecked.contains(bottomLeftAdj)&&isPositionAvailable(bottomLeftAdj)){
                availablePoints.add(bottomLeftAdj);
            }
            else{
                alreadyChecked.add(bottomLeftAdj);
            }
            if(!alreadyChecked.contains(bottomRightAdj)&&isPositionAvailable(bottomRightAdj)){
                availablePoints.add(bottomRightAdj);
            }
            else{
                alreadyChecked.add(bottomRightAdj);
            }
        }
        return availablePoints;
    }
}