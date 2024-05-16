package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * This class represents all types of ObjectiveCards
 */

public class ObjectiveCard extends Card{
    private final int awardedPoints;
    private final boolean isPositionalObjective;
    private final ObjectiveSequence positionalRequirements;
    private final List<TokenType> listRequirements;

    /**
     *
     * @param awardedPoints number of points given by fulfilling the conditions
     * @param ID id of the card
     * @param isPositionalObjective flag that represents whether this cards need a sequence or a set of symbols to score points
     * @param positionalRequirements requirements if isPositionalObjective is true
     * @param listRequirements requirements if isPositionalObjective is false
     */
    public ObjectiveCard(int awardedPoints, int ID, boolean isPositionalObjective, ObjectiveSequence positionalRequirements, List<TokenType> listRequirements) {
        this.awardedPoints = awardedPoints;
        this.ID = ID;
        this.isPositionalObjective = isPositionalObjective;
        this.positionalRequirements = positionalRequirements;
        this.listRequirements = listRequirements;
    }
    /**
     *True if the objective represented by this card involves a sequence of placed cards
     * @return isPositionalObjective
     */
    public boolean isPositional(){return isPositionalObjective;}
    /**
     *
     * @return awardedPoints
     */
    public int getPoints(){
        return awardedPoints;
    }
    /**
     *
     * @return positionalRequirements
     */
    public ObjectiveSequence getPositionalRequirements(){
        return positionalRequirements;
    }
    /**
     *
     * @return listRequirements
     */
    public Map<TokenType,Integer> getListRequirementsAsMap(){
        Map<TokenType,Integer> objectiveRequirements= new HashMap<>();
        for(TokenType t:this.listRequirements){
            if(!objectiveRequirements.containsKey(t)){
                objectiveRequirements.put(t,1);
            }
            else{
                objectiveRequirements.put(t,objectiveRequirements.get(t)+1);
            }
        }
        return objectiveRequirements;
    }

    /**
     * Gets the card's data as a String with this format:
     * Objective card: you get {awardedPoints} for every[ [set of 3 || set of quill, ink and scroll||pair of] {listRequirements} || positionalRequirements] you have in your playing area
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        String cardData = super.printCardInfo()+"XGives "+awardedPoints+" pointsX";
        if(isPositionalObjective){
            cardData= cardData+ positionalRequirements.toString();
        }else{
            for(TokenType t:listRequirements){
                cardData=cardData+"|   "+t+"   |X";
            }
        }
        return cardData;
    }
}
