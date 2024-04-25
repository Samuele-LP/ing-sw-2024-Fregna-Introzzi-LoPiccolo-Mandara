package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.CardType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests that the Deck's CLI output is formatted correctly, it should be like this:<br>
 * DeckType deck.
 * The top card's type is animal      || The deck has no more cards in it.<br>
 * The first visible card's id is: n  || There is no first visible card<br>
 * The second visible card's id is: n || There is no second visible card<br>
 */
public class DeckViewTest {
    DeckView deck= new DeckView("TestType");
    @Before
    public void setUp(){
        deck.update(CardType.animal,2,19);
        printDeck();
        deck.update(null,9,7);
        printDeck();
        deck.update(null,null,3);
        printDeck();
        deck.update(null,36,null);
        printDeck();
        deck.update(null,null,null);
    }
    @Test
    public void printDeck() {
        deck.printDeck();
    }

}