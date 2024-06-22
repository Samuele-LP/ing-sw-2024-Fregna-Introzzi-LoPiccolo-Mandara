package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that adds the information necessary to handle a GoldCard.
 */
public class GoldCard extends PlayableCard{

    private final int requiredFungi,requiredPlant,requiredAnimal,requiredInsect;

    private final int awardedPoints;

    private final TokenType pointsCondition;

    /**
     * Constructs a GoldCard with the specified parameters.
     *
     * @param ID              id of the card
     * @param topRight        top right corner symbol
     * @param topLeft         top left corner symbol
     * @param bottomLeft      bottom left corner symbol
     * @param bottomRight     bottom right corner symbol
     * @param backTopRight    back top right corner symbol
     * @param backTopLeft     back top left corner symbol
     * @param backBottomLeft  back bottom left corner symbol
     * @param backBottomRight back bottom right corner symbol
     * @param colour          colour of the card
     * @param awardedPoints   number of points given by fulfilling the conditions one time
     * @param pointsCondition the condition on which points are calculated,
     *                        if it's TokenType.Empty then there is no condition,
     *                        if it's TokenType.Blocked then the points are awarded for each corner covered by this card
     * @param requiredFungi   number of required fungi symbols to place
     * @param requiredPlant   number of required plant symbols to place
     * @param requiredAnimal  number of required animal symbols to place
     * @param requiredInsect  number of required insect symbols to place
     */
    public GoldCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour, int awardedPoints, TokenType pointsCondition, int requiredFungi, int requiredPlant, int requiredAnimal, int requiredInsect) {
        super(ID, topRight, topLeft, bottomLeft, bottomRight, backTopRight, backTopLeft, backBottomLeft, backBottomRight, colour);

        this.requiredFungi = requiredFungi;
        this.requiredPlant = requiredPlant;
        this.requiredAnimal = requiredAnimal;
        this.requiredInsect = requiredInsect;
        this.awardedPoints = awardedPoints;
        this.pointsCondition = pointsCondition;
    }

    /**
     * Returns the number of points given for each time the condition is fulfilled.
     *
     * @return the awarded points
     */
    public int getPoints(){
        return awardedPoints;
    }

    /**
     * Returns the condition for awarding points.
     *
     * @return the points condition
     */
    public TokenType getPointsCondition() {
        return pointsCondition;
    }

    /**
     * Returns a map of the placement conditions for the card.
     *
     * @return a map where the keys are the required TokenTypes and the values are the required amounts
     */
    public Map<TokenType,Integer> getPlacementConditions(){
        Map<TokenType,Integer> conditions = new HashMap<>();

        conditions.put(TokenType.animal, requiredAnimal);
        conditions.put(TokenType.fungi, requiredFungi);
        conditions.put(TokenType.plant, requiredPlant);
        conditions.put(TokenType.insect, requiredInsect);

        return conditions;
    }

    /**
     * Gets the card's data as a String with this format:
     * CardType: [CardColor||    ]
     * Front of the card: {FrontCorners}
     * Back of the card : {BackCorners}
     * This card gives {awardedPoints} points for every [{pointsCondition} in your playing area|| for every corner blocked by this card
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        String cardData = super.printCardInfo() + "This card gives " + awardedPoints + " points ";

        if(pointsCondition == TokenType.scroll || pointsCondition == TokenType.quill || pointsCondition == TokenType.ink) {
            cardData = cardData + "for every " + pointsCondition + " in your playing area\n";
        } else if(pointsCondition==TokenType.blocked){
            cardData = cardData + "for every corner blocked by this card\n";
        } else {
            cardData = cardData + "\n";
        }

        cardData = cardData + "Symbols required to play this card: " + TokenType.fungi+": "+ requiredFungi+"; ";
        cardData = cardData + TokenType.animal + ": " + requiredAnimal + "; ";
        cardData = cardData + TokenType.insect + ": " + requiredInsect + "; ";
        cardData = cardData + TokenType.plant + ": " + requiredPlant + "\n";

        return cardData;
    }

    /**
     * Returns the ASCII art for the front of the card.
     *
     * @return an array containing two strings: the first is the top line, the second is the bottom line
     */
    @Override
    public String[] asciiArtFront() {
        String[] art=super.asciiArtFront();
        art[0] = topLeft + "\u001B[43m" + "   " + "\u001B[0m" + topRight;

        return art;
    }
}