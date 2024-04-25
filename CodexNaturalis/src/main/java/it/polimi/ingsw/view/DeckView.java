package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.CardType;

/**
 * Used to memorize the necessary information about the decks to be shown to the player
 */
public class DeckView {
    private final String type;
    private CardType topCard;
    private Integer firstVisible;
    private Integer secondVisible;
    public DeckView(String type) {
        this.type=type;
    }

    /**
     * @param top colour of the current top of the deck
     * @param firstVisibleID id of the current first visible card
     * @param secondVisibleID id of the current second visible card
     */
    public void update(CardType top, Integer firstVisibleID, Integer secondVisibleID){
        topCard=top;
        firstVisible=firstVisibleID;
        secondVisible=secondVisibleID;
    }
    /**
     * Prints the deck's information for the cli
     */
    public void printDeck() {
        System.out.println("\n"+type+" deck.");
        if(topCard!=null){
            System.out.println("The top card's type is "+topCard);
        }else{
            System.out.println("The deck has no more cards in it.");
        }
        if(firstVisible!=null){
            System.out.println("The first visible card's id is: "+firstVisible);
        }else{
            System.out.println("There is no first visible card");
        }
        if(secondVisible!=null){
            System.out.println("The second visible card's id is: "+secondVisible);
        }else{
            System.out.println("There is no second visible card");
        }
        System.out.println();
    }
}
