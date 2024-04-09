package it.polimi.ingsw.model.cards;

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
     * Gets card data as String for the CLI
     */
    public String printCardInfo(){
        return new String(String.valueOf(ID));
    };
}
