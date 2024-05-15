package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class StartingCardTest {
    /**
     * Tests that the information on the card is printed correctly in all cases
     */
    @Test
    public void StartingTest(){
        StartingCard st = new StartingCard(82, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi,TokenType.insect,TokenType.quill,TokenType.insect);
        System.out.println(st.printCardInfo());
    }
}