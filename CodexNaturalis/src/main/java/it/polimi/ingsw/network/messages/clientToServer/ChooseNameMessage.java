package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Message sent by a client when choosing their name
 */
public class ChooseNameMessage extends ClientToServerMessage {
    private final String name;
    public ChooseNameMessage(String name) {
        this.name = name;
    }

    /**
     *
     * @return the player's name choice
     */
    public String getName() {
        return name;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }
}
