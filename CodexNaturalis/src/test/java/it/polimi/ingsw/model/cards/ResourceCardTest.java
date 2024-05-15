package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceCardTest {
    /**
     * Tests that the information on the card is printed correctly in all cases
     */
    @Test
    public void ResourceTest() {
        ResourceCard rc = new ResourceCard(1, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi, 2);
        System.out.println(rc.printCardInfo());
        rc = new ResourceCard(1, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi, 0);
        System.out.println(rc.printCardInfo());
    }
}