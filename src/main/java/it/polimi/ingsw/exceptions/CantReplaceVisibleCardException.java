package it.polimi.ingsw.exceptions;

/**
 * Thrown when both decks are empty and a visible card is drawn
 */
public class CantReplaceVisibleCardException extends Exception {
    public CantReplaceVisibleCardException(String s) {super(s);
    }

}
