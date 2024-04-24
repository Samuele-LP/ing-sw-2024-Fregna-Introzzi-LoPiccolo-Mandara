package it.polimi.ingsw;
import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *Class that is used to create Lists of cards from Json files.
 * There are four different types of card that need to be in four different Json files: <br>
 * ObjectiveCard in ObjectiveDeck.json<br>
 * StartingCard in StartingDeck.json<br>
 * ResourceCard in ResourceDeck.json<br>
 * GoldCard inGoldDeck.json<br><br>
 * Each file only contains one card type, duplicate cards are admissible, but they must all have different IDs.
 * ResourceCard IDs range from 1 to 40<br>GoldCard IDs from41 to 80<br>ResourceCard IDs from 81 to 86<br>ObjectiveCard IDs from 87 to 102
 */
public class Creation {
    private final static String goldDeckPath="src/main/java/it/polimi/ingsw/jsonData/GoldDeck.json";
    private final static String resourceDeckPath="src/main/java/it/polimi/ingsw/jsonData/ResourceDeck.json";
    private final static String startingDeckPath="src/main/java/it/polimi/ingsw/jsonData/StartingDeck.json";
    private final static String objectiveDeckPath="src/main/java/it/polimi/ingsw/jsonData/ObjectiveDeck.json";
    private final static Gson gsonParser=new Gson();

    /**
     *
     * @return a List of Card containing the GoldCards
     * @throws IOException if the reading from file didn't go well
     */
    public static List<Card> getGoldCards() throws IOException {
        FileReader reader = new FileReader(goldDeckPath);
        GoldCard[] DECK_GOLD = gsonParser.fromJson(reader, GoldCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_GOLD));
    }
    /**
     *
     * @return a List of Card containing the StartingCards
     * @throws IOException if the reading from file didn't go well
     */
    public static List<Card> getStartingCards() throws IOException {
        FileReader reader = new FileReader(startingDeckPath);
        StartingCard[] DECK_STARTING = gsonParser.fromJson(reader, StartingCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }
    /**
     *
     * @return a List of Card containing the ResourceCards
     * @throws IOException if the reading from file didn't go well
     */
    public static List<Card> getResourceCards() throws IOException {
        FileReader reader = new FileReader(resourceDeckPath);
        ResourceCard[] DECK_STARTING = gsonParser.fromJson(reader, ResourceCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }
    /**
     *
     * @return a List of Card containing the ObjectiveCards
     * @throws IOException if the reading from file didn't go well
     */
    public static List<Card> getObjectiveCards() throws IOException {
        FileReader reader = new FileReader(objectiveDeckPath);
        ObjectiveCard[] DECK_OBJECTIVE = gsonParser.fromJson(reader, ObjectiveCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_OBJECTIVE));
    }
}
