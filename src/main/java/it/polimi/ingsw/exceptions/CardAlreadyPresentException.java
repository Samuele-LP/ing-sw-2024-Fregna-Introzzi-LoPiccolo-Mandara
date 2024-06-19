package it.polimi.ingsw.exceptions;

/**
 * Thrown when the decks try to replace an already existing visible card
 */
public class CardAlreadyPresentException extends Exception {
    public CardAlreadyPresentException(){
        super();
    }
}
