package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class is used to ask about another player's current field's information.
 * The control for validity of the name of the requested player's name is done client-side
 */
public class PlayerFieldMessage extends Message {
    /**
     * parameter used to determine whose field data has been received
     */
    private final String fieldOwner;

    public PlayerFieldMessage(String fieldOwner) {
        this.fieldOwner = fieldOwner;
    }

    /**
     * @return the name of the player whose field has been sent
     */
    public String getFieldOwner() {
        return fieldOwner;
    }
    /*
    TODO: decide how to send playing field data to the client
     */
}
