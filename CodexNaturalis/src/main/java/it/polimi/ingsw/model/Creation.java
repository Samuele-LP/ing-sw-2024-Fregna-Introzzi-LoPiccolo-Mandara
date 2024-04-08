package it.polimi.ingsw.model;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Creation {
    public static void main(String[] args) throws FileNotFoundException {
        Gson obj_gson1 = new Gson();

        String string1 = new String("src/main/java/it/polimi/ingsw/model/Deck/GoldDeck.json");
        String string2 = new String("src/main/java/it/polimi/ingsw/model/Deck/ResourceDeck.json");
        String string3 = new String("src/main/java/it/polimi/ingsw/model/Deck/StartingDeck.json");
        String string4 = new String("src/main/java/it/polimi/ingsw/model/Deck/ObjectiveDeck.json");

        // ------------------ ( Gold ) -------------------
        FileReader reader1 = new FileReader(string1);
        GoldCard[] DECK_GOLD = obj_gson1.fromJson(reader1, GoldCard[].class);

        List<GoldCard> Golden_deck1 = new ArrayList<>();
        for (GoldCard goldCard : DECK_GOLD) {
            Golden_deck1.add(goldCard);
        }

        // ------------------ ( Resource ) --------------------------------------
        Gson obj_gson2 = new Gson();
        FileReader reader2 = new FileReader(string2);
        ResourceCard[] DECK_RESOURCE = obj_gson2.fromJson(reader2, ResourceCard[].class);

        List<ResourceCard> Resource_deck1 = new ArrayList<>();
        for (ResourceCard resourceCard : DECK_RESOURCE) {
            Resource_deck1.add(resourceCard);
        }

        // ------------------ ( Objective ) --------------------------------------
        Gson obj_gson3 = new Gson();
        FileReader reader3 = new FileReader(string4);
        ObjectiveCard[] DECK_OBJECTIVE = obj_gson3.fromJson(reader3, ObjectiveCard[].class);

        List<ObjectiveCard> Objective_deck1 = new ArrayList<>();
        for (ObjectiveCard objectiveCard : DECK_OBJECTIVE) {
            Objective_deck1.add(objectiveCard);
        }

        //-------------------- ( Starting ) ----------------------------------------
        Gson obj_gson4 = new Gson();
        FileReader reader4 = new FileReader(string3);
        StartingCard[] DECK_STARTING = obj_gson4.fromJson(reader4, StartingCard[].class);

        List<ObjectiveCard> Starting_deck1 = new ArrayList<>();
        for (ObjectiveCard startingCard : DECK_OBJECTIVE) {
            Starting_deck1.add(startingCard);
        }
    }
}
