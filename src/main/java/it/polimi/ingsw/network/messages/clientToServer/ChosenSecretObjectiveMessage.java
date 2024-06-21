package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message that contains the information on the chosen secret objective,input validity checks are done by the client
 */
public class ChosenSecretObjectiveMessage extends ClientToServerMessage {

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

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
