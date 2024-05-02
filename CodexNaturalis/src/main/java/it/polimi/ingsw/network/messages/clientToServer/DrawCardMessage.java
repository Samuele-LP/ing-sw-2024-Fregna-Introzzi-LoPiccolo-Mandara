package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Message that contains the player's draw choice
 */
public class DrawCardMessage extends ClientToServerMessage {

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

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }
}
