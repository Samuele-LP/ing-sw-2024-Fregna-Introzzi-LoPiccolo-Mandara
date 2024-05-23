package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.CantReplaceVisibleCardException;
import it.polimi.ingsw.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.exceptions.NoVisibleCardException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

public class DeckTest {
    Deck testGold;
    Deck testResource;

    /**
     * Creates the decks to be tested.
     * @throws IOException if a reading error has occurred
     */
    @Before
    public void setUp() throws IOException {
        testGold = new Deck(Creation.getGoldCards());
        testResource = new Deck(Creation.getResourceCards());
        testResource.shuffle();
        setSecondVisible();
        setFirstVisible();
    }

    /**
     * Tests that if the deck is empty it returns -1 as the topCardId, if it's not empty it returns an ID>0
     */
    @Test
    public void getTopCardID() {
        int gTop= testGold.getTopCardID();
        int rTop= testResource.getTopCardID();
        assertTrue(gTop==-1&&testGold.getNumRemaining()==0||gTop>0&&testGold.getNumRemaining()>0);
        assertTrue(rTop==-1&&testResource.getNumRemaining()==0||rTop>0&&testResource.getNumRemaining()>0);
    }

    /**
     * Tests that until there are cards in the deck a visible card won't be null
     */
    @Test
    public void getFirstVisible() {
        if(testResource.getNumRemaining()!=0){
            assertNotNull(testResource.getFirstVisible());
        }
        if(testGold.getNumRemaining()!=0){
            assertNotNull(testGold.getFirstVisible());
        }
    }
    /**
     * Tests that until there are cards in the deck a visible card won't be null
     */
    @Test
    public void getSecondVisible() {
        if(testResource.getNumRemaining()!=0){
            assertNotNull(testResource.getSecondVisible());
        }
        if(testGold.getNumRemaining()!=0){
            assertNotNull(testGold.getSecondVisible());
        }
    }

    /**
     * Tests that the ID of a card drawn from the top is the same as the  ID that can be observed before the draw.
     *Also test that if the deck is empty an exception is thrown when trying to set a new visible card and both decks are empty
     */
    @Test
    public void draw() throws Exception {
        assertThrows(IllegalArgumentException.class,()->
                testResource.draw(912));
        int prevID;
        while(testGold.getNumRemaining()!=2){
            getTopCardID();
            getSecondVisible();
            getFirstVisible();
            prevID= testGold.getTopCardID();
            assertEquals(prevID, testGold.draw(0).getID());
            assertTrue(prevID!= testGold.draw(0).getID());
        }
        testGold.draw(1);
        testGold.setVisibleAfterDraw(testResource);
        getFirstVisible();
        testGold.draw(2);
        testGold.setVisibleAfterDraw(testResource);
        getSecondVisible();
        assertTrue(testGold.getFirstVisible() instanceof GoldCard);
        assertTrue(testGold.getSecondVisible() instanceof GoldCard);
        testGold.draw(1);
        testGold.setVisibleAfterDraw(testResource);
        getFirstVisible();
        testGold.draw(2);
        testGold.setVisibleAfterDraw(testResource);
        while(testResource.getNumRemaining()!=0){
            getTopCardID();
            getSecondVisible();
            getFirstVisible();
            testResource.draw(1);
            testResource.setVisibleAfterDraw(testGold);
            getTopCardID();
            getSecondVisible();
            getFirstVisible();
        }
        testGold.draw(1);
        assertThrows(CantReplaceVisibleCardException.class,()->{
            testGold.setVisibleAfterDraw(testResource);
        });
        testGold.draw(2);
        assertThrows(CantReplaceVisibleCardException.class,()->{
            testGold.setVisibleAfterDraw(testResource);
        });
        testResource.draw(2);
        assertThrows(NoVisibleCardException.class,()->
                testResource.draw(2));
        testResource.draw(1);
        assertThrows(NoVisibleCardException.class,()->
                testResource.draw(1));
        getTopCardID();
        getSecondVisible();
        getFirstVisible();
    }

    /**
     * Tests that exceptions are thrown when trying to set a new visible card after the deck has been emptied or when there is already a card present in that slot
     */
    @Test
    public void setFirstVisible()  {
        if(testResource.getNumRemaining()==0)
            assertThrows(CantReplaceVisibleCardException.class, ()->{
                testResource.setFirstVisible();
            });
        else if(testResource.getFirstVisible()!=null){
            assertThrows(CardAlreadyPresentException.class, ()->{
                testResource.setFirstVisible();
            });
        }
        else{
            int tID=testResource.getTopCardID();
            try {
                testResource.setFirstVisible();
            }catch (Exception e){
                fail();
            }
            assertTrue(testResource.getFirstVisible().getID()==tID);
        }
        if(testGold.getNumRemaining()==0)
            assertThrows(CantReplaceVisibleCardException.class, ()->{
                testGold.setFirstVisible();
            });
        else if(testGold.getFirstVisible()!=null){
            assertThrows(CardAlreadyPresentException.class, ()->{
                testGold.setFirstVisible();
            });
        }
        else{
            int tID=testGold.getTopCardID();
            try {
                testGold.setFirstVisible();
            }catch (Exception e){
                fail();
            }
            assertEquals(testGold.getFirstVisible().getID(), tID);
        }
    }
    /**
     * Tests that exceptions are thrown when trying to set a new visible card after the deck has been emptied or when there is already a card present in that slot
     */
    @Test
    public void setSecondVisible() {
        if (testResource.getNumRemaining() == 0) {
            assertNull(testResource.getTopCardColour());
            assertThrows(CantReplaceVisibleCardException.class, () -> {
                testResource.setSecondVisible();
            });
        }
        else if (testResource.getFirstVisible() != null) {
            assertThrows(CardAlreadyPresentException.class, () -> {
                testResource.setSecondVisible();
            });
        } else {
            int tID = testResource.getTopCardID();
            if(tID<11){
                assertEquals(CardType.fungi,testResource.getTopCardColour());
            }else if(tID<21){
                assertEquals(CardType.plant,testResource.getTopCardColour());
            }else if(tID<31) {
                assertEquals(CardType.animal,testResource.getTopCardColour());
            }else {
                assertEquals(CardType.insect,testResource.getTopCardColour());
            }
            try {
                testResource.setSecondVisible();
            }catch (Exception e){
                fail();
            }
            assertEquals(testResource.getSecondVisible().getID(), tID);
        }
        if (testResource.getNumRemaining() == 0)
            assertThrows(CantReplaceVisibleCardException.class, () -> {
                testGold.setSecondVisible();
            });
        else if (testResource.getFirstVisible() != null) {
            assertThrows(CardAlreadyPresentException.class, () -> {
                testGold.setSecondVisible();
            });
        } else {
            int tID = testGold.getTopCardID();
            try {
                testGold.setSecondVisible();
            }catch (Exception e){
                fail();
            }
            assertEquals(testGold.getSecondVisible().getID(), tID);
        }
    }
}