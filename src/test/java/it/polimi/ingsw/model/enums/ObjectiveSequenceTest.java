package it.polimi.ingsw.model.enums;

import org.junit.Test;

/**
 * Test class to control the String representation of {@link ObjectiveSequence}
 */
public class ObjectiveSequenceTest {

    @Test
    public void testToString() {
        for (ObjectiveSequence t : ObjectiveSequence.values()) {
            for (String s : t.toString().split("X")) {
                System.out.println(s);
            }
            System.out.println("------------");
        }
    }
}