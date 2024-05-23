package it.polimi.ingsw.model;

import it.polimi.ingsw.ConstantValues;
import org.junit.Test;

public class ScoreTrackTest {
    @Test
    public void scoreTrackTest(){
        ScoreTrack st= new ScoreTrack("test1","test2");
        st.updateScoreTrack("test1",25);
        st.updateScoreTrack("test2",2);
        st.setPawnColor("test1", ConstantValues.ansiGreen);
        st.setPawnColor("test2", ConstantValues.ansiRed);
        st.copyScoreTrack().printTable();
    }
}