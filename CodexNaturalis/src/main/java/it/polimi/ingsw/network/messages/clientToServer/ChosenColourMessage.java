package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 *
 */
public class ChosenColourMessage extends ClientToServerMessage {
    private final String colour;

    public ChosenColourMessage(String colour) {
        this.colour = colour;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this);
    }

    public String getColour() {
        return colour;
    }
}
