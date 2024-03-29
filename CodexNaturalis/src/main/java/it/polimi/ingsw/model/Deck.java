package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.List;
import it.polimi.ingswaaa.exceptions.NoVisibleCardException;
import it.polimi.ingswaaa.exceptions.EmptyDeckException;

/**
 *This class aim to manage the deck for playable and objective cards
 */

public class Deck {
    private int numRemaining;
    private List<Card> cards;
    private Card firstVisible;
    private Card secondVisible;

    public Deck(List<Card> cards) {
        this.cards = cards;
        this.numRemaining = cards.size();
        firstVisible=cards.removeFirst();
        numRemaining--;
        secondVisible=cards.removeFirst();
        numRemaining--;
    }
    /**
     * @return numRemaining
     */
    public int getNumRemaining() {
        return numRemaining;
    }

    /**
     * @return firstVisible
     */
    public Card getFirstVisible() {
        return firstVisible;
    }

    /**
     * @return secondVisible
     */
    public Card getSecondVisible() {
        return secondVisible;
    }
    /**
     *This method can be used to randomize the position of the cards in the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * @return drawnCard
     * @param choice refers to where the player want to draw the card from: 0 from
     * the deck, 1 to pick the first visible card on the table, 2 to draw the second visible card
     *
     */
    public Card draw(int choice) throws Exception {
        Card drawnCard;
        switch(choice){
            case 0:
                if(cards.isEmpty()){
                    throw new EmptyDeckException();
                }
                drawnCard=cards.removeFirst();
                numRemaining--;
                break;
            case 1:
                if(firstVisible==null){
                    throw new NoVisibleCardException();
                }
                drawnCard=firstVisible;
                firstVisible=null;
                setFirstVisible();
                break;

            case 2:
                if(secondVisible==null){
                    throw new NoVisibleCardException();
                }
                drawnCard=secondVisible;
                secondVisible=null;
                setSecondVisible();
                break;
            default:
                throw new IllegalArgumentException("Invalid choice");
        }
        return drawnCard;
    }

    /**
     *  this method is used to place the first visible card on the table if it has already been drawn
     */

    private void setFirstVisible() throws Exception {
        if (firstVisible != null) {
            throw new IllegalStateException("Card already on the table");
        }
        Card drawnCard;
        drawnCard = draw(0);
        firstVisible = drawnCard;
    }

    /**
     * This method is used to place the second visible card on the table
     * if it has already been drawn
     */

    private void setSecondVisible() throws Exception {
        if (secondVisible != null) {
            throw new IllegalStateException("Card already on the table");
        }
        Card drawnCard;
        drawnCard = draw(0);
        secondVisible = drawnCard;
    }

}
