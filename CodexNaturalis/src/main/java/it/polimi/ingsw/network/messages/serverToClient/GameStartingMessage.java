package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message sent after the game has started, it has the information about the player's starting hand
 *(that has been drawn from the decks),the starting card (assigned randomly), the four visible cards
 * and the colour of the deck's top card's back
 */
public class GameStartingMessage extends it.polimi.ingsw.network.messages.Message {
    /**
     * Contains the other player's names, ordered in the order of the player's turns
     */
    private final List<String> playersInfo;
    private final String startingCardData;
    private final List<String> playerHandData;
    private final Message sharedFieldData;
    public GameStartingMessage(List<String> playersInfo, String startingCardData, List<String> playerHandData, Message sharedFieldData) {
        this.playersInfo = playersInfo;
        this.startingCardData = startingCardData;
        this.playerHandData = playerHandData;
        this.sharedFieldData = sharedFieldData;
    }

    /**
     * Getter for the playersInfo field
     * @return an ArrayList containing the other player's names, ordered in the order of the player's turns
     */
    public List<String> getPlayersInfo(){
        return new ArrayList<>(playersInfo);
    }

    /**
     *
     * @return the StartingCard's data as a String
     */
    public String getStartingCardData() {
        return startingCardData;
    }

    public List<String> getPlayerHandData() {
        return playerHandData;
    }

    /**
     * This method returns a message that has information on the common rea of the field
     * @return a SharedFieldUpdateMessage
     */
    public Message getSharedFieldData() {
        return sharedFieldData;
    }
}
