package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 *Message containing information about a player's choice about their colour
 */
public class ChosenColourMessage extends ClientToServerMessage {
    private final String colour;

    public ChosenColourMessage(String colour) {
        this.colour = colour;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this,sender);
    }

    /**
     *
     * @return a String containing the ansi colour code for teh chosen colour
     */
    public String getColour() {
        return colour;
    }
}
