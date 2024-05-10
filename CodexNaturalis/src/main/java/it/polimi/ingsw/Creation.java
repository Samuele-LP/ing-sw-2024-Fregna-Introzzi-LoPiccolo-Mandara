package it.polimi.ingsw;
import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

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
    private final static String goldDeckPath="CodexNaturalis/src/main/resources/GoldDeck.json";
    private final static String resourceDeckPath="CodexNaturalis/src/main/resources/ResourceDeck.json";
    private final static String startingDeckPath="CodexNaturalis/src/main/resources/StartingDeck.json";
    private final static String objectiveDeckPath="CodexNaturalis/src/main/resources/ObjectiveDeck.json";
    private static final Logger LOGGER = Logger.getLogger( Creation.class.getName() );
    private final static Gson gsonParser=new Gson();
    /**
     *In the json file for resource card each card has these attributes:
     * {"pointsOnPlacement": int, the number of points given by this card. Must be >=0 <br>
     * Each of the eight corners must be of type TokenType.<br>
     * "topRight": TokenType,<br>
     * "topLeft": TokenType,<br>
     * "bottomLeft":TokenType,<br>
     * "bottomRight":TokenType,<br>
     * "backTopRight":TokenType,<br>
     * "backTopLeft":TokenType,<br>
     * "backBottomLeft":TokenType,<br>
     * "backBottomRight":TokenType, <br>
     * "colour": CardType, indicates the colour of the card and the resource visible on the backside.<br>
     * "isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     * "placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * "ID":int} for resource cards must range from 1 to 40<br>
     * @return a List of Card containing the ResourceCards
     * @throws IOException if the reading from file didn't go well
     */
    public static List<Card> getResourceCards() throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        FileReader reader = new FileReader(resourceDeckPath);
        ResourceCard[] DECK_STARTING = gsonParser.fromJson(reader, ResourceCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }
    /**
     * In the json file for gold cards the cards are structured like this:<br>
     * The first four attributes indicate the conditions for playing a gold card,
     * ie the number of visible symbols of a certain type must be grater or equal to the number specified in the card<br>
     *{"requiredFungi":int,<br>
     * "requiredPlant":int,<br>
     * "requiredAnimal":int,<br>
     * "requiredInsect":int,<br>
     * "awardedPoints":int, the number of points awarded for each time the condition is fulfilled.<br>
     *"pointsCondition":TokenType, indicate which symbol is taken in consideration fo the calculation of points;
     * we use the value "blocked" if the condition for points is blocking corners and value "empty" if there is no condition<br>
     *Each of the eight corners must be of type TokenType.<br>
     *"topRight": TokenType,<br>
     *"topLeft": TokenType,<br>
     *"bottomLeft":TokenType,<br>
     *"bottomRight":TokenType,<br>
     *"backTopRight":TokenType,<br>
     *"backTopLeft":TokenType,<br>
     *"backBottomLeft":TokenType,<br>
     *"backBottomRight":TokenType, <br>
     *"colour": CardType, indicates the colour of the card and the resource visible on the backside.<br>
     *"isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     *"placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * ID":int} for gold cards must range from 41 to 80<br>
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
     * In the json file for gold cards the cards are structured like this:<br>
     *The first three attributes indicate the central symbols a starting card can have. They shouldn't be of value blocked,
     * but they can be of value empty if there are less than three visible symbols in the center of the card<br>
     * {"centralSymbol1":TokenType,<br>
     * "centralSymbol2":TokenType,<br>
     * "centralSymbol3":TokenType,<br>
     * Each of the eight corners must be of type TokenType.<br>
     *"topRight": TokenType,<br>
     *"topLeft": TokenType,<br>
     *"bottomLeft":TokenType,<br>
     *"bottomRight":TokenType,<br>
     *"backTopRight":TokenType,<br>
     *"backTopLeft":TokenType,<br>
     *"backBottomLeft":TokenType,<br>
     *"backBottomRight":TokenType, <br>
     *"colour": CardType, must be starter. To indicate that there is no visible resource given specifically for the starting cards<br>
     *"isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     *"placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * ID":int} for resource cards must range from 81 to 86<br>
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
     * The cards in the json file for ObjectiveCards are structured like this: <br>
     *{"awardedPoints":int, Is the number of points given each time the objective is scored<br>
     *   "isPositionalObjective":boolean, indicates whether we take in consideration the attribute positionalRequirements or listRequirements<br>
     *  "positionalRequirements":ObjectiveSequence,taken in consideration when isPositionalObjective is true<br>
     *  "listRequirements":[TokenType,TokenType,TokenType],taken in consideration when isPositionalObjective is false.
     *  It indicates which set of 3 or less TokenType symbols will be used to determine objective points. Values empty and blocked will NOT be counted for points<br>
     *  "ID":int}, for objective cards it goes from 87 to 102<br>
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
