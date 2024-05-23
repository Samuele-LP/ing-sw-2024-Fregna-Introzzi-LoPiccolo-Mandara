package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.ObjectiveSequence;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObjectiveCardTest {
    /**
     * Tests that the information on the card is printed correctly in all cases
     */
    @Test
    public void ObjectiveTest(){
        ObjectiveCard objCard= new ObjectiveCard(1,100,true, ObjectiveSequence.greenAntiDiagonal,new ArrayList<TokenType>());
        System.out.println(objCard.printCardInfo());
        ArrayList<TokenType> list= new ArrayList<TokenType>();
        list.add(TokenType.quill);
        list.add(TokenType.ink);
        list.add(TokenType.quill);
        ObjectiveCard a= new ObjectiveCard(1,101,false, ObjectiveSequence.redDiagonal,list);
        System.out.println(a.printCardInfo());
    }
}