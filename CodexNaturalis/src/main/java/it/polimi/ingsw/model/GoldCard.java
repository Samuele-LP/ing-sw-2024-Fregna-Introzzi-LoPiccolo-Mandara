package it.polimi.ingsw.model;


import java.util.HashMap;
import java.util.Map;

public class GoldCard extends PlayableCard{
    private final int requiredFungi,requiredPlant,requiredAnimal,requiredInsect;
    private final int awardedPoints;
    private final TokenType pointsCondition;
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
    @Override
    public String printCardInfo() {
        String s=super.printCardInfo()+"This card gives "+awardedPoints+" points ";
        if(pointsCondition==TokenType.scroll||pointsCondition==TokenType.quill||pointsCondition==TokenType.ink){
            s = s+"for every "+pointsCondition.toString()+" in your playing area\n";
        }
        else if(pointsCondition==TokenType.blocked){
            s = s+"for every corner blocked by this card\n";
        }
        else {
            s= s+"\n";
        }
        return s;
    }
}