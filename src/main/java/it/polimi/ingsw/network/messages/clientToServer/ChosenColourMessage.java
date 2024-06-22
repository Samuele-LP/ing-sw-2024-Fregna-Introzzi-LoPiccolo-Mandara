package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message containing information about a player's choice of colour.
 */
public class ChosenColourMessage extends ClientToServerMessage {

    /**
     * The chosen colour in ANSI colour code format.
     */
    private final String colour;

    /**
     * Constructor to initialize the chosen colour.
     *
     * @param colour the chosen colour in ANSI colour code format
     */
    public ChosenColourMessage(String colour) {
        this.colour = colour;
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

    /**
     * Gets the chosen colour.
     *
     * @return a String containing the ANSI colour code for the chosen colour
     */
    public String getColour() {
        return colour;
    }
}
