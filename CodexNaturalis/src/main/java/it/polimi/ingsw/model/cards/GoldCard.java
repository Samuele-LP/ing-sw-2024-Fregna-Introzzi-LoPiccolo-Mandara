package it.polimi.ingsw.model.cards;


import com.google.gson.Gson;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that adds the information necessary to handle a GoldCard
 */
public class GoldCard extends PlayableCard{
    private final int requiredFungi,requiredPlant,requiredAnimal,requiredInsect;
    private final int awardedPoints;
    private final TokenType pointsCondition;

    /**
     * @param ID id of the card
     * @param topRight top right corner
     * @param topLeft top left corner
     * @param bottomLeft bottom left corner
     * @param bottomRight bottom right corner
     * @param backTopRight back top right corner
     * @param backTopLeft back top left corner
     * @param backBottomLeft back bottom left corner
     * @param backBottomRight back bottom right corner
     * @param colour colour of the card
     * @param awardedPoints number of points given by fulfilling the conditions
     * @param pointsCondition conditions that awards points
     * @param requiredFungi number of required symbols to place
     * @param requiredPlant number of required symbols to place
     * @param requiredAnimal number of required symbols to place
     * @param requiredInsect number of required symbols to place
     */
    public GoldCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour, int awardedPoints, TokenType pointsCondition, int requiredFungi, int requiredPlant, int requiredAnimal, int requiredInsect) {
        super(ID, topRight, topLeft, bottomLeft, bottomRight, backTopRight, backTopLeft, backBottomLeft, backBottomRight, colour);
        this.requiredFungi = requiredFungi;
        this.requiredPlant = requiredPlant;
        this.requiredAnimal = requiredAnimal;
        this.requiredInsect = requiredInsect;
        this.awardedPoints=awardedPoints;
        this.pointsCondition=pointsCondition;
    }

    /**
     * Returns how many points are given for each time the condition is fulfilled
     * @return awardedPoints
     */
    public int getPoints(){
        return awardedPoints;
    }
    /**
     * This method returns "blocked" if the condition for points is blocking corners and value "empty" if there is no condition
     * @return pointsCondition
     */
    public TokenType getPointsCondition() {
        return pointsCondition;
    }

    /**
     * This method returns a map between the four possible TokenTypes that are required to play the card
     * @return conditions
     */
    public Map<TokenType,Integer> getPlacementConditions(){
        Map<TokenType,Integer> conditions = new HashMap<>();
        conditions.put(TokenType.animal,requiredAnimal);
        conditions.put(TokenType.fungi,requiredFungi);
        conditions.put(TokenType.plant,requiredPlant);
        conditions.put(TokenType.insect,requiredInsect);
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
        String cardData=super.printCardInfo()+"This card gives "+awardedPoints+" points ";
        if(pointsCondition==TokenType.scroll||pointsCondition==TokenType.quill||pointsCondition==TokenType.ink){
            cardData = cardData+"for every "+pointsCondition.toString()+" in your playing area\n";
        }
        else if(pointsCondition==TokenType.blocked){
            cardData = cardData+"for every corner blocked by this card\n";
        }
        else {
            cardData= cardData+"\n";
        }
        return cardData;
    }
    /**
     * This method returns the ascii art for the front of the card
     * @return an array containing two strings. The first is the top line, the second is the bottom line.
     */
    @Override
    public String[] asciiArtFront() {
        String[] art=super.asciiArtFront();
        art[0] =topLeft+"\u001B[43m"+"   "+"\u001B[0m"+topRight;
        return art;
    }
}