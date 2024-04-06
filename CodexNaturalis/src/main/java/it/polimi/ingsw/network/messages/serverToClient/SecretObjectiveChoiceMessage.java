package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * This message sends the player information on the two choices available for the secret objective
 */
public class SecretObjectiveChoiceMessage extends Message {
    private final String firstChoice;
    private final String secondChoice;
    public SecretObjectiveChoiceMessage(String firstChoice, String secondChoice) {
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
    }

    /**
     *
     * @return the first of the two choices for the secret objective
     */
    public String getFirstChoice() {
        return firstChoice;
    }

    /**
     * @return the second of the two choices for the secret objective
     */
    public String getSecondChoice() {
        return secondChoice;
    }
}
