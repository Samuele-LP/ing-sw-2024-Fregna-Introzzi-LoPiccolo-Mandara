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
 * This class manages the deck for playable and objective cards.
 */
public class Deck {
    private final List<Card> cards;
    private Card firstVisible;
    private Card secondVisible;

    /**
     * Creates a deck given a list of cards.
     *
     * @param cards the list of cards to initialize the deck
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        firstVisible=null;
        secondVisible=null;
    }

    /**
     * Used to determine whether the final phase has started.
     *
     * @return the number of remaining cards
     */
    public synchronized int getNumRemaining(){
        return cards.size();
    }

    /**
     * Used to get information on the top card of the deck, to be used by the view to determine what to show.
     *
     * @return null if the deck is empty, otherwise the CardType attribute of the top card
     */
    public synchronized CardType getTopCardColour() {
        if(cards.isEmpty()){
            return null;
        }

        PlayableCard topCard = (PlayableCard) cards.getFirst();

        return topCard.getCardColour();
    }

    /**
     * @return the first visible card
     */
    public synchronized Card getFirstVisible() {
        return firstVisible;
    }

    /**
     * @return the second visible card
     */
    public synchronized Card getSecondVisible() {
        return secondVisible;
    }

    /**
     * This method can be used to randomize the position of the cards in the deck.
     */
    public synchronized void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card from the deck.
     *
     * @param choice refers to where the player wants to draw the card from:
     *               0 to draw from the deck,
     *               1 to pick the first visible card on the table,
     *               2 to draw the second visible card
     * @return the drawn card
     * @throws EmptyDeckException    when a card is being drawn from the top of an empty deck
     * @throws NoVisibleCardException when a player tries to draw from a non-existent visible card position
     */
    public synchronized Card draw(int choice) throws EmptyDeckException, NoVisibleCardException {
        Card drawnCard;
        switch(choice){
            case 0:
                if (cards.isEmpty()) {
                    throw new EmptyDeckException();
                }

                drawnCard = cards.removeFirst();

                break;
            case 1:
                if (firstVisible == null) {
                    throw new NoVisibleCardException();
                }

                drawnCard = firstVisible;
                firstVisible = null;

                break;
            case 2:
                if (secondVisible == null) {
                    throw new NoVisibleCardException();
                }

                drawnCard = secondVisible;
                secondVisible = null;

                break;
            default:
                throw new IllegalArgumentException("Invalid choice");
        }

        return drawnCard;
    }

    /**
     * Sets the first visible card on the table.
     *
     * @throws CantReplaceVisibleCardException if there are no cards left to set as visible
     * @throws CardAlreadyPresentException if there is already a first visible card
     */
    public synchronized void setFirstVisible() throws CantReplaceVisibleCardException, CardAlreadyPresentException {
        if (cards.isEmpty()) {
            throw new CantReplaceVisibleCardException("Visible card must be procured from another deck ");
        }

        if (firstVisible != null) {
            throw new CardAlreadyPresentException();
        }

        firstVisible=cards.removeFirst();
    }

    /**
     * Sets the first visible card from another deck if the current deck is empty.
     *
     * @param otherDeck the other deck to draw the visible card from
     * @throws EmptyDeckException if the other deck is also empty
     */
    private void setFirstVisible(Deck otherDeck) throws EmptyDeckException {
        try {
            firstVisible = otherDeck.draw(0);
        } catch (NoVisibleCardException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets the second visible card on the table.
     *
     * @throws CantReplaceVisibleCardException if there are no cards left to set as visible
     * @throws CardAlreadyPresentException if there is already a second visible card
     */
    public synchronized void setSecondVisible() throws CantReplaceVisibleCardException, CardAlreadyPresentException {
        if (cards.isEmpty()) {
            throw new CantReplaceVisibleCardException("Visible card must be procured from another deck ");
        }

        if(secondVisible != null){
            throw new CardAlreadyPresentException();
        }

        secondVisible = cards.removeFirst();
    }

    /**
     * Sets the second visible card from another deck if the current deck is empty.
     *
     * @param otherDeck the other deck to draw the visible card from
     * @throws EmptyDeckException if the other deck is also empty
     */
    private void setSecondVisible(Deck otherDeck) throws EmptyDeckException {
        try {
            secondVisible = otherDeck.draw(0);
        } catch (NoVisibleCardException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets the visible cards after a player has drawn. If a deck finishes, it tries to draw from the other deck.
     *
     * @param otherDeck the other deck to draw the visible card from
     * @throws CantReplaceVisibleCardException if no visible card can be drawn from both decks, the final phase must start
     * @throws CardAlreadyPresentException if an error has occurred
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
        } catch(EmptyDeckException e) {
            throw new CantReplaceVisibleCardException("");
        }
    }
}
