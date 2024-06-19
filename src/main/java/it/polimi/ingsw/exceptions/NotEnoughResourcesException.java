package it.polimi.ingsw.exceptions;

/**
 * Thrown if a player tries to place a gold card without enough resources to actually do so
 */
public class NotEnoughResourcesException extends Exception {
    public NotEnoughResourcesException(){
        super();
    }
}
