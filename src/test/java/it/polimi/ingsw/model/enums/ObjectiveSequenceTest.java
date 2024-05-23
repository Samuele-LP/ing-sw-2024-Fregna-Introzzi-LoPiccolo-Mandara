package it.polimi.ingsw.model.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectiveSequenceTest {

    @Test
    public void testToString() {
        for(ObjectiveSequence t: ObjectiveSequence.values()){
            System.out.println(t.toString());
        }
    }
}