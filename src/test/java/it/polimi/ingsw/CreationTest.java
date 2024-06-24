package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.CardType;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Test class that checks that the json files all contain the correct object types with the correct ID's and card colours<br>
 */
public class CreationTest {
    @Test
    public void getGoldCards() throws IOException {
        List<Card> gold = Creation.getInstance().getGoldCards();
        List<Integer> IDList = new ArrayList<>();
        for (Card c : gold) {
            if (!(c instanceof GoldCard)) {
                fail();
            }
            GoldCard gc = (GoldCard) c;
            if (IDList.contains(c.getID())) {
                System.out.println(c.getID());
                fail();
            }
            if (c.getID() > 80 || c.getID() < 41) {
                fail();
            }
            if (gc.getID() > 40 && gc.getID() < 51 && !gc.getCardColour().equals(CardType.fungi)) {
                fail();
            }
            if (gc.getID() > 50 && gc.getID() < 61 && !gc.getCardColour().equals(CardType.plant)) {
                fail();
            }
            if (gc.getID() > 60 && gc.getID() < 71 && !gc.getCardColour().equals(CardType.animal)) {
                fail();
            }
            if (gc.getID() > 70 && gc.getID() < 81 && !gc.getCardColour().equals(CardType.insect)) {
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    public void getStartingCards() throws IOException {
        List<Card> starter = Creation.getInstance().getStartingCards();
        List<Integer> IDList = new ArrayList<>();
        for (Card c : starter) {
            if (!(c instanceof StartingCard)) {
                fail();
            }
            if (IDList.contains(c.getID())) {
                System.out.println(c.getID());
                fail();
            }
            if (c.getID() < 81 || c.getID() > 86) {
                fail();
            }
            if (!CardType.starter.equals(((StartingCard) c).getCardColour())) {
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    public void getResourceCards() throws IOException {
        List<Card> resourceCards = Creation.getInstance().getResourceCards();
        List<Integer> IDList = new ArrayList<>();
        for (Card c : resourceCards) {
            if (!(c instanceof ResourceCard)) {
                fail();
            }
            ResourceCard rc = (ResourceCard) c;
            if (IDList.contains(c.getID())) {
                System.out.println(c.getID());
                fail();
            }
            if (c.getID() < 0 || c.getID() > 40) {
                fail();
            }
            if (rc.getID() > 0 && rc.getID() < 11 && !rc.getCardColour().equals(CardType.fungi)) {
                fail();
            }
            if (rc.getID() > 10 && rc.getID() < 21 && !rc.getCardColour().equals(CardType.plant)) {
                fail();
            }
            if (rc.getID() > 20 && rc.getID() < 31 && !rc.getCardColour().equals(CardType.animal)) {
                fail();
            }
            if (rc.getID() > 30 && rc.getID() < 41 && !rc.getCardColour().equals(CardType.insect)) {
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    public void getObjectiveCards() throws IOException {
        List<Card> gold = Creation.getInstance().getObjectiveCards();
        List<Integer> IDList = new ArrayList<>();
        for (Card c : gold) {
            if (!(c instanceof ObjectiveCard)) {
                fail();
            }
            if (IDList.contains(c.getID())) {
                System.out.println(c.getID());
                fail();
            }
            if (c.getID() < 87 || c.getID() > 102) {
                fail();
            }
            IDList.add(c.getID());
        }
    }
}