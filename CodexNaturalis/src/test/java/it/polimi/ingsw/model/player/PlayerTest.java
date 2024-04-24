package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Player blockedTest;
    ScoreTrack stub = new ScoreTrack("","");
    List<Card> resource= Creation.getResourceCards();
    List<Card> gold=Creation.getGoldCards();
    List<Card> starter=Creation.getStartingCards();
    List<Card> objectives=Creation.getObjectiveCards();

    public PlayerTest() throws IOException {
    }

    /**
     * A situation is set up such that a player has no possible placing moves left.
     * The test  fails if an exception is thrown during the setUp or if there are any Points in the list of all available positions, the test is passed when
     * a PlayerCantPlaceAnymoreException is thrown
     */
    @Before
    public void setUp() {
       try {
           //This test refers to a field condition that blocks every possible move
           PlayableCard[] startingHand = new PlayableCard[3];
           startingHand[0] = (PlayableCard) resource.get(16);
           startingHand[1] = (PlayableCard) resource.get(0);
           startingHand[2] = (PlayableCard) gold.get(0);
           blockedTest = new Player("blocked", (PlayableCard) starter.get(4), startingHand);
           blockedTest.placeStartingCard(true);
           blockedTest.placeCard(1, -1, 1, true, stub);
           blockedTest.receiveDrawnCard((PlayableCard) resource.get(16));
           ;
           blockedTest.placeCard(17, 1, 1, true, stub);
           blockedTest.receiveDrawnCard((PlayableCard) resource.get(7));
           blockedTest.placeCard(8, 2, 0, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(22));
           blockedTest.placeCard(23, 3, 1, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(33));
           blockedTest.placeCard(34, -2, 0, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(35));
           blockedTest.placeCard(36, -2, 2, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(12));
           blockedTest.placeCard(13, 0, 2, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) gold.get(16));
           blockedTest.placeCard(57, 4, 0, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(2));
           blockedTest.placeCard(3, -1, 3, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) resource.get(38));
           blockedTest.placeCard(39, -2, 4, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) gold.get(18));
           blockedTest.placeCard(59, -3, 5, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) gold.get(26));
           blockedTest.placeCard(67, -1, 5, true, stub);

           blockedTest.receiveDrawnCard((PlayableCard) gold.get(38));
           blockedTest.placeCard(79, -2, 6, true, stub);
           assertThrows(PlayerCantPlaceAnymoreException.class, () -> {
               blockedTest.getAvailablePositions();
           });
       }catch (Exception e){
           fail();
       }
    }

    /**
     * Failed if there are any available positions to place a card
     */
    @Test
    public void getAvailablePositions() {
            assertThrows(PlayerCantPlaceAnymoreException.class,()->{
                blockedTest.getAvailablePositions();
            });
    }

    /**
     * As this method is called after SetUp it must throw an AlreadyPlacedException to pass, the starting card should have already been placed
     */
    @Test
    public void placeStartingCard() {
        assertThrows(AlreadyPlacedException.class,()->{
            blockedTest.placeStartingCard(false);
        });
    }

    /**
     * Tests that the objectives are counted correctly for this specific composition
     */
    @Test
    public void calculateSecretObjective() {
            assertEquals(14, blockedTest.getPoints());
        for(Card o: objectives){
                blockedTest.calculateCommonObjectives((ObjectiveCard) o,null);
        }
            assertTrue(26==blockedTest.getPoints()&&blockedTest.getNumberOfScoredObjectives()==6);
    }

    /**
     * The player must always have two or three cards in the hand
     */
    @Test
    public void quantityOfCards(){
        assertTrue(blockedTest.quantityOfCards()<=3&&blockedTest.quantityOfCards()>=2);
    }
}