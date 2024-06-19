package it.polimi.ingsw.exceptions;

/**
 * Thrown when a draw of the top card or a card replacement happens with an empty deck
 */
public class EmptyDeckException extends Exception {
    public EmptyDeckException(){
        super();
    }
}
