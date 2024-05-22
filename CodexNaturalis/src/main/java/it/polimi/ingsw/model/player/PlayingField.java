package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.SimpleCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the playing field of a specific player.
 * It contains most of the game logic regarding a single player's playing area.
 */
class PlayingField {
    /**
     * Private class used to memorize the cards and points stored.
     */
    private class PlacedCards {

        private int nextIndex;
        private final ArrayList<Point> positions;
        private final ArrayList<PlayableCard> cards;
        public PlacedCards(){
            positions= new ArrayList<>();
            cards= new ArrayList<>();
            nextIndex=0;
        }
        /**
         * Constructor used to make a backup of a player's field
         */
        PlacedCards(PlacedCards other){
            this.nextIndex= other.nextIndex;
            this.positions= new ArrayList<>(other.positions);
            this.cards= new ArrayList<>(other.cards);
        }
        /**
         * Inserts the point and the card in their respective Lists
         * @param p the point on which the card is placed
         * @param card the reference to the card being placed
         */
        public synchronized void put(Point p, PlayableCard card){
            positions.add(nextIndex,p);
            cards.add(nextIndex,card);
            nextIndex++;
        }
        /**
         *
         * @param p the point to find
         * @return true if the point already has a card on it, false otherwise or if p is null
         */
        public synchronized boolean containsPoint(Point p){
            return p != null && positions.contains(p);
        }

        /**
         * It is only called after containsPoint(p) has returned true
         * @param p the point on which the card is placed
         * @return the card that is placed in position p
         */
        public synchronized PlayableCard get(Point p){
            return cards.get(positions.indexOf(p));
        }
        public synchronized ArrayList<PlayableCard> cardsList(){
            return new ArrayList<>(cards);
        }
        /**
         * Returns an ArrayList used to iterate
         * @return the list of points that have a card on top of them
         */
        public synchronized ArrayList<Point> pointsList(){
            ArrayList<Point> keyList = new ArrayList<>(positions);
            return keyList;
        }
    }
    private final Map<TokenType,Integer> visibleSymbols;
    private final PlacedCards placedCards;
    /**
     *Creates the placedCards and visibleSymbols objects and sets to 0 the counter for every TokenType.
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
        placedCards = new PlacedCards();
    }
    /**
     * Constructor used to make a backup of a player's field
     */
    PlayingField(PlayingField other){
        this.visibleSymbols= new HashMap<>(other.visibleSymbols);
        this.placedCards= new PlacedCards(other.placedCards);
    }
    /**
     * This method does not control the validity of a placement. It is called after the validity is confirmed by the class Player
     * @param card the card that will be placed
     * @throws NotPlacedException when the given card input is invalid
     */
    public synchronized void addPlacedCard(PlayableCard card)throws NotPlacedException {
        placedCards.put(card.getPosition(),card);
        updateVisibleSymbols(card.getPosition(),card);
    }
    /**
     * Adds the new card's visible symbols to the counter, it then calls coverCorners to remove any symbol that was covered during the placement
     * @param p the new card's position
     * @param card the new card
     * @throws NotPlacedException when the given card input is invalid
     */
    private synchronized void updateVisibleSymbols(Point p, PlayableCard card) throws NotPlacedException {
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
    private synchronized void coverCorners(Point p) throws NotPlacedException {
        int x=p.getX();
        int y=p.getY();
        //The following Points represent the position of the four possible adjacent cards, if they exist, to the card that is being placed
        //TopRight represents the position on the top right relative to "p", and so on for the other 3 points
        Point topRight =new Point(x+1,y+1);
        Point topLeft =new Point(x-1,y+1);
        Point bottomRight =new Point(x+1,y-1);
        Point bottomLeft =new Point(x-1,y-1);
        TokenType otherCardCoveredCorner;//Used to get the TokenType of the covered corner
        if(placedCards.containsPoint(topRight)){//If there is a card on the placed card's top right then that card's bottom left is what has been covered
            otherCardCoveredCorner=placedCards.get(topRight).getPlacedBottomLeft();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsPoint(topLeft)){//If there is a card on the placed card's top left then that card's bottom right is what has been covered
            otherCardCoveredCorner=placedCards.get(topLeft).getPlacedBottomRight();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsPoint(bottomLeft)){//If there is a card on the placed card's bottom left then that card's top right is what has been covered
            otherCardCoveredCorner=placedCards.get(bottomLeft).getPlacedTopRight();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
        if(placedCards.containsPoint(bottomRight)){//If there is a card on the placed card's bottom right then that card's top left is what has been covered
            otherCardCoveredCorner=placedCards.get(bottomRight).getPlacedTopLeft();
            visibleSymbols.put(otherCardCoveredCorner,visibleSymbols.get(otherCardCoveredCorner)-1);
        }
    }
    /**
     * Checks if the given Gold card's requirements are met
     * @return true if there are enough symbols to place the card, false otherwise
     */
    public synchronized boolean isGoldCardPlaceable(GoldCard goldCard){
        Map<TokenType,Integer> conditions = goldCard.getPlacementConditions();
        return (conditions.get(TokenType.animal)<=visibleSymbols.get(TokenType.animal))&&
                (conditions.get(TokenType.insect)<=visibleSymbols.get(TokenType.insect))&&
                (conditions.get(TokenType.fungi)<=visibleSymbols.get(TokenType.fungi))&&
                (conditions.get(TokenType.plant)<=visibleSymbols.get(TokenType.plant));
    }
    /**Checks if a card can be placed on the given point. A point (z,w) is valid only if currently there is no card on it and there is at least a point (x,y) such that (x+1,y+1)=(z,w) or (x-1,y+1)=(z,w) or (x+1,y-1)=(z,w) or (x-1,y-1)=(z,w) that has a card placed on it. And there aren't cards that: have a blocked bottomLeftCorner in (z+1,w+1); have a blocked bottomRightCorner in (z-1,w+1); have a blocked topRightCorner in (z-1,w-1); have a blocked topLeftCorner in (z+1,w-1)
     * @param point - point (z,w) in which the card will be placed if it is a valid position.
     * @return return false if position is invalid
     * @throws NotPlacedException if a card that has not been correctly updated ended up in the HashMap placedCards
     */
    public synchronized boolean isPositionAvailable(Point point) throws NotPlacedException {
        if(placedCards.containsPoint(point)){
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
        if(placedCards.containsPoint(topRight)){
            countAdjacenntCards++;
            if(placedCards.get(topRight).getPlacedBottomLeft()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsPoint(topLeft)){
            countAdjacenntCards++;
            if(placedCards.get(topLeft).getPlacedBottomRight()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsPoint(bottomRight)){
            countAdjacenntCards++;
            if(placedCards.get(bottomRight).getPlacedTopLeft()==TokenType.blocked){
                return false;
            }
        }
        if(placedCards.containsPoint(bottomLeft)){
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
    public synchronized List<Point> getAvailablePositions() throws NotPlacedException {
        List<Point> availablePoints = new ArrayList<>();
        List<Point> alreadyChecked = new ArrayList<>();
        int x,y;
        Point topRightAdj;
        Point topLeftAdj;
        Point bottomRightAdj;
        Point bottomLeftAdj;
        for(Point p : placedCards.pointsList()){
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
        return availablePoints.stream().distinct().sorted((p1, p2) -> {
            if (p1.equals(p2)) {
                return 0;
            }
            if (p1.getX() > p2.getX()) {
                return -1;
            } else if (p1.getX() == p2.getX() && p1.getY() > p2.getY()) {
                return -1;
            }
            return +1;
        }).
                toList();
    }
    /**
     * "empty","blocked" are excluded from the result. Usually the player does not care about how many blocked or empty corners there are, if they do they can use getVisibleTokenType
     * @return a map between a TokenType and the number of occurrences in the playing field
     */
    public synchronized HashMap<TokenType, Integer> getVisibleSymbols() {
        HashMap<TokenType,Integer> visible = new HashMap<>();
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
     * @param requested is the specific TokenType whose number is wanted
     * @return how many of the requested TokenType  are there
     */
    public synchronized int getVisibleTokenType(TokenType requested){
        return visibleSymbols.get(requested);
    }
    /**
     * This method checks how the card awards points, then calculates how many points are scored. This method is only called after placing a card
     * @param card is the gold card that has just been placed
     * @return how many points the card scored for the player
     * @throws NotPlacedException if an error in placement has occurred
     */
    public synchronized int calculateGoldPoints(GoldCard card)throws NotPlacedException {
        TokenType pointsCondition= card.getPointsCondition();
        int cardPoints= card.getPoints();
        int awardedPoints=0;
        if(pointsCondition==TokenType.empty){
            awardedPoints= cardPoints;
        }
        /*
        Since this method is called just after a placement there can't be a card on top of this one so
        the 4 points that are being checked are either empty or contain a card that has been placed in a turn
        before the one this method has been called in
         */
        else if(pointsCondition==TokenType.blocked){
           int x=card.getPosition().getX(),y=card.getPosition().getY();
            Point topRight =new Point(x+1,y+1);
            Point topLeft =new Point(x-1,y+1);
            Point bottomRight =new Point(x+1,y-1);
            Point bottomLeft =new Point(x-1,y-1);
            if(placedCards.containsPoint(topRight)){
                awardedPoints=awardedPoints+ cardPoints;
            }
            if(placedCards.containsPoint(topLeft)){
                awardedPoints=awardedPoints+ cardPoints;
            }
            if(placedCards.containsPoint(bottomRight)){
                awardedPoints=awardedPoints+ cardPoints;
            }
            if(placedCards.containsPoint(bottomLeft)){
                awardedPoints=awardedPoints + cardPoints;
            }
        }
        else{//counts how many symbols are there in total after the placement
            //In an earlier version the method removed the symbols on the card itself, but the rules state that they are included
            awardedPoints=cardPoints*visibleSymbols.get(pointsCondition);
        }
        return awardedPoints;
    }

    public synchronized int calculateObjectivePoints(ObjectiveCard objective){
        if(objective==null){
            return 0;
        }
        int pointsScored=0;
        if(objective.isPositional()){
            if(objective.getPositionalRequirements()== ObjectiveSequence.blueDiagonal||objective.getPositionalRequirements()== ObjectiveSequence.redDiagonal){
                pointsScored=this.countDiagonals(objective.getPositionalRequirements(),+1)*objective.getPoints();
            }
            else if(objective.getPositionalRequirements()== ObjectiveSequence.greenAntiDiagonal||objective.getPositionalRequirements()== ObjectiveSequence.purpleAntiDiagonal){
                pointsScored= this.countDiagonals(objective.getPositionalRequirements(),-1)*objective.getPoints();
            }
            else{
                if(objective.getPositionalRequirements()==ObjectiveSequence.twoBlueOneRed){
                    pointsScored=  this.countLShapes(CardType.animal,CardType.fungi,1,1)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoRedOneGreen){
                    pointsScored= this.countLShapes(CardType.fungi,CardType.plant,1,-1)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoGreenOnePurple){
                    pointsScored= this.countLShapes(CardType.plant,CardType.insect,-1,-1)*objective.getPoints();
                }
                else if(objective.getPositionalRequirements()==ObjectiveSequence.twoPurpleOneBlue){
                    pointsScored= this.countLShapes(CardType.insect,CardType.animal,-1,1)*objective.getPoints();
                }
            }
        }
        else{
            Map<TokenType,Integer> requirements= objective.getListRequirementsAsMap();
            int minimum=-1;//it represents the minimum number of times a certain symbol/multiplicity of the same symbol has appeared on the field
            /*For example: I have 3 quills and 2 scrolls visible and the  objective requires 1 scroll and 1 quill to be valid
              then I can't score it as many times as how many quills I have because  the limiting factor is the number of quills
            */
            for(TokenType t: requirements.keySet()){
                int timesScored= Math.floorDiv(visibleSymbols.get(t),requirements.get(t));
                if(minimum==-1 || timesScored<minimum){
                    minimum=timesScored;
                }
            }
            pointsScored= minimum*objective.getPoints();
        }
        return pointsScored;
    }
    /**
     * Counts how many diagonals of the needed colour are there
     * @param requirements used to determine if the program is searching for a blue or red diagonal
     * @param xDirection it's the direction in which moves the x coordinate of the diagonal, from bottom to top so the y increases by 1 always
     * @return the number of red or blue diagonal are there
     */
    private synchronized int countDiagonals(ObjectiveSequence requirements,int xDirection){
        CardType colour = requirements==ObjectiveSequence.blueDiagonal? CardType.animal : (requirements==ObjectiveSequence.redDiagonal?CardType.fungi: (requirements==ObjectiveSequence.greenAntiDiagonal?CardType.plant:CardType.insect)  );
        List<Point> possibleSequencePoints = new ArrayList<>();
        List<Point> topExtremities = new ArrayList<>();
        List<Point> bottomExtremities = new ArrayList<>();
        Point slider;
        int x,y;
        Point topAdj,bottomAdj;
        int numberOfSequences=0;
        for(Point p : placedCards.pointsList()){
            if(placedCards.get(p).getCardColour()==colour){
                possibleSequencePoints.add(p);
            }
        }
        /*This for cycle eliminates "alone" points, points that have no other adjacent points that could form a diagonal
         */
        ArrayList<Point> toRemove= new ArrayList<>();
        for(Point p : possibleSequencePoints){
            x=p.getX();
            y=p.getY();
            //y+1 because it is on Top, x+xDirection because the diagonal is analyzed bottom to top, so the direction from bottom to top is xDirection
            topAdj=new Point(x+xDirection,y+1);
            //y-1 because it is on bottom, x-xDirection because the diagonal is analyzed bottom to top, so the direction from top to bottom is the inverse of xDirection
            bottomAdj= new Point(x-xDirection,y-1);
            updateExtremityLists(toRemove,possibleSequencePoints, topExtremities, bottomExtremities, topAdj, bottomAdj, p);
        }
        possibleSequencePoints.removeAll(toRemove);
        /*In possibleSequencePoints there are only middle-points for diagonals, so if there are no middle points
        then it means that either there is no diagonal or that the maximum length is 2 (one top and one bottom extremity)
        and no points are scored
        * */
        if(possibleSequencePoints.isEmpty()){
            return 0;
        }
        /*
        This loops iterate from every bottom of a diagonal until it hits a top, the length is used to calculate how many times the diagonal can be used
        to score points.
         */
        for(Point bottom: bottomExtremities){
            int length=1;
            slider=new Point(bottom.getX(),bottom.getY());
            while(!topExtremities.contains(slider)){
                length++;
                slider=new Point(slider.getX()+xDirection,slider.getY()+1);
            }
            numberOfSequences=numberOfSequences+Math.floorDiv(length,3);
        }
        return numberOfSequences;
    }
    /**
     * Method used to calculate how many L-shapes are there
     * @param columnColour the colour of the "|" part of the L-Shape
     * @param sideColour colour of the "_" part of the L-shape
     * @param xOffset used to determine if the "_" part is on the right or the left
     * @param yOffset used to determine if the "_"part is on top or bottom
     * @return how many L-shapes that satisfy the parameters are there
     */
    private synchronized int countLShapes(CardType columnColour, CardType sideColour,int xOffset,int yOffset){
        List<Point> possibleColumnPoints = new ArrayList<>();
        List<Point> possibleSidePoints = new ArrayList<>();
        List<Point> topExtremity = new ArrayList<>();
        List<Point> bottomExtremity = new ArrayList<>();
        for(Point p: placedCards.pointsList()){
            if(placedCards.get(p).getCardColour()==columnColour){
                possibleColumnPoints.add(p);
            }
        }
        Point above;
        Point below;
        ArrayList<Point> toRemove = new ArrayList<>();
        for(Point p:possibleColumnPoints){
            above= new Point(p.getX(),p.getY()+2);
            below= new Point(p.getX(),p.getY()-2);
            updateExtremityLists(toRemove,possibleColumnPoints, topExtremity, bottomExtremity, above, below, p);
        }
        possibleColumnPoints.removeAll(toRemove);
        //if there are no columns of at least height 2 then there can't be matching sequences, so if there are no top or bottom extremities
        if(bottomExtremity.isEmpty()||topExtremity.isEmpty()){
            return 0;
        }
        for(Point p: placedCards.pointsList()){
            /*
            p is a possible side point only if there is a point (a,b) in the possible column points such that (a+xOffset,b+yOffset)=(p.getX(),p.getY())
            or if, depending on the y offset a bottom or top point exists such that it can complete a sequence
            */
            if(placedCards.get(p).getCardColour()==sideColour){
                if(possibleColumnPoints.contains(new Point(p.getX()-xOffset,p.getY()-yOffset))||topExtremity.contains(new Point(p.getX()-xOffset,p.getY()-yOffset))||bottomExtremity.contains(new Point(p.getX()-xOffset,p.getY()-yOffset))){
                    possibleSidePoints.add(p);
                }
            }
        }
        if(possibleSidePoints.isEmpty()){
            return 0;
        }
        //If the side piece is on the bottom side then the sequence will be calculated top-to-bottom, otherwise it will be calculated bottom-to-top
        return findSequences((yOffset==-1?topExtremity:bottomExtremity),(yOffset==-1?bottomExtremity:topExtremity),possibleSidePoints,possibleColumnPoints,xOffset,yOffset);
    }
    /**
     * Method used to aid in the calculation of which point should be considered in an objective sequence
     * @param possibleSequencePoints the list of all candidates of the required color, in  the end it will only contain points that
     *                               are in the middle of a sequence (so not in topExtremities or bottomExtremities) or will be empty
     * @param topExtremities is the list of points that are on the top of the sequence, initially empty, it is updated by the method
     * @param bottomExtremities is the lists on the bottom of the sequence
     * @param topAdj is the location of the point on top of p
     * @param bottomAdj is the location of the point below p
     * @param p is the point that will be moved between the lists
     */
    private synchronized void updateExtremityLists(List<Point> toRemove,List<Point> possibleSequencePoints, List<Point> topExtremities, List<Point> bottomExtremities, Point topAdj, Point bottomAdj, Point p) {
        if(!possibleSequencePoints.contains(topAdj)&&!possibleSequencePoints.contains(bottomAdj)){
            //if a point is isolated it is removed from the List of valid points
            toRemove.add(p);
        }
        else if(possibleSequencePoints.contains(topAdj)&&!possibleSequencePoints.contains(bottomAdj)){
            //if a point has no other point on the bottom left then it is a bottom extremity
            bottomExtremities.add(p);
            toRemove.add(p);
        }
        else if(!possibleSequencePoints.contains(topAdj)&&possibleSequencePoints.contains(bottomAdj)){
            //if a point has no other point on the top right then it is a top extremity
            topExtremities.add(p);
            toRemove.add(p);
        }
    }
    /**
     * This method considers the cards of each column two by two, if a sequence is found with those two cards then it will jump to the third and fourth(if they exist) and so on until an endingExtremity is found;
     * if a sequence isn't found it discards the card closest to the startingExtremity and consider the second and third(if the third exists) and continues until it hits an endingExtremity
     * @param startingExtremities list of top/bottom extremities from which the method starts finding sequences
     * @param endingExtremities list used as an upper/lower bound for the columns
     * @param possibleSidePoints list of the points that could complete a sequence
     * @param xOffset determines if the side piece is left(-1) or right(+1)
     * @param direction determines if the sequences are calculated top-to-bottom(-1) or borrom-to-top(+1)
     * @return number of sequences found
     */
    private synchronized int findSequences(List<Point> startingExtremities,List<Point> endingExtremities, List<Point> possibleSidePoints,List<Point> possibleColumnPoints,int xOffset,int direction) {
        int found=0;
        for(Point start:startingExtremities){
            int columnX=start.getX();
            Point slider=new Point(columnX,start.getY()+2*direction);
            if(possibleSidePoints.contains(new Point(columnX+xOffset,slider.getY()+direction))){
                found++;
                slider= new Point(columnX,slider.getY()+4*direction);
            }else{
                slider= new Point(columnX,slider.getY()+2*direction);
            }
            //until local end is part of at least one of the lists
            while(possibleColumnPoints.contains(slider)||endingExtremities.contains(slider)){
                if(possibleSidePoints.contains(new Point(columnX+xOffset,slider.getY()+direction))){
                    found++;
                    slider= new Point(columnX,slider.getY()+4*direction);
                }else{
                    slider= new Point(columnX,slider.getY()+2*direction);
                }
            }
        }
        return found;
    }


    public List<SimpleCard> getCardsAsSimpleCards() {
        List<PlayableCard> cards= placedCards.cardsList();
        List<SimpleCard> simpleCards= new ArrayList<>();
        for(int i=0;i<cards.size();i++){
            Point pos;
            boolean facing;
            try {
                pos= cards.get(i).getPosition();
                facing= cards.get(i).isFacingUp();
            }catch (NotPlacedException e){
                throw new RuntimeException();
            }
            simpleCards.add(new SimpleCard(pos.getX(), pos.getY(),facing,cards.get(i).getID() ));
        }
        return simpleCards;
    }
}