package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.List;

/**
 *
 * This class represents all types of ObjectiveCards
 */

public class ObjectiveCard extends Card{
    private int awardedPoints;
    private boolean isPositionalObjective;
    private ObjectiveSequence positionalRequirements;
    private List<TokenType> listRequirements;
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
    public int GetPoints(){
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
    public List<TokenType> getListRequirements(){
        return listRequirements;
    }

    /**
     * Gets the card's data as a String with this format:
     * Objective card: you get {awardedPoints} for every[ [set of 3 || set of quill, ink and scroll||pair of] {listRequirements} || positionalRequirements] you have in your playing area
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        String cardData = new String("Objective card: you get "+awardedPoints+" for every ");
        if(isPositionalObjective){
            cardData = cardData + positionalRequirements.toString();
        }
        else{
            if(ID<=98&&ID>=95){
                cardData = cardData + "set of 3 "+listRequirements.get(0).toString();
            }
            else if(ID==99){
                cardData = cardData +"set of quill, ink and scroll";
            }
            else{
                cardData= cardData +"pair of "+listRequirements.get(0).toString();
            }
        }
        cardData = cardData +" you have on your playing field";
        return cardData;
    }
}
