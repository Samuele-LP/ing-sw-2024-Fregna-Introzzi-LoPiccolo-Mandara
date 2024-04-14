package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * This message sends the player information on the two choices available for the secret objective
 */
public class SecretObjectiveChoiceMessage extends Message {
    private final int firstChoice;
    private final int secondChoice;
    public SecretObjectiveChoiceMessage(int firstChoice, int secondChoice) {
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
    }

    /**
     *
     * @return the first of the two choices for the secret objective
     */
    public int getFirstChoice() {
        return firstChoice;
    }

    /**
     * @return the second of the two choices for the secret objective
     */
    public int getSecondChoice() {
        return secondChoice;
    }
}
