package it.polimi.ingsw.model.enums;

import org.junit.Test;

public class CardTypeTest {

    @Test
    public void testToString() {
        for(CardType t: CardType.values()){
            System.out.println(t.toString());
        }
    }

    @Test
    public void fullString() {
        for(CardType t: CardType.values()){
            System.out.println(t.fullString());
        }
    }
}