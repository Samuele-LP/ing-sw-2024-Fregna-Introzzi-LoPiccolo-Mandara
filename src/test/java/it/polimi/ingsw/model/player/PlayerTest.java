package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests the methods to correctly place cards
 */
public class PlayerTest {
    Player player;
    private final ScoreTrack stub = new ScoreTrack("", "");
    private final List<Card> resource = Creation.getInstance().getResourceCards();
    private final List<Card> gold = Creation.getInstance().getGoldCards();
    private final  List<Card> starter = Creation.getInstance().getStartingCards();
    private final List<Card> objectives = Creation.getInstance().getObjectiveCards();

    public PlayerTest() throws IOException {
    }

    /**
     * A situation is set up such that a player has no possible placing moves left.
     * It's also tested that the player's hand is updated successfully after a placement
     * The test  fails if an exception is thrown during the setUp or if there are any Points in the list of all available positions, the test is passed when
     * a PlayerCantPlaceAnymoreException is thrown.
     * The test then calls other methods top test whether exceptions get thrown successfully
     */
    @Test
    public void softLockTest() {
        try {
            //This test refers to a field condition that blocks every possible move
            PlayableCard[] startingHand = new PlayableCard[3];
            startingHand[0] = (PlayableCard) resource.get(16);
            startingHand[1] = (PlayableCard) resource.get(0);
            startingHand[2] = (PlayableCard) gold.get(0);
            //Asserts that a card that was not placed does throw the exception correctly
            assertThrows(NotPlacedException.class, () -> startingHand[0].getPosition());
            assertThrows(IllegalStartingCardException.class, () ->
                    new Player("illegalCard", (PlayableCard) gold.get(4), startingHand)
            );

            player = new Player("blocked", (PlayableCard) starter.get(4), startingHand);
            player.placeStartingCard(true);
            assertEquals(player.getStartingCard(), starter.get(4));
            assertEquals("blocked", player.getName());
            assertTrue(player.viewCurrentHand().contains((PlayableCard) resource.get(16)) &&
                    player.viewCurrentHand().contains((PlayableCard) resource.get(0)) &&
                    player.viewCurrentHand().contains((PlayableCard) gold.get(0)));

            player.placeCard(1, -1, 1, true, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(16));

            assertTrue(!(player.viewCurrentHand().contains((PlayableCard) resource.get(0))) &&
                    player.viewCurrentHand().contains((PlayableCard) resource.get(16)) &&
                    player.viewCurrentHand().contains((PlayableCard) gold.get(0)));

            player.placeCard(17, 1, 1, true, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(7));
            player.placeCard(8, 2, 0, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(22));
            player.placeCard(23, 3, 1, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(33));
            player.placeCard(34, -2, 0, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(35));
            player.placeCard(36, -2, 2, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(12));
            player.placeCard(13, 0, 2, true, stub);

            player.receiveDrawnCard((PlayableCard) gold.get(16));
            player.placeCard(57, 4, 0, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(2));
            player.placeCard(3, -1, 3, true, stub);

            player.receiveDrawnCard((PlayableCard) resource.get(38));
            player.placeCard(39, -2, 4, true, stub);

            player.receiveDrawnCard((PlayableCard) gold.get(18));
            player.placeCard(59, -3, 5, true, stub);

            player.receiveDrawnCard((PlayableCard) gold.get(26));
            player.placeCard(67, -1, 5, true, stub);

            player.receiveDrawnCard((PlayableCard) gold.get(38));
            player.placeCard(79, -2, 6, true, stub);
            assertThrows(PlayerCantPlaceAnymoreException.class, () -> {
                player.getAvailablePositions();
            });
        } catch (Exception e) {
            fail();
        }
        System.out.println(player.viewVisibleSymbols().toString() + "\n");
        placeStartingCard();
        calculateObjectives();
    }

    /**
     * As this method is called after softLockTest it must throw an AlreadyPlacedException to pass, the starting card should have already been placed
     */
    private void placeStartingCard() {
        assertThrows(AlreadyPlacedException.class, () -> {
            player.placeStartingCard(false);
        });
    }

    /**
     * Tests that the objectives are counted correctly for the specific composition of the softLock test
     */
    private void calculateObjectives() {
        assertEquals(14, player.getPoints());
        for (Card o : objectives) {
            player.calculateCommonObjectives((ObjectiveCard) o, null);
        }
        assertTrue(26 == player.getPoints() && player.getNumberOfScoredObjectives() == 6);
    }

    /**
     * Tests whether the placement of cards fails correctly.
     * If unintended exceptions are thrown then the test is failed immediately.<br>
     * It tests that a starting card cannot be place multiple times<br>
     * It tests that if the player tries to place a card that is not in the player's hand the placing fails.<br>
     * If a gold card is being placed without enough resources the correct exception is thrown.
     */
    @Test
    public void placingFailingTest() {
        try {
            PlayableCard[] startingHand = new PlayableCard[3];
            startingHand[0] = (PlayableCard) resource.get(16);
            startingHand[1] = (PlayableCard) resource.get(0);
            startingHand[2] = (PlayableCard) gold.get(39);//for gold position 0 is id 40
            player = new Player("placing fail", (PlayableCard) starter.get(4), startingHand);
            player.placeStartingCard(true);
            assertTrue(player.viewCurrentHand().contains((PlayableCard) resource.get(16)) &&
                    player.viewCurrentHand().contains((PlayableCard) resource.get(0)) &&
                    player.viewCurrentHand().contains((PlayableCard) gold.get(39)));
            assertThrows(AlreadyPlacedException.class, () -> {
                player.placeStartingCard(true);
            });
            assertThrows(CardNotInHandException.class, () -> {
                player.placeCard(67, 1, 1, true, stub);
            });
            assertThrows(NotEnoughResourcesException.class, () -> {
                player.placeCard(80, 1, 1, true, stub);
            });
            assertThrows(InvalidPositionException.class, () -> {
                player.placeCard(1, 2, 1, true, stub);
            });
            assertThrows(InvalidPositionException.class, () -> {
                player.placeCard(1, -1, -1, true, stub);
            });
            player.setSecretObjective((ObjectiveCard) objectives.get(0));
            assertThrows(ObjectiveAlreadySetException.class, () -> {
                player.setSecretObjective((ObjectiveCard) objectives.get(0));
            });
            player.calculateSecretObjective();
            assertTrue(player.getNumberOfScoredObjectives() == 0 && player.getPoints() == 0);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests that gold card points and placement conditions are calculated correctly
     */
    @Test
    public void goldCardPlacement() {
        try {
            PlayableCard[] startingHand = new PlayableCard[3];
            startingHand[0] = (PlayableCard) resource.get(0);
            startingHand[1] = (PlayableCard) resource.get(1);
            startingHand[2] = (PlayableCard) gold.get(0);
            player = new Player("gold", (PlayableCard) starter.get(0), startingHand);
            player.placeStartingCard(true);

            assertThrows(NotEnoughResourcesException.class, () ->
                    player.placeCard(41, 1, 1, true, stub)
            );

            player.placeCard(1, 1, 1, false, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(19));

            player.placeCard(2, -1, 1, false, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(2));

            player.placeCard(20, 2, 2, false, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(3));

            player.placeCard(3, 3, 3, false, stub);
            player.receiveDrawnCard((PlayableCard) resource.get(29));

            player.placeCard(30, 4, 4, false, stub);
            player.receiveDrawnCard((PlayableCard) gold.get(5));//It's the gold card that awards 2 points for every corner covered
            player.placeCard(41, 1, -1, true, stub);
            assertEquals(1, player.getPoints());/*The card with id 41 awards 1 point for every quill on the field
            the only quill on the field was placed by the gold card itself*/
            System.out.println(player.viewVisibleSymbols().toString() + "\n");
            player.placeCard(46, 0, 2, true, stub);//Places the card covering 2 corners -->4 points added
            assertEquals(5, player.getPoints());
        } catch (IllegalStartingCardException | InvalidPositionException | CardNotInHandException |
                 NotEnoughResourcesException | AlreadyPlacedException | HandAlreadyFullException |
                 NotPlacedException e) {
            fail();
        }
    }
}