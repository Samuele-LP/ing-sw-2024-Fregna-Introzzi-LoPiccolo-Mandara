package it.polimi.ingsw.exceptions;

/**
 * Thrown when a card that is not a starting card is set to be placed as the first card
 */
public class IllegalStartingCardException extends Exception {
    public IllegalStartingCardException(){super();}
}
