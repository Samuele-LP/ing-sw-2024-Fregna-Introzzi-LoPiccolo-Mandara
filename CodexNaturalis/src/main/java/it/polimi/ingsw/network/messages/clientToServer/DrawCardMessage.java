package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message that contains the player's draw choice
 */
public class DrawCardMessage extends Message {
    /*
    TODO: decide how to format draw choices
     */
    private final String choice;

    public DrawCardMessage(String choice) {
        this.choice = choice;
    }

    /**
     *
     * @return the choice
     */
    public String getChoice() {
        return choice;
    }
}
