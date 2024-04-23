package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class PlayingFieldTest {
    PlayingField playingField;
    List<Card> gold = Creation.getGoldCards();
    List<Card> resource = Creation.getResourceCards();
    List<Card> objectives= Creation.getObjectiveCards();

    PlayableCard red ;
    PlayableCard green ;
    PlayableCard blue;
    PlayableCard purple;
    public PlayingFieldTest() throws IOException {

    }

    /**
     * This method sets up a situation where all positional objectives can be scored.
     * This test serves the purpose of trying to find errors in the algorithm to calculate objectives.
     */
    @Test
    public void positionalObjectivesTest() throws IOException, AlreadyPlacedException, NotPlacedException {
        playingField = new PlayingField();
        StartingCard st= (StartingCard) Creation.getStartingCards().getFirst();
        st.placeCard(new Point(0,0),0,false);
        red= (PlayableCard) resource.get(0);
        //This part of the setup is to verify that a diagonal of 4 will be counted as 2 points, and verifies that if a diagonal is interrupted by other colour cards it won't be counted

        red.placeCard(new Point(1,1),1,false);
        playingField.addPlacedCard(red);

        red= (PlayableCard) resource.get(1);
        red.placeCard(new Point(2,2),1,false);
        playingField.addPlacedCard(red);

        red= (PlayableCard) resource.get(2);
        red.placeCard(new Point(3,3),1,false);
        playingField.addPlacedCard(red);
        red= (PlayableCard) resource.get(3);
        red.placeCard(new Point(4,4),1,false);
        playingField.addPlacedCard(red);

        blue= (PlayableCard) resource.get(22);
        blue.placeCard(new Point(5,5),1,false);
        playingField.addPlacedCard(blue);

        blue= (PlayableCard) resource.get(21);
        blue.placeCard(new Point(6,6),1,false);
        playingField.addPlacedCard(blue);

        red= (PlayableCard) resource.get(4);
        red.placeCard(new Point(7,7),1,false);
        playingField.addPlacedCard(red);
        red= (PlayableCard) resource.get(5);
        red.placeCard(new Point(8,8),1,false);
        playingField.addPlacedCard(red);

        assertTrue(playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(0))==2);
        //Sets up an ambiguous situation in which an L-shape objective can be counted only once but in two ways

        red= (PlayableCard) resource.get(6);
        red.placeCard(new Point(1,3),1,false);
        playingField.addPlacedCard(red);

        green= (PlayableCard) resource.get(10);
        green.placeCard(new Point(2,4),1,false);
        playingField.addPlacedCard(green);

        red= (PlayableCard) resource.get(7);
        red.placeCard(new Point(1,5),1,false);
        playingField.addPlacedCard(red);

        green= (PlayableCard) resource.get(11);
        green.placeCard(new Point(2,6),1,false);
        playingField.addPlacedCard(green);

        red= (PlayableCard) resource.get(8);
        red.placeCard(new Point(1,7),1,false);
        playingField.addPlacedCard(red);

        green= (PlayableCard) resource.get(12);
        green.placeCard(new Point(2,8),1,false);
        playingField.addPlacedCard(green);

        red= (PlayableCard) resource.get(9);
        red.placeCard(new Point(1,9),1,false);
        playingField.addPlacedCard(red);

        assertTrue(playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(4))==3);

        //Sets up a diagonal in a direction that is not correct

        purple=(PlayableCard) resource.get(30);
        purple.placeCard(new Point(-1,-1),1,false);
        playingField.addPlacedCard(purple);
        purple=(PlayableCard) resource.get(31);
        purple.placeCard(new Point(-2,-2),1,false);
        playingField.addPlacedCard(purple);
        purple=(PlayableCard) resource.get(32);
        purple.placeCard(new Point(-3,-3),1,false);
        playingField.addPlacedCard(purple);
        assertTrue(playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(3))==0);
        //sets up two L shape of the same kind

        purple=(PlayableCard) resource.get(33);
        purple.placeCard(new Point(-2,0),1,false);
        playingField.addPlacedCard(purple);
        purple=(PlayableCard) resource.get(34);
        purple.placeCard(new Point(-3,-1),1,false);
        playingField.addPlacedCard(purple);

        blue=(PlayableCard) gold.get(20);
        blue.placeCard(new Point(-3,1),1,false);
        playingField.addPlacedCard(blue);
        blue=(PlayableCard) gold.get(21);
        blue.placeCard(new Point(-4,0),1,false);
        playingField.addPlacedCard(blue);
        assertEquals(6, playingField.calculateObjectivePoints((ObjectiveCard) objectives.get(7)));
    }
    @Test
    public void calculateGoldPoints() throws NotPlacedException, AlreadyPlacedException {
    }
}