package it.polimi.ingsw.view;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;
import java.util.List;

/**
 * Used to memorize the necessary information about the decks to be shown to the player
 */
public class DeckView {
    private final String type;
    private CardType topCard;
    private Integer firstVisible;
    private Integer secondVisible;
    private final List<Card> cards;
    public DeckView(String type) throws IOException {
        this.type=type;
        cards=Creation.getResourceCards();
        cards.addAll(Creation.getGoldCards());
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
            System.out.println("The top card's type is "+topCard.fullString());
        }else{
            System.out.println("The deck has no more cards in it.");
        }
        if(firstVisible!=null){
            System.out.println("The first visible card's id is: "+firstVisible);
            printCard(firstVisible);
        }else{
            System.out.println("There is no first visible card");
        }
        if(secondVisible!=null){
            System.out.println("The second visible card's id is: "+secondVisible);
            printCard(secondVisible);
        }else{
            System.out.println("There is no second visible card");
        }
        System.out.println();
    }

    /**
     * Prints the card with the specified id
     * */
    private void printCard(int id) {
        PlayableCard card= (PlayableCard) cards.get(id-1);
        String[] asciiArt= card.asciiArtFront();
        System.out.println("|"+asciiArt[0]+"|");
        System.out.println("|"+asciiArt[1]+"|");
        System.out.println("|"+asciiArt[2]+"|");
        System.out.println();
    }
}
