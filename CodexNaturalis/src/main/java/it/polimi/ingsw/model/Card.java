package it.polimi.ingsw.model;

public abstract class Card {

    int ID;

    /**
     *Returns ID of a Card
     * @return ID
     */
    public int getID(){
        return ID;
    }

    /**
     * Prints card data for the CLI
     */
    public String printCardInfo(){
        return "";
    }
}
