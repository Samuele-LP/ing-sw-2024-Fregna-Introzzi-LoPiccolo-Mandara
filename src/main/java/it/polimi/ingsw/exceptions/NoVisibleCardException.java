package it.polimi.ingsw.exceptions;

public class NoVisibleCardException extends Exception {
    public NoVisibleCardException() {
        super("The visible card you are trying to draw isn't on the table");
    }
}