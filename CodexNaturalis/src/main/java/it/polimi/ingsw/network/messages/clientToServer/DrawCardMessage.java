package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;

/**
 * Message that contains the player's draw choice
 */
public class DrawCardMessage extends Message {

    private final PlayerDrawChoice playerChoice;

    public DrawCardMessage(PlayerDrawChoice playerChoice) {
        this.playerChoice = playerChoice;
    }

    /**
     *
     * @return the choice
     */
    public PlayerDrawChoice getChoice() {
        return playerChoice;
    }
}
