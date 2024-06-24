package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.view.Deck.DeckViewCli;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests that the Deck's CLI output is formatted correctly, it should be like this:<br>
 * DeckType deck.
 * The top card's type is animal      || The deck has no more cards in it.<br>
 * The first visible card's id is: n  || There is no first visible card<br>
 * The second visible card's id is: n || There is no second visible card<br>
 */
public class DeckViewCliTest {
    DeckViewCli deck = new DeckViewCli("TestType");

    public DeckViewCliTest() throws IOException {
    }

    @Before
    public void setUp() {
        GameView gv = new GameViewCli();
        deck.update(CardType.fungi, 2, 19);
        printDeck();
        deck.update(null, 9, 7);
        printDeck();
        deck.update(null, null, 3);
        printDeck();
        deck.update(null, 36, null);
        printDeck();
        deck.update(null, null, null);
    }

    @Test
    public void printDeck() {
        for (String s : deck.printDeck()) {
            System.out.println(s);
        }
    }

}