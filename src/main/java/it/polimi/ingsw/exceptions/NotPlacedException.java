package it.polimi.ingsw.exceptions;

/**
 * Thrown if a fatal error has occurred in the program: a card that has not been placed yet has been accessed as if it was placed
 */
public class NotPlacedException extends Exception {
    public NotPlacedException(){
        super();
    }
}
