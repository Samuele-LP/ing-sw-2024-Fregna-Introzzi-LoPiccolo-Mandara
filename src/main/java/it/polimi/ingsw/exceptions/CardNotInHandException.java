package it.polimi.ingsw.exceptions;

/**
 * Thrown when a card that is not in the hand of a player is set as the card to be placed
 */
public class CardNotInHandException extends Exception {
    public CardNotInHandException(){super();}
}
