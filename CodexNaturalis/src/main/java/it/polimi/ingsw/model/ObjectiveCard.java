package it.polimi.ingsw.model;

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
////////////////
    /**
     *
     * @return true if the card's positionalRequirements matches one of the sequence in the ObjectiveSequence, false otherwise
     */
   /* public boolean isPositional() {
        ObjectiveSequence[] values = ObjectiveSequence.values();
        for (int i = 0; i < values.length; i++) {
            ObjectiveSequence sequenceControl = values[i];
            if (positionalRequirements == sequenceControl)
                return true;
        }
        return false;
    }*/
/////////////////////
    /**
     *
     * @return
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

}
