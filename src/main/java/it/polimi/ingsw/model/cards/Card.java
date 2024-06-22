package it.polimi.ingsw.model.cards;

/**
 * Abstract class used to represent a generic card with its ID
 */
public abstract class Card {

    int ID;

    /**
     *Returns ID of a Card
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets card data as String for the CLI
     */
    public String printCardInfo(){
        return new String("Card ID: " + ID);
    }
}
