package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ScoreTrack;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class that serves the purpose of seeing if the CLI output is correctly formatted.<br>
 * This is the format:<br>
 * Player: player's name Points: player's points<br>
 * Its printed 2, 3 or 4 times, according to the number of players. And they are ordered first by number of points and then according to
 * String.CASE_INSENSITIVE_ORDER.compare(s1,s2)
 */
public class ImmutableScoreTrackTest {
    ScoreTrack temp = new ScoreTrack("test1", "test2", "test3", "test4");
    ImmutableScoreTrack test;

    @Before
    public void setUp() {
        temp.updateScoreTrack("test1", 2);
        temp.updateScoreTrack("tedfvbggfdsbfdsbfbdfsbsdbst2", 4);
        temp.updateScoreTrack("s", 2);
        temp.updateScoreTrack("test4", 1);
        test = temp.copyScoreTrack();
    }

    @Test
    public void printTable() {
        for (String s : test.printTable()) {
            System.out.println(s);
        }
    }
}