package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.CantReplaceVisibleCardException;
import it.polimi.ingsw.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.NoVisibleCardException;
import it.polimi.ingsw.model.enums.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *This class aims to manage the deck for playable and objective cards.
 */
public class Deck {
    private final List<Card> cards;
    private Card firstVisible;
    private Card secondVisible;

    /**
     * Creates a deck given a list of cards
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        firstVisible=null;
        secondVisible=null;
    }

    /**
     * Used to determine whether the final phase has started
     * @return the number of remaining cards
     */
    public synchronized int getNumRemaining(){
        return cards.size();
    }
    /**
     *Used to get information on the top card of the deck, to be used by the view to determine what to show
     * @return null if the deck is empty the CardType attribute of the top card otherwise
     */
    public synchronized CardType getTopCardColour(){
        if(cards.isEmpty()){
            return null;
        }
        PlayableCard topCard= (PlayableCard) cards.getFirst();
        return topCard.getCardColour();
    }
    /**
     * @return the id of the first visible card
     */
    public synchronized Card getFirstVisible() {
        return firstVisible;
    }

    /**
     * @return te id of the second visible card
     */
    public synchronized Card getSecondVisible() {
        return secondVisible;
    }
    /**
     *This method can be used to randomize the position of the cards in the deck
     */
    public synchronized void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * @return the id of the drawn card
     * @param choice refers to where the player want to draw the card from: 0 from
     * the deck, 1 to pick the first visible card on the table, 2 to draw the second visible card
     * @throws EmptyDeckException when a card is being drawn from the top of an empty deck
     * @throws NoVisibleCardException when a player tries to draw from a non-existent visible card position
     */
    public synchronized Card draw(int choice) throws EmptyDeckException, NoVisibleCardException {
        Card drawnCard;
        switch(choice){
            case 0:
                if(cards.isEmpty()){
                    throw new EmptyDeckException();
                }
                drawnCard=cards.removeFirst();
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
     * This method is used to place the second visible card on the table
     */
    public synchronized void setFirstVisible() throws CantReplaceVisibleCardException, CardAlreadyPresentException {
        if(cards.isEmpty()){
            throw new CantReplaceVisibleCardException("Visible card must be procured from another deck ");
        }
        if(firstVisible!=null){
            throw new CardAlreadyPresentException();
        }
        firstVisible=cards.removeFirst();
    }

    /**
     * Method called when one deck has no more cards but a visible card must be drawn
     * @param otherDeck is the other gold/resource deck
     * @throws EmptyDeckException when a card cant be drawn from any deck, starts the final game phase
     */
    private void setFirstVisible(Deck otherDeck) throws EmptyDeckException {
        try {
            firstVisible = otherDeck.draw(0);
        }catch (NoVisibleCardException e){
            throw new RuntimeException();
        }
    }
    /**
     * This method is used to place the second visible card on the table
     */
    public synchronized void setSecondVisible() throws CantReplaceVisibleCardException, CardAlreadyPresentException {
        if (cards.isEmpty()) {
            throw new CantReplaceVisibleCardException("Visible card must be procured from another deck ");
        }
        if(secondVisible!=null){
            throw new CardAlreadyPresentException();
        }
        secondVisible = cards.removeFirst();
    }
    /**
     * Method called when one deck has no more cards but a visible card must be drawn
     * @param otherDeck is the other gold/resource deck
     * @throws EmptyDeckException when a card cant be drawn from any deck, starts the final game phase
     */
    private void setSecondVisible(Deck otherDeck) throws EmptyDeckException {
        try {
            secondVisible = otherDeck.draw(0);
        }catch (NoVisibleCardException e){
            throw new RuntimeException();
        }
    }

    /**
     * Method used to set the visible cards after a player has drawn; if a deck finishes then it tries to draw from the other deck
     * @param otherDeck is the other gold/resource deck
     * @throws CantReplaceVisibleCardException Exception when a card cant be drawn from both decks, the final phase must start
     * @throws CardAlreadyPresentException when an error has occurred
     */
    public synchronized void setVisibleAfterDraw(Deck otherDeck) throws CardAlreadyPresentException, CantReplaceVisibleCardException {
        try {
            if (firstVisible == null) {
                try {
                    setFirstVisible();
                } catch (CantReplaceVisibleCardException e) {
                    setFirstVisible(otherDeck);
                }
            } else if (secondVisible == null) {
                try {
                    setSecondVisible();
                } catch (CantReplaceVisibleCardException e) {
                    setSecondVisible(otherDeck);
                }
            }
        }catch(EmptyDeckException e){
            throw new CantReplaceVisibleCardException("");
        }
    }
}
