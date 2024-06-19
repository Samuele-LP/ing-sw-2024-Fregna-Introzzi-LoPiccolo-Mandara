package it.polimi.ingsw.exceptions;

/**
 * Thrown if a Card's methods for placement are called after it has already been placed
 */
public class AlreadyPlacedException extends Exception {
    public AlreadyPlacedException(){
        super();
    }
}
