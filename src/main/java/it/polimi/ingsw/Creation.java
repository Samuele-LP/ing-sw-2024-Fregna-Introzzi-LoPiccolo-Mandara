package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.polimi.ingsw.model.enums.TokenType;

/**
 * Class that is used to create Lists of cards from Json files. Implements the singletorn pattern.<br>
 * There are four different types of card that need to be in four different Json files: <br>
 * ObjectiveCard in ObjectiveDeck.json<br>
 * StartingCard in StartingDeck.json<br>
 * ResourceCard in ResourceDeck.json<br>
 * GoldCard inGoldDeck.json<br><br>
 * Each file only contains one card type, duplicate cards are admissible, but they must all have different IDs.
 * ResourceCard IDs range from 1 to 40<br>GoldCard IDs from41 to 80<br>ResourceCard IDs from 81 to 86<br>ObjectiveCard IDs from 87 to 102
 */
@SuppressWarnings("ALL")
public class Creation {

    private final Gson gsonParser;

    private static Creation instance = null;

    private Creation(){
        gsonParser = new Gson();
        instance = this;
    }

    public static Creation getInstance(){
        return instance==null?new Creation():instance;
    }

    /**
     * In the json file for resource card each card has these attributes:
     * {"pointsOnPlacement": int, the number of points given by this card. Must be >=0 <br>
     * Each of the eight corners must be of type {@link TokenType}.<br>
     * "topRight": {@link TokenType},<br>
     * "topLeft": {@link TokenType},<br>
     * "bottomLeft":{@link TokenType},<br>
     * "bottomRight":{@link TokenType},<br>
     * "backTopRight":{@link TokenType},<br>
     * "backTopLeft":{@link TokenType},<br>
     * "backBottomLeft":{@link TokenType},<br>
     * "backBottomRight":{@link TokenType}, <br>
     * "colour": CardType, indicates the colour of the card and the resource visible on the backside.<br>
     * "isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     * "placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * "ID":int} for resource cards must range from 1 to 40<br>
     *
     * @return a List of Card containing the ResourceCards
     * @throws IOException if the reading from file didn't go well
     */
    public List<Card> getResourceCards() throws IOException {
        Reader reader = new InputStreamReader(Creation.class.getResourceAsStream("/ResourceDeck.json"));
        ResourceCard[] DECK_STARTING = gsonParser.fromJson(reader, ResourceCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }

    /**
     * In the json file for gold cards the cards are structured like this:<br>
     * The first four attributes indicate the conditions for playing a gold card,
     * ie the number of visible symbols of a certain type must be grater or equal to the number specified in the card<br>
     * {"requiredFungi":int,<br>
     * "requiredPlant":int,<br>
     * "requiredAnimal":int,<br>
     * "requiredInsect":int,<br>
     * "awardedPoints":int, the number of points awarded for each time the condition is fulfilled.<br>
     * "pointsCondition":{@link TokenType}, indicate which symbol is taken in consideration fo the calculation of points;
     * we use the value "blocked" if the condition for points is blocking corners and value "empty" if there is no condition<br>
     * Each of the eight corners must be of type {@link TokenType}.<br>
     * "topRight": {@link TokenType},<br>
     * "topLeft": {@link TokenType},<br>
     * "bottomLeft":{@link TokenType},<br>
     * "bottomRight":{@link TokenType},<br>
     * "backTopRight":{@link TokenType},<br>
     * "backTopLeft":{@link TokenType},<br>
     * "backBottomLeft":{@link TokenType},<br>
     * "backBottomRight":{@link TokenType}, <br>
     * "colour": CardType, indicates the colour of the card and the resource visible on the backside.<br>
     * "isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     * "placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * ID":int} for gold cards must range from 41 to 80<br>
     *
     * @return a List of Card containing the GoldCards
     * @throws IOException if the reading from file didn't go well
     */
    public List<Card> getGoldCards() throws IOException {
        Reader reader = new InputStreamReader(Creation.class.getResourceAsStream("/GoldDeck.json"));
            GoldCard[] DECK_GOLD = gsonParser.fromJson(reader, GoldCard[].class);
            reader.close();
            return new ArrayList<>(Arrays.asList(DECK_GOLD));
    }

    /**
     * In the json file for gold cards the cards are structured like this:<br>
     * The first three attributes indicate the central symbols a starting card can have. They shouldn't be of value blocked,
     * but they can be of value empty if there are less than three visible symbols in the center of the card<br>
     * {"centralSymbol1":{@link TokenType},<br>
     * "centralSymbol2":{@link TokenType},<br>
     * "centralSymbol3":{@link TokenType},<br>
     * Each of the eight corners must be of type {@link TokenType}.<br>
     * "topRight": {@link TokenType},<br>
     * "topLeft": {@link TokenType},<br>
     * "bottomLeft":{@link TokenType},<br>
     * "bottomRight":{@link TokenType},<br>
     * "backTopRight":{@link TokenType},<br>
     * "backTopLeft":{@link TokenType},<br>
     * "backBottomLeft":{@link TokenType},<br>
     * "backBottomRight":{@link TokenType}, <br>
     * "colour": CardType, must be starter. To indicate that there is no visible resource given specifically for the starting cards<br>
     * "isFacingUp":boolean, must be initialized as false. Indicates how the card is facing when it is placed<br>
     * "placedInTurn":int, must be initialized as -1 indicates at which point of the game the card has been placed<br>
     * ID":int} for resource cards must range from 81 to 86<br>
     *
     * @return a List of Card containing the StartingCards
     * @throws IOException if the reading from file didn't go well
     */
    public List<Card> getStartingCards() throws IOException {
        Reader reader = new InputStreamReader(Creation.class.getResourceAsStream("/StartingDeck.json"));
        StartingCard[] DECK_STARTING = gsonParser.fromJson(reader, StartingCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_STARTING));
    }

    /**
     * The cards in the json file for ObjectiveCards are structured like this: <br>
     * {"awardedPoints":int, Is the number of points given each time the objective is scored<br>
     * "isPositionalObjective":boolean, indicates whether we take in consideration the attribute positionalRequirements or listRequirements<br>
     * "positionalRequirements":ObjectiveSequence,taken in consideration when isPositionalObjective is true<br>
     * "listRequirements":[{@link TokenType},{@link TokenType},{@link TokenType}],taken in consideration when isPositionalObjective is false.
     * It indicates which set of 3 or less TokenType symbols will be used to determine objective points. Values empty and blocked will NOT be counted for points<br>
     * "ID":int}, for objective cards it goes from 87 to 102<br>
     *
     * @return a List of Card containing the ObjectiveCards
     * @throws IOException if the reading from file didn't go well
     */
    public List<Card> getObjectiveCards() throws IOException {
        Reader reader = new InputStreamReader(Creation.class.getResourceAsStream("/ObjectiveDeck.json"));
        ObjectiveCard[] DECK_OBJECTIVE = gsonParser.fromJson(reader, ObjectiveCard[].class);
        reader.close();
        return new ArrayList<>(Arrays.asList(DECK_OBJECTIVE));
    }
}
