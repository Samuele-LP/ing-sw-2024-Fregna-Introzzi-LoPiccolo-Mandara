package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * The ChooseNameMessage class represents a message sent by a client when choosing their name.
 */
public class ChooseNameMessage extends ClientToServerMessage {

    /**
     * The player's chosen name.
     */
    private final String name;

    /**
     * Constructs a ChooseNameMessage with the specified name.
     *
     * @param name the player's chosen name.
     */
    public ChooseNameMessage(String name) {
        this.name = name;
    }

    /**
     * Returns the player's chosen name.
     *
     * @return the player's name choice.
     */
    public String getName() {
        return name;
    }

    /**
     * Executes the message using the provided server-side message listener.
     *
     * @param lis the server-side message listener that handles the ChooseNameMessage.
     * @param sender the client handler that sent the message.
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
