package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameStartingMessage class represents a message sent after the game has started.
 * It contains information about the player's starting hand, the starting card assigned randomly,
 * the four visible cards, and the color of the deck's top card's back.
 */
public class GameStartingMessage extends ServerToClientMessage {

    /**
     * The names of the other players, ordered by the order of the player's turns.
     */
    private final List<String> playersInfo;

    /**
     * The starting card assigned to the player.
     */
    private final Integer startingCard;

    /**
     * The player's starting hand.
     */
    private final List<Integer> playerHand;

    /**
     * The shared field data.
     */
    private final SharedFieldUpdateMessage sharedFieldData;

    /**
     * The first common objective.
     */
    private final int firstCommonObjective;

    /**
     * The second common objective.
     */
    private final int secondCommonObjective;

    /**
     * The name of the first player.
     */
    private final String firstPlayerName;

    /**
     * Constructs a GameStartingMessage with the specified parameters.
     *
     * @param playersInfo         the names of the other players.
     * @param startingCard        the starting card assigned to the player.
     * @param playerHand          the player's starting hand.
     * @param sharedFieldData     the shared field data.
     * @param firstCommonObjective the first common objective.
     * @param secondCommonObjective the second common objective.
     * @param firstPlayerName     the name of the first player.
     */
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
     * Returns the names of the other players, ordered by the order of the player's turns.
     *
     * @return an ArrayList containing the names of the other players.
     */
    public List<String> getPlayersInfo(){
        return new ArrayList<>(playersInfo);
    }

    /**
     * Returns the starting card assigned to the player.
     *
     * @return the starting card.
     */
    public Integer getStartingCard() {
        return startingCard;
    }

    /**
     * Returns the player's starting hand.
     *
     * @return the player's starting hand.
     */
    public List<Integer> getPlayerHand() {
        return playerHand;
    }

    /**
     * Returns a message that has information on the common area of the field.
     *
     * @return a SharedFieldUpdateMessage.
     */
    public SharedFieldUpdateMessage getSharedFieldData() {
        return sharedFieldData;
    }

    /**
     * Returns the first common objective.
     *
     * @return the first common objective.
     */
    public int getFirstCommonObjective() {
        return firstCommonObjective;
    }

    /**
     * Returns the second common objective.
     *
     * @return the second common objective.
     */
    public int getSecondCommonObjective() {
        return secondCommonObjective;
    }

    /**
     * Returns the name of the first player.
     *
     * @return the name of the first player.
     */
    public String firstPlayerName() {
        return firstPlayerName;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the GameStartingMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
