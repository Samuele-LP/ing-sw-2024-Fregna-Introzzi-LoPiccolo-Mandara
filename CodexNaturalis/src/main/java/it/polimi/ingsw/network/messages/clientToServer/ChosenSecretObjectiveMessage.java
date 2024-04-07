package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message that contains the information on the chosen secret objective,input validity checks are done by the client
 */
public class ChosenSecretObjectiveMessage extends Message {
    private final int ID;
    public ChosenSecretObjectiveMessage(int id) {
        ID = id;
    }
    /**
     * @return the id of the secret objective
     */
    public int getID() {
        return ID;
    }
}
