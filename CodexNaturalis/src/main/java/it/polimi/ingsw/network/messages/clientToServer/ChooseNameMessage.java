package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message sent by a client when choosing their name
 */
public class ChooseNameMessage extends Message {
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
}
