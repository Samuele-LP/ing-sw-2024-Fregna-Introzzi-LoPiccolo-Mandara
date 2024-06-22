package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents all types of ObjectiveCards.
 */
public class ObjectiveCard extends Card{

    private final int awardedPoints;

    private final boolean isPositionalObjective;

    private final ObjectiveSequence positionalRequirements;

    private final List<TokenType> listRequirements;

    /**
     * Constructor for ObjectiveCard.
     *
     * @param awardedPoints         number of points given by fulfilling the conditions
     * @param ID                    id of the card
     * @param isPositionalObjective flag that represents whether this card needs a sequence or a set of symbols to score points
     * @param positionalRequirements requirements if isPositionalObjective is true
     * @param listRequirements      requirements if isPositionalObjective is false
     */
    public ObjectiveCard(int awardedPoints, int ID, boolean isPositionalObjective, ObjectiveSequence positionalRequirements, List<TokenType> listRequirements) {
        this.awardedPoints = awardedPoints;
        this.ID = ID;
        this.isPositionalObjective = isPositionalObjective;
        this.positionalRequirements = positionalRequirements;
        this.listRequirements = listRequirements;
    }

    /**
     * Checks if the objective represented by this card involves a sequence of placed cards.
     *
     * @return true if the objective is positional, false otherwise
     */
    public boolean isPositional(){return isPositionalObjective;}

    /**
     * Gets the points awarded by this objective card.
     *
     * @return awardedPoints
     */
    public int getPoints(){
        return awardedPoints;
    }

    /**
     * Gets the positional requirements of this objective card.
     *
     * @return positionalRequirements
     */
    public ObjectiveSequence getPositionalRequirements(){
        return positionalRequirements;
    }

    /**
     * Gets the list requirements of this objective card as a map.
     *
     * @return a map of token types and their counts
     */
    public Map<TokenType,Integer> getListRequirementsAsMap(){
        Map<TokenType,Integer> objectiveRequirements = new HashMap<>();

        for (TokenType t : this.listRequirements) {
            if (!objectiveRequirements.containsKey(t)) {
                objectiveRequirements.put(t, 1);
            } else {
                objectiveRequirements.put(t, objectiveRequirements.get(t) + 1);
            }
        }

        return objectiveRequirements;
    }

    /**
     * Gets the card's data as a String containing an ASCII art representation, with lines separated by an 'X'.
     *
     * @return card data as a formatted String
     */
    @Override
    public String printCardInfo() {
        StringBuilder cardData = new StringBuilder(super.printCardInfo() + "XGives " + awardedPoints + " pointsX");

        if (isPositionalObjective) {
            cardData.append(positionalRequirements.toString());
        } else {
            for (TokenType t : listRequirements) {
                cardData.append("|   ").append(t).append("   |X");
            }
        }

        return cardData.toString();
    }
}
