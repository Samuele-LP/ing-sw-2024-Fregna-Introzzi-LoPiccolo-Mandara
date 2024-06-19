package it.polimi.ingsw.exceptions;

/**
 * Thrown when a drawn card is received by a player with 3 cards in their hand
 */
public class HandAlreadyFullException extends Exception {
    public HandAlreadyFullException(){
        super();
    }
}
