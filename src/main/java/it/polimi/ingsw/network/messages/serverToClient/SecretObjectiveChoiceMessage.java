package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The SecretObjectiveChoiceMessage class represents a message that sends the player information
 * on the two choices available for their secret objective.
 */
public class SecretObjectiveChoiceMessage extends ServerToClientMessage {

    /**
     * The first choice for the secret objective.
     */
    private final int firstChoice;

    /**
     * The second choice for the secret objective.
     */
    private final int secondChoice;

    /**
     * Constructs a SecretObjectiveChoiceMessage with the specified choices for the secret objective.
     *
     * @param firstChoice  the first choice for the secret objective.
     * @param secondChoice the second choice for the secret objective.
     */
    public SecretObjectiveChoiceMessage(int firstChoice, int secondChoice) {
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
    }

    /**
     * Returns the first of the two choices for the secret objective.
     *
     * @return the first choice.
     */
    public int getFirstChoice() {
        return firstChoice;
    }

    /**
     * Returns the second of the two choices for the secret objective.
     *
     * @return the second choice.
     */
    public int getSecondChoice() {
        return secondChoice;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the SecretObjectiveChoiceMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
