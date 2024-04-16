package it.polimi.ingsw.audience;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to manage a list of GameAudience, so that multiple games can be managed
 */

public class AudiencesHandler {
    public List<GameAudience> audiences;

    /**
     * The constructor makes an ArrayList of GameAudience
     */
    public AudiencesHandler(){
        audiences = new ArrayList<>();
    }

}
