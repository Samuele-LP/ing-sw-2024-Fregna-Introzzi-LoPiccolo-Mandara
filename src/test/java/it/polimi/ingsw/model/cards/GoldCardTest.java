package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Test;

public class GoldCardTest {
    /**
     * Tests that the information on the card is printed correctly in all cases
     */
    @Test
    public void GoldTest(){
        GoldCard gc= new GoldCard(45, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi,2,TokenType.quill,0,0,0,0);
        System.out.println(gc.printCardInfo());
        gc= new GoldCard(45, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi,2,TokenType.blocked,0,0,0,0);
        System.out.println(gc.printCardInfo());
        gc= new GoldCard(45, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty, TokenType.empty,
                TokenType.empty, TokenType.empty, CardType.fungi,2,TokenType.empty,0,0,0,0);
        System.out.println(gc.printCardInfo());
        for(String s: gc.asciiArtFront()){
            System.out.println(s);
        }
        for(String s: gc.asciiArtBack()){
            System.out.println(s);
        }
    }
}