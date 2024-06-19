package it.polimi.ingsw.exceptions;

/**
 * Thrown when a player tries to draw from an empty visible card position
 */
public class NoVisibleCardException extends Exception {
    public NoVisibleCardException() {
        super("The visible card you are trying to draw isn't on the table");
    }
}