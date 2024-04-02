package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.*;

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
        if(card.getCardColour()==CardType.starter&&card.isFacingUp()){
            StartingCard startingCard = (StartingCard) card;
            for(TokenType t : startingCard.getCentralSymbols()){
                visibleSymbols.put(t,visibleSymbols.get(t)+1);
            }
        }
        else if(!(card.getCardColour()==CardType.starter)&&!card.isFacingUp()){
            CardType backColour=card.getCardColour();
            TokenType backSymbol= (backColour==CardType.animal? TokenType.animal : (backColour==CardType.fungi? TokenType.fungi :(backColour==CardType.insect? TokenType.insect : TokenType.plant)) );
            visibleSymbols.put(backSymbol,visibleSymbols.get(backSymbol)+1);
        }
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
    public boolean isGoldCardPlaceable(GoldCard goldCard){
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
                alreadyChecked.add(topRightAdj);
            }
            else{
                alreadyChecked.add(topRightAdj);
            }
            if(!alreadyChecked.contains(topLeftAdj)&&isPositionAvailable(topLeftAdj)){
                availablePoints.add(topLeftAdj);
                alreadyChecked.add(topRightAdj);
            }
            else{
                alreadyChecked.add(topLeftAdj);
            }
            if(!alreadyChecked.contains(bottomLeftAdj)&&isPositionAvailable(bottomLeftAdj)){
                availablePoints.add(bottomLeftAdj);
                alreadyChecked.add(topRightAdj);
            }
            else{
                alreadyChecked.add(bottomLeftAdj);
            }
            if(!alreadyChecked.contains(bottomRightAdj)&&isPositionAvailable(bottomRightAdj)){
                availablePoints.add(bottomRightAdj);
                alreadyChecked.add(topRightAdj);
            }
            else{
                alreadyChecked.add(bottomRightAdj);
            }
        }
        return availablePoints;
    }

    /**
     * "empty","blocked" are exluded from the result. Usually the player does not care about how many blocked or empty corners there are, if they do they can use getVisibleTokenType
     * @return a map between a TokenType and the number of occurrences in the playing field
     */
    public Map<TokenType, Integer> getVisibleSymbols() {
        Map<TokenType,Integer> visible = new HashMap<>();
        visible.put(TokenType.animal,getVisibleTokenType(TokenType.animal));
        visible.put(TokenType.plant,getVisibleTokenType(TokenType.plant));
        visible.put(TokenType.fungi,getVisibleTokenType(TokenType.fungi));
        visible.put(TokenType.insect,getVisibleTokenType(TokenType.insect));
        visible.put(TokenType.quill,getVisibleTokenType(TokenType.quill));
        visible.put(TokenType.scroll,getVisibleTokenType(TokenType.scroll));
        visible.put(TokenType.ink,getVisibleTokenType(TokenType.ink));
        return visible;
    }
    /**
     *
     * @param requested is the specific TokenType which number is wanted
     * @return how many of the requested TokenType  are there
     */
    public int getVisibleTokenType(TokenType requested){
        return visibleSymbols.get(requested);
    }

    /**
     * This method checks how the card awards points, then calculates how many points are scored. This method is only called after placing a card
     * @param card is the gold card that has just been placed
     * @return how many points the card scored for the player
     * @throws NotPlacedException if an error in placement has occurred
     */
    public int calculateGoldPoints(GoldCard card)throws NotPlacedException {
        int givenPoints=0;
        if(card.getPointsCondition()==TokenType.empty){
            givenPoints= card.getPoints();
        }
        /*
        Since this method is called just after a placement there can't be a card on top of this one so
        the 4 points that are being checked are either empty or contain a card that has been placed in a turn
        before the one this method has been called in
         */
        else if(card.getPointsCondition()==TokenType.blocked){
           int x=card.getPosition().getX(),y=card.getPosition().getY();
            Point topRight =new Point(x+1,y+1);
            Point topLeft =new Point(x-1,y+1);
            Point bottomRight =new Point(x+1,y-1);
            Point bottomLeft =new Point(x-1,y-1);
            if(placedCards.containsKey(topRight)){
                givenPoints=givenPoints+ card.getPoints();
            }
            if(placedCards.containsKey(topLeft)){
                givenPoints=givenPoints+ card.getPoints();
            }
            if(placedCards.containsKey(bottomRight)){
                givenPoints=givenPoints+ card.getPoints();
            }
            if(placedCards.containsKey(bottomLeft)){
                givenPoints=givenPoints + card.getPoints();
            }
        }
        else{//counts how many symbols are there in total after the placement
            //In an earlier version the method removed the symbols on the card itself, but the rules state that they are included
            givenPoints=card.getPoints()*visibleSymbols.get(card.getPointsCondition());
        }
        return givenPoints;
    }
    public int calculateObjectivePoints(ObjectiveCard objective){
        if(objective.isPositional()){
            if(objective.getPositionalRequirements()== ObjectiveSequence.blueDiagonal||objective.getPositionalRequirements()== ObjectiveSequence.redDiagonal){
                return this.countDiagonals(objective.getPositionalRequirements())*objective.getPoints();
            }
            else if(objective.getPositionalRequirements()== ObjectiveSequence.greenAntiDiagonal||objective.getPositionalRequirements()== ObjectiveSequence.purpleAntiDiagonal){
                return this.countAntiDiagonals(objective.getPositionalRequirements())*objective.getPoints();
            }
            else{
                if(objective.getPositionalRequirements()==ObjectiveSequence.twoBlueOneRed){
                    return this.countLShapes(CardType.animal,CardType.fungi)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoRedOneGreen){
                    return this.countLShapes(CardType.fungi,CardType.plant)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoGreenOnePurple){
                    return this.countLShapes(CardType.plant,CardType.insect)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoPurpleOneBlue){
                    return this.countLShapes(CardType.insect,CardType.animal)*objective.getPoints();
                }
            }
        }
        else{

        }
        return 0;
    }
    /**
     * Counts how many red or blue diagonals are there
     * @param requirements used to determine if the program is searching for a blue or red diagonal
     * @return the number of red or blue diagonal are there
     */
    private int countDiagonals(ObjectiveSequence requirements) {
        CardType colour = requirements==ObjectiveSequence.blueDiagonal? CardType.animal : CardType.fungi;
        List<Point> possibleSequencePoints = new ArrayList<>();
        List<Point> topExtremities = new ArrayList<>();
        List<Point> bottomExtremities = new ArrayList<>();
        int x,y;
        Point topRight,bottomLeft;
        int numberOfSequences=0;
        //in this list there are only the points that could be inside a valid sequence
        for(Point p : placedCards.keySet()){
            if(placedCards.get(p).getCardColour()==colour){
                possibleSequencePoints.add(p);
            }
        }
        /*This for cycle eliminates "alone" points, points that on their topRight or bottomLeft have no other points that could form a diagonal
        * And assigns new values if the point is in a possible chain
         */
        for(Point p : possibleSequencePoints){
            x=p.getX();
            y=p.getY();
            topRight=new Point(x+1,y+1);
            bottomLeft= new Point(x-1,y-1);
            if(!possibleSequencePoints.contains(topRight)&&!possibleSequencePoints.contains(bottomLeft)){
                //if a point is isolated it is removed from the List of valid points
                possibleSequencePoints.remove(p);
            }
            else if(possibleSequencePoints.contains(topRight)&&!possibleSequencePoints.contains(bottomLeft)){
                //if a point has no other point on the bottom left then it is a bottom extremity
                bottomExtremities.add(p);
                possibleSequencePoints.remove(p);
            }
            else if(!possibleSequencePoints.contains(topRight)&&possibleSequencePoints.contains(bottomLeft)){
                //if a point has no other point on the top right then it is a top extremity
                topExtremities.add(p);
                possibleSequencePoints.remove(p);
            }
        }
        /*In possibleSequencePoints there are only middle-points for diagonals, so if there are no middle points
        then it means that either there is no diagonal or that the maximum length is 2 (one top and one bottom extremity)
        and no points are scored
        * */
        if(possibleSequencePoints.isEmpty()){
            return 0;
        }
        //A top and bottom extremity refer to the same diagonal only if x1-x2=y1-y2
        //Examples: (1,1) (2,2) (3,3) is a diagonal; (2,-4) (3,-3) (4,-2) is another diagonal
        for(Point bottom: bottomExtremities){
            for(Point top:topExtremities){
                int difference=top.getX()-bottom.getX();
                if(difference==top.getY()- bottom.getY()){
                    difference++;//It is increased by 1 because with a diagonal long Z difference is Z-1
                    //If a diagonal is 3*n long then it can contain (3*n/3) sequences that score points, as cards can be used for the same objective only once
                    //But if the diagonal is long only, for example, 3*n -2 then there are n-1 sequences that score points
                    numberOfSequences=numberOfSequences+Math.floorDiv(difference,3);
                }
            }
        }
        return numberOfSequences;
    }
    /**
     * Counts how many green or purple diagonals are there
     * @param requirements used to determine if the program is searching for a green or purple anti-diagonal
     * @return the number of green or purple diagonal are there
     */
    private int countAntiDiagonals(ObjectiveSequence requirements) {
        CardType colour = requirements==ObjectiveSequence.greenAntiDiagonal? CardType.plant : CardType.insect;
        List<Point> possibleSequencePoints = new ArrayList<>();
        List<Point> topExtremities = new ArrayList<>();
        List<Point> bottomExtremities = new ArrayList<>();
        int x,y;
        Point topLeft,bottomRight;
        int numberOfSequences=0;
        //in this list there are only the points that could be inside a valid sequence
        for(Point p : placedCards.keySet()){
            if(placedCards.get(p).getCardColour()==colour){
                possibleSequencePoints.add(p);
            }
        }
        /*This for cycle eliminates "alone" points, points that on their topLeft or bottomRight have no other points that could form an anti-diagonal
         * And assigns new values if the point is in a possible chain
         */
        for(Point p : possibleSequencePoints){
            x=p.getX();
            y=p.getY();
            topLeft=new Point(x-1,y+1);
            bottomRight= new Point(x+1,y-1);
            if(!possibleSequencePoints.contains(topLeft)&&!possibleSequencePoints.contains(bottomRight)){
                //if a point is isolated it is removed from the List of valid points
                possibleSequencePoints.remove(p);
            }
            else if(possibleSequencePoints.contains(topLeft)&&!possibleSequencePoints.contains(bottomRight)){
                //if a point has no other point on the bottom right then it is a bottom extremity
                bottomExtremities.add(p);
                possibleSequencePoints.remove(p);
            }
            else if(!possibleSequencePoints.contains(topLeft)&&possibleSequencePoints.contains(bottomRight)){
                //if a point has no other point on the top left then it is a top extremity
                topExtremities.add(p);
                possibleSequencePoints.remove(p);
            }
        }
        /*In possibleSequencePoints there are only middle-points for diagonals, so if there are no middle points
        then it means that either there is no diagonal or that the maximum length is 2 (one top and one bottom extremity)
        and no points are scored
        * */
        if(possibleSequencePoints.isEmpty()){
            return 0;
        }
        //A top and bottom extremity refer to the same diagonal only if x1-x2=y1-y2
        //Examples: (1,4) (2,3) (3,2) is an anti-diagonal; (2,-4) (3,-5) (4,-6) is another anti-diagonal
        for(Point bottom: bottomExtremities){
            for(Point top:topExtremities){
                int difference=top.getX()-bottom.getX();
                if(difference==top.getY()- bottom.getY()){
                    difference++;//It is increased by 1 because with an anti-diagonal long Z difference is Z-1
                    //If an anti-diagonal is 3*n long then it can contain (3*n/3) sequences that score points, as cards can be used for the same objective only once
                    //But if the anti-diagonal is long only, for example, 3*n -2 then there are n-1 sequences that score points
                    numberOfSequences=numberOfSequences+Math.floorDiv(difference,3);
                }
            }
        }
        return numberOfSequences;
    }
    private int countLShapes(CardType column, CardType side) {
        return 0;
    }
}