package it.polimi.ingsw.exceptions;

/**
 * Thrown when a player blocks every possible new move in their field
 */
public class PlayerCantPlaceAnymoreException extends Exception {
    public PlayerCantPlaceAnymoreException(){
        super();
    }
}
