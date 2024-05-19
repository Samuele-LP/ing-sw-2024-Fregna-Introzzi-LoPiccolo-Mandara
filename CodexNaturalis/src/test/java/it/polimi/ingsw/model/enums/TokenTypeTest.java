package it.polimi.ingsw.model.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTypeTest {

    @Test
    public void testToString() {
        for(TokenType t: TokenType.values()){
            System.out.println(t.toString());
        }
    }
}