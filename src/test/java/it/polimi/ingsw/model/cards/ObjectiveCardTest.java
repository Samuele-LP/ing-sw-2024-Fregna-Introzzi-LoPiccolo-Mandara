package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Test;

import java.util.ArrayList;

public class ObjectiveCardTest {
    /**
     * Tests that the information on the card is printed correctly
     */
    @Test
    public void ObjectiveTest() {
        ObjectiveCard objCard = new ObjectiveCard(1, 100, true, ObjectiveSequence.greenAntiDiagonal, new ArrayList<TokenType>());
        for (String s : objCard.printCardInfo().split("X")) {
            System.out.println(s);
        }
        System.out.println();
        ArrayList<TokenType> list = new ArrayList<TokenType>();
        list.add(TokenType.quill);
        list.add(TokenType.ink);
        list.add(TokenType.quill);
        objCard = new ObjectiveCard(1, 101, false, ObjectiveSequence.redDiagonal, list);
        for (String s : objCard.printCardInfo().split("X")) {
            System.out.println(s);
        }
    }
}