package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.List;
import it.polimi.ingsw.exceptions.NoVisibleCardException;
import it.polimi.ingsw.exceptions.EmptyDeckException;

/**
 *This class aim to manage the deck for playable and objective cards
 */

public class Deck<T> {
    private int numRemaining;
    private List<T> cards;
    private T firstVisible;
    private T secondVisible;

    public Deck(List<T> cards) {
        this.cards = cards;
        this.numRemaining = cards.size();
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
    public T getFirstVisible() {
        return firstVisible;
    }

    /**
     * @return secondVisible
     */
    public T getSecondVisible() {
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

    public T draw(int choice) throws Exception {
        T drawnCard;

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
                break;

            case 2:
                if(secondVisible==null){
                    throw new NoVisibleCardException();
                }
                drawnCard=secondVisible;
                secondVisible=null;
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
        T drawnCard;
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
        T drawnCard;
        drawnCard = draw(0);
        secondVisible = drawnCard;
    }

}
