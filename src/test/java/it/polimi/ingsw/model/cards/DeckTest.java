package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.CantReplaceVisibleCardException;
import it.polimi.ingsw.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.NoVisibleCardException;
import it.polimi.ingsw.model.enums.CardType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {
    Deck res= new Deck(Creation.getResourceCards());
    Deck gold= new Deck(Creation.getGoldCards());
    public DeckTest() throws Exception {
    }
    @Before
    public void setUp() throws Exception {
        res.shuffle();
        res.setFirstVisible();
        res.setSecondVisible();
        gold.shuffle();
        gold.setFirstVisible();
        gold.setSecondVisible();
    }
    @Test
    public void getTopCardColour() {
        if(res.getNumRemaining()!=0){
            assertNotNull(res.getTopCardColour());
        }else{
            assertNull(res.getTopCardColour());
        }
        if(gold.getNumRemaining()!=0){
            assertNotNull(gold.getTopCardColour());
        }else{
            assertNull(gold.getTopCardColour());
        }
    }

    @Test
    public void getVisibles() {
        if(res.getNumRemaining()!=0){
            assertNotNull(res.getFirstVisible());
            assertNotNull(res.getSecondVisible());
        }
        if(gold.getNumRemaining()!=0){
            assertNotNull(gold.getFirstVisible());
            assertNotNull(gold.getSecondVisible());
        }
    }

    /**
     * Tests that a card is drawn correctly in every case<br>
     * Some of the edge cases are:<br>
     * If the deck is empty the card has to be drawn form the other deck<br>
     * If both decks are empty then the draw will have to be refused<br>
     * If a draw cannot be performed because there is no card in that position then the draw is refused<br>
     */
    @Test
    public void testDraws()  {
        assertThrows(IllegalArgumentException.class,()-> res.draw(-1));
        try {
            assertEquals(res.getSecondVisible().getID(),res.draw(2).getID());
            res.setVisibleAfterDraw(gold);
            assertEquals(gold.getFirstVisible().getID(),gold.draw(1).getID());
            gold.setVisibleAfterDraw(res);
            getVisibles();
            getTopCardColour();
        } catch (EmptyDeckException | NoVisibleCardException | CardAlreadyPresentException |
                 CantReplaceVisibleCardException e) {
            fail();
        }
        while(res.getNumRemaining()!=2){//sets up the deck so that it has only two cards remaining
            try {
                assertEquals(res.getSecondVisible().getID(),res.draw(2).getID());
                res.setVisibleAfterDraw(gold);
                getVisibles();
                getTopCardColour();
            } catch (EmptyDeckException | NoVisibleCardException|
                     CardAlreadyPresentException|CantReplaceVisibleCardException e) {
                fail();
            }
        }
        while(gold.getNumRemaining()!=0){//Empties the other deck so that the edge cases can be tested
            try {
                gold.draw(0);
                getVisibles();
                getTopCardColour();
            } catch (EmptyDeckException | NoVisibleCardException e) {
                fail();
            }
        }
        assertThrows(EmptyDeckException.class,()->gold.draw(0));
        getVisibles();
        getTopCardColour();
        //Tests that the cards drawn from the other deck actually are what gets put in the visible position after a draw
        CardType resTop= res.getTopCardColour();
        try{
            gold.draw(1);
            gold.setVisibleAfterDraw(res);
        }catch (EmptyDeckException | NoVisibleCardException | CardAlreadyPresentException |
                CantReplaceVisibleCardException e) {
            fail();
        }
        PlayableCard card = (PlayableCard)gold.getFirstVisible();
        assertEquals(resTop,card.getCardColour());
        resTop= res.getTopCardColour();
        try{
            gold.draw(2);
            gold.setVisibleAfterDraw(res);
        }catch (EmptyDeckException | NoVisibleCardException | CardAlreadyPresentException |
                CantReplaceVisibleCardException e) {
            fail();
        }
        card = (PlayableCard)gold.getSecondVisible();
        assertEquals(resTop,card.getCardColour());
        //Now both the decks are empty
        assertThrows(CantReplaceVisibleCardException.class,()-> {
            gold.draw(1);
            gold.setVisibleAfterDraw(res);
        });
        assertThrows(NoVisibleCardException.class,()->gold.draw(1));
        assertThrows(NoVisibleCardException.class,()-> {
            gold.draw(2);//There is still this visible card
            gold.draw(2);
        });
    }
}