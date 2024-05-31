package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Message sent after the game has started, it has the information about the player's starting hand
 *(that has been drawn from the decks),the starting card (assigned randomly), the four visible cards
 * and the colour of the deck's top card's back
 */
public class GameStartingMessage extends ServerToClientMessage {
    /**
     * Contains the other player's names, ordered in the order of the player's turns
     */
    private final List<String> playersInfo;
    private final Integer startingCard;
    private final List<Integer> playerHand;
    private final SharedFieldUpdateMessage sharedFieldData;
    private final int firstCommonObjective;
    private final int secondCommonObjective;
    private final String firstPlayerName;
    public GameStartingMessage(List<String> playersInfo, Integer startingCard, List<Integer> playerHand, SharedFieldUpdateMessage sharedFieldData, int firstCommonObjective, int secondCommonObjective, String firstPlayerName) {
        this.playersInfo = playersInfo;
        this.startingCard = startingCard;
        this.playerHand = playerHand;
        this.sharedFieldData = sharedFieldData;
        this.firstCommonObjective = firstCommonObjective;
        this.secondCommonObjective = secondCommonObjective;
        this.firstPlayerName = firstPlayerName;
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
    public Integer getStartingCard() {
        return startingCard;
    }

    public List<Integer> getPlayerHand() {
        return playerHand;
    }

    /**
     * This method returns a message that has information on the common rea of the field
     * @return a SharedFieldUpdateMessage
     */
    public SharedFieldUpdateMessage getSharedFieldData() {
        return sharedFieldData;
    }

    public int getFirstCommonObjective() {
        return firstCommonObjective;
    }

    public int getSecondCommonObjective() {
        return secondCommonObjective;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }

    public String firstPlayerName() {
        return firstPlayerName;
    }
}
