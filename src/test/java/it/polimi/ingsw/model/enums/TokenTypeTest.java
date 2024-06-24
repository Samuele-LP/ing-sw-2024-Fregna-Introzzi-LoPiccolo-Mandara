package it.polimi.ingsw.model.enums;

import org.junit.Test;

/**
 * Test class to control the String representation of {@link TokenType}
 */
public class TokenTypeTest {

    @Test
    public void testToString() {
        for (TokenType t : TokenType.values()) {
            System.out.println(t.toString());
        }
    }
}