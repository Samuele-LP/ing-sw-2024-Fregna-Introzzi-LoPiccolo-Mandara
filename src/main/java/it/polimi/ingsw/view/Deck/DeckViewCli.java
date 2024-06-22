package it.polimi.ingsw.view.Deck;

import it.polimi.ingsw.view.Deck.DeckView;
import it.polimi.ingsw.view.GameViewCli;

import java.io.IOException;

/**
 * Class that provides a CLI view of the deck.
 */
public class DeckViewCli extends DeckView {

    /**
     * Constructor for DeckViewCli.
     *
     * @param type the type of the deck (e.g., "Gold", "Resource")
     * @throws IOException if there is an error initializing the deck view
     */
    public DeckViewCli(String type) throws IOException {
        super(type);
    }

    /**
     * Prints the deck's information for the CLI.
     *
     * @return an array of 10 Strings representing the deck's state for the CLI
     */
    public String[] printDeck(){
        String[] lines = new String[10];
        lines[0] = type + " deck.";

        if (topCard != null) {
            lines[1] = "The top card's type is " + topCard.fullString();
            //26
        } else {
            lines[1] = "The deck has no more cards in it.";
            //33
        }

        if (firstVisible != null) {
            lines[2] = "First visible id is: " + firstVisible;//23
            String[]card = GameViewCli.printCardAsciiFront(firstVisible);
            lines[3] = "|" + card[0] + "|";
            lines[4] = "|" + card[1] + "|";
            lines[5] = "|" + card[2] + "|";
        } else {
            lines[2] = "There is no first visible card";//30
            lines[3] = "           ";
            lines[4] = "           ";
            lines[5] = "           ";
        }

        if (secondVisible != null) {
            lines[6] = "The second visible card's id is: " + secondVisible;//35
            String[]card = GameViewCli.printCardAsciiFront(secondVisible);
            lines[7] = "|" + card[0] + "|";
            lines[8] = "|" + card[1] + "|";
            lines[9] = "|" + card[2] + "|";
        } else {
            lines[6] = "There is no second visible card";//31
            lines[7] = "           ";
            lines[8] = "           ";
            lines[9] = "           ";
        }

        return lines;
    }
}
