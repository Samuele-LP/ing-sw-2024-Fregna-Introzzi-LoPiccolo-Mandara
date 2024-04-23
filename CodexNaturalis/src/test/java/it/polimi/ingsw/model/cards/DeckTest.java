package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.CantReplaceVisibleCardException;
import it.polimi.ingsw.exceptions.CardAlreadyPresentException;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

public class DeckTest {
    Deck testGold;
    Deck testResource;
    @Before
    public void setUp() throws IOException, CantReplaceVisibleCardException, CardAlreadyPresentException {
        testGold = new Deck(Creation.getGoldCards());
        testResource = new Deck(Creation.getResourceCards());
        testResource.shuffle();
        setSecondVisible();
        setFirstVisible();
    }
    @Test
    public void getTopCardID() {
        int gTop= testGold.getTopCardID();
        int rTop= testResource.getTopCardID();
        assertTrue(gTop==-1&&testGold.getNumRemaining()==0||gTop>0&&testGold.getNumRemaining()>0);
        assertTrue(rTop==-1&&testResource.getNumRemaining()==0||rTop>0&&testResource.getNumRemaining()>0);
    }

    @Test
    public void getFirstVisible() {
        if(testResource.getNumRemaining()!=0){
            assertNotNull(testResource.getFirstVisible());
        }
        if(testGold.getNumRemaining()!=0){
            assertNotNull(testGold.getFirstVisible());
        }
    }

    @Test
    public void getSecondVisible() {
        if(testResource.getNumRemaining()!=0){
            assertNotNull(testResource.getSecondVisible());
        }
        if(testGold.getNumRemaining()!=0){
            assertNotNull(testGold.getSecondVisible());
        }
    }

    @Test
    public void draw() throws Exception {
        int prevID;
        while(testGold.getNumRemaining()>2){
            prevID= testGold.getTopCardID();
            assertTrue(prevID== testGold.draw(0).getID());
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
        while(testResource.getNumRemaining()!=0){
            testResource.draw(1);
            testResource.setVisibleAfterDraw(testGold);
        }
        testGold.draw(1);
        assertThrows(CantReplaceVisibleCardException.class,()->{
            testGold.setVisibleAfterDraw(testResource);
        });
    }
    @Test
    public void setFirstVisible() throws CardAlreadyPresentException, CantReplaceVisibleCardException {
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
            testResource.setFirstVisible();
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
            testGold.setFirstVisible();
            assertTrue(testGold.getFirstVisible().getID()==tID);
        }
    }
    @Test
    public void setSecondVisible() throws CardAlreadyPresentException, CantReplaceVisibleCardException {
        if (testResource.getNumRemaining() == 0)
            assertThrows(CantReplaceVisibleCardException.class, () -> {
                testResource.setSecondVisible();
            });
        else if (testResource.getFirstVisible() != null) {
            assertThrows(CardAlreadyPresentException.class, () -> {
                testResource.setSecondVisible();
            });
        } else {
            int tID = testResource.getTopCardID();
            testResource.setSecondVisible();
            assertTrue(testResource.getSecondVisible().getID() == tID);
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
            testGold.setSecondVisible();
            assertTrue(testGold.getSecondVisible().getID() == tID);
        }
    }
}