package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StartingCard;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test class aims mainly to verify that the objective calculation algorithm is correct.
 * The methods related to the correctness of the field are tested by {@link PlayerTest}
 */
public class PlayingFieldTest {
    PlayingField playingField = new PlayingField();
    List<Card> gold = Creation.getInstance().getGoldCards();
    List<Card> resource = Creation.getInstance().getResourceCards();
    List<Card> objectives = Creation.getInstance().getObjectiveCards();
    /**
     * Used to help understand what id the colour of  the card that is being placed
     */
    PlayableCard red;
    /**
     * Used to help understand what id the colour of  the card that is being placed
     */
    PlayableCard green;
    /**
     * Used to help understand what id the colour of  the card that is being placed
     */
    PlayableCard blue;
    /**
     * Used to help understand what id the colour of  the card that is being placed
     */
    PlayableCard purple;

    public PlayingFieldTest() throws IOException {

    }


    /**
     * Tests that a diagonal long 6 cards will be counted for 4 points.
     */
    @Test
    public void testDoubleDiagonal() throws Exception {
        StartingCard st = (StartingCard) Creation.getInstance().getStartingCards().getFirst();
        st.placeCard(new Point(0, 0), 0, false);
        red = (PlayableCard) resource.get(0);
        red.placeCard(new Point(1, 1), 1, false);
        playingField.addPlacedCard(red);
        red = (PlayableCard) resource.get(1);
        red.placeCard(new Point(2, 2), 1, false);
        playingField.addPlacedCard(red);
        red = (PlayableCard) resource.get(2);
        red.placeCard(new Point(3, 3), 1, false);
        playingField.addPlacedCard(red);
        red = (PlayableCard) resource.get(3);
        red.placeCard(new Point(4, 4), 1, false);
        playingField.addPlacedCard(red);
        red = (PlayableCard) resource.get(4);
        red.placeCard(new Point(5, 5), 1, false);
        playingField.addPlacedCard(red);
        red = (PlayableCard) resource.get(5);
        red.placeCard(new Point(6, 6), 1, false);
        playingField.addPlacedCard(red);
        assertEquals(4, playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(0)));
    }

    /**
     * The setup of the field in this test is as follows:<br>
     * A diagonal that is longer than 3 cards and has other cards of the same colour in that general diagonal, but they are separated by other colour cards;
     * the diagonal objective must be scored only once for this setup<br><br>
     */
    @Test
    public void testSingleDiagonal() {
        try {
            StartingCard st = (StartingCard) Creation.getInstance().getStartingCards().getFirst();
            st.placeCard(new Point(0, 0), 0, false);
            red = (PlayableCard) resource.get(0);
            /*This part of the setup is to verify that a diagonal of 4 will be counted as 2 points,
             and verifies that if a diagonal is interrupted by other colour cards it won't be counted*/

            red.placeCard(new Point(1, 1), 1, false);
            playingField.addPlacedCard(red);

            red = (PlayableCard) resource.get(1);
            red.placeCard(new Point(2, 2), 1, false);
            playingField.addPlacedCard(red);

            red = (PlayableCard) resource.get(2);
            red.placeCard(new Point(3, 3), 1, false);
            playingField.addPlacedCard(red);
            red = (PlayableCard) resource.get(3);
            red.placeCard(new Point(4, 4), 1, false);
            playingField.addPlacedCard(red);

            blue = (PlayableCard) resource.get(22);
            blue.placeCard(new Point(5, 5), 1, false);
            playingField.addPlacedCard(blue);

            blue = (PlayableCard) resource.get(21);
            blue.placeCard(new Point(6, 6), 1, false);
            playingField.addPlacedCard(blue);

            red = (PlayableCard) resource.get(4);
            red.placeCard(new Point(7, 7), 1, false);
            playingField.addPlacedCard(red);
            red = (PlayableCard) resource.get(5);
            red.placeCard(new Point(8, 8), 1, false);
            playingField.addPlacedCard(red);

            assertEquals(2, playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(0)));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * The setup of the field in this test is as follows:<br>
     * A column of 3 cards of the same colour that have 3 side pieces in the correct position one on top of the other (staring from the top);
     * it creates an ambiguous situation where an objective is counted once but can be counted in two ways<br><br>
     */
    @Test
    public void ambiguousL_Shape() {
        try {
            /*Sets up an ambiguous situation in which an L-shape objective can be counted only once but in two ways
             * In this case it was used the objective that requires two red cards and a green card on the bottom left
             * */

            green = (PlayableCard) resource.get(10);
            green.placeCard(new Point(2, 4), 1, false);
            playingField.addPlacedCard(green);

            red = (PlayableCard) resource.get(7);
            red.placeCard(new Point(1, 5), 1, false);
            playingField.addPlacedCard(red);

            green = (PlayableCard) resource.get(11);
            green.placeCard(new Point(2, 6), 1, false);
            playingField.addPlacedCard(green);

            red = (PlayableCard) resource.get(8);
            red.placeCard(new Point(1, 7), 1, false);
            playingField.addPlacedCard(red);

            green = (PlayableCard) resource.get(12);
            green.placeCard(new Point(2, 8), 1, false);
            playingField.addPlacedCard(green);

            red = (PlayableCard) resource.get(9);
            red.placeCard(new Point(1, 9), 1, false);
            playingField.addPlacedCard(red);

            assertEquals(3, playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(4)));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * This test sets up a diagonal in an incorrect direction
     */
    @Test
    public void incorrectDirectionDiagonal() {
        try {

            purple = (PlayableCard) resource.get(30);
            purple.placeCard(new Point(-1, -1), 1, false);
            playingField.addPlacedCard(purple);
            purple = (PlayableCard) resource.get(31);
            purple.placeCard(new Point(-2, -2), 1, false);
            playingField.addPlacedCard(purple);
            purple = (PlayableCard) resource.get(32);
            purple.placeCard(new Point(-3, -3), 1, false);
            playingField.addPlacedCard(purple);
            assertEquals(0, playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(3)));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests that two L shapes of the same kind will be counted correctly, in the same column
     */
    @Test
    public void twoL_shapes() {
        try {
            ObjectiveCard twoPurpleOneBlue = (ObjectiveCard) objectives.get(7);
            purple = (PlayableCard) resource.get(31);
            assertThrows(NotPlacedException.class, () ->
                    purple.isFacingUp()
            );
            purple.placeCard(new Point(1, 1), 1, false);
            assertThrows(AlreadyPlacedException.class, () ->
                    purple.placeCard(new Point(1, 1), 1, false)
            );
            playingField.addPlacedCard(purple);

            purple = (PlayableCard) resource.get(32);
            purple.placeCard(new Point(1, 3), 1, false);
            playingField.addPlacedCard(purple);

            blue = (PlayableCard) resource.get(21);
            blue.placeCard(new Point(0, 4), 1, false);
            playingField.addPlacedCard(blue);

            purple = (PlayableCard) resource.get(33);
            purple.placeCard(new Point(1, 5), 1, false);
            playingField.addPlacedCard(purple);

            purple = (PlayableCard) resource.get(34);
            purple.placeCard(new Point(1, 7), 1, false);
            playingField.addPlacedCard(purple);

            blue = (PlayableCard) resource.get(22);
            blue.placeCard(new Point(0, 8), 1, false);
            playingField.addPlacedCard(blue);
            System.out.println(playingField.getVisibleSymbols().toString());//displays that the cards' colours are counted correctly
            assertEquals(6, playingField.calculateObjectivePoints(twoPurpleOneBlue));
        } catch (Exception e) {
            fail();
        }
    }
}