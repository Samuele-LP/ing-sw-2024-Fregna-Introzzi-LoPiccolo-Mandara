package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.exceptions.CantReplaceVisibleCardException;
import it.polimi.ingsw.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.exceptions.NoVisibleCardException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.player.Player;

/**
 *This class aim to manage the deck for playable and objective cards
 */

public class Deck {
    private final List<Card> cards;
    private Card firstVisible;
    private Card secondVisible;

    /**
     * Creates a deck given a list of cardsw
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        firstVisible=null;
        secondVisible=null;
    }
    public synchronized int getNumRemaining(){
        return cards.size();
    }
    /**
     *Used to get information on the top card of the deck, to be used by the view to determine what to show
     * @return the top card of the deck's id, -1 if the deck is empty
     * @deprecated may be removed as it's not used outside of tests
     */
    @Deprecated
    public synchronized int getTopCardID(){
        return cards.isEmpty()?-1: cards.getFirst().getID();
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
     * @return firstVisible card\
     */
    public synchronized Card getFirstVisible() {
        return firstVisible;
    }

    /**
     * @return secondVisible card
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
     * @return drawnCard
     * @param choice refers to where the player want to draw the card from: 0 from
     * the deck, 1 to pick the first visible card on the table, 2 to draw the second visible card
     *
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
     * if it has already been drawn
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
     * @throws Exception when a card cant be drawn from any deck, starts the final game phase
     */
    private void setFirstVisible(Deck otherDeck) throws Exception {
        firstVisible=otherDeck.draw(0);
    }
    /**
     * This method is used to place the second visible card on the table
     * if it has already been drawn
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
     * @throws Exception when a card cant be drawn from any deck, starts the final game phase
     */
    private void setSecondVisible(Deck otherDeck) throws Exception {
        secondVisible=otherDeck.draw(0);
    }

    /**
     * Method used to set the visible cards after a player has drawn; if a deck finishes then it tries to draw from the other deck
     * @param otherDeck is the other gold/resource deck
     * @throws Exception when a card cant be drawn from both decks, the final phase must start
     */
    public synchronized void setVisibleAfterDraw(Deck otherDeck)throws Exception{
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
