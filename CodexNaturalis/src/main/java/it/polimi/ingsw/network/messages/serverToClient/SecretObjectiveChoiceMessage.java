package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * This message sends the player information on the two choices available for the secret objective
 */
public class SecretObjectiveChoiceMessage extends ServerToClientMessage {
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

    @Override
    public void execute(ClientSideMessageListener lis) {

    }
}
