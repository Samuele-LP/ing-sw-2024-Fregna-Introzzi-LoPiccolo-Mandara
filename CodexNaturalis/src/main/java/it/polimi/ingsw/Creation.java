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
 *
 */
public class Creation {
    private final static String goldDeckPath="src/main/java/it/polimi/ingsw/jsonData/GoldDeck.json";
    private final static String resourceDeckPath="src/main/java/it/polimi/ingsw/jsonData/ResourceDeck.json";
    private final static String startingDeckPath="src/main/java/it/polimi/ingsw/jsonData/StartingDeck.json";
    private final static String objectiveDeckPath="src/main/java/it/polimi/ingsw/jsonData/ObjectiveDeck.json";
    private final static Gson gsonParser=new Gson();
    public static List<Card> getGoldCards() throws IOException {
        FileReader reader = new FileReader(goldDeckPath);
        GoldCard[] DECK_GOLD = gsonParser.fromJson(reader, GoldCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_GOLD));
    }
    public static List<Card> getStartingCards() throws IOException {
        FileReader reader = new FileReader(startingDeckPath);
        StartingCard[] DECK_STARTING = gsonParser.fromJson(reader, StartingCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }
    public static List<Card> getResourceCards() throws IOException {
        FileReader reader = new FileReader(resourceDeckPath);
        ResourceCard[] DECK_STARTING = gsonParser.fromJson(reader, ResourceCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }
    public static List<Card> getObjectiveCards() throws IOException {
        FileReader reader = new FileReader(objectiveDeckPath);
        ObjectiveCard[] DECK_OBJECTIVE = gsonParser.fromJson(reader, ObjectiveCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_OBJECTIVE));
    }
}
