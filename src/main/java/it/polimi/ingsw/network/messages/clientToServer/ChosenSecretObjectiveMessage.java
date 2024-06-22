package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message that contains the information on the chosen secret objective.
 * Input validity checks are done by the client.
 */
public class ChosenSecretObjectiveMessage extends ClientToServerMessage {

    /**
     * The ID of the chosen secret objective.
     */
    private final int ID;

    /**
     * Constructor to initialize the chosen secret objective ID.
     *
     * @param id the ID of the chosen secret objective
     */
    public ChosenSecretObjectiveMessage(int id) {
        ID = id;
    }

    /**
     * Gets the ID of the chosen secret objective.
     *
     * @return the ID of the secret objective
     */
    public int getID() {
        return ID;
    }

    /**
     * Executes the message using the given listener and sender.
     *
     * @param lis    the listener to handle the message
     * @param sender the client handler that sent the message
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
