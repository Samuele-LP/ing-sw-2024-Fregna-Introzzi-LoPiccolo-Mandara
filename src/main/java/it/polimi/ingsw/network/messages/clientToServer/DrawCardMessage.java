package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message that contains the player's draw choice.
 */
public class DrawCardMessage extends ClientToServerMessage {

    private final PlayerDrawChoice playerChoice;

    /**
     * Constructs a DrawCardMessage with the specified draw choice.
     *
     * @param playerChoice the choice the player made for drawing a card
     */
    public DrawCardMessage(PlayerDrawChoice playerChoice) {
        this.playerChoice = playerChoice;
    }

    /**
     * Gets the player's draw choice.
     *
     * @return the player's draw choice
     */
    public PlayerDrawChoice getChoice() {
        return playerChoice;
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
}
