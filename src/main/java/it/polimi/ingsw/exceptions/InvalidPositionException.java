package it.polimi.ingsw.exceptions;

/**
 * Thrown when a player tries to place in a position that is either already occupied or impossible to occupy
 */
public class InvalidPositionException extends Exception {
    public InvalidPositionException(){super();}
}
