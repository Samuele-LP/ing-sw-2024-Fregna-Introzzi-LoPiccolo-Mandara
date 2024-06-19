package it.polimi.ingsw.exceptions;

/**
 * Thrown if somehow the player has already a secret objective but another one is being set
 */
public class ObjectiveAlreadySetException extends Exception {
    public ObjectiveAlreadySetException(){
        super();
    }
}
