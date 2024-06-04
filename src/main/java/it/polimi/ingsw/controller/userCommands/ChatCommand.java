package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.server.ClientHandlerSocket;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ChatCommand extends UserCommand{
    private final boolean global;
    /**
     * Contains the name of the recipient or is null if it's for the general chat
     */
    private final String head;
    private final String body;
    public ChatCommand(boolean global, String head, String body) {
        this.global = global;
        this.head = head;
        this.body = body;
    }

    /**
     *
     * @return true if directed to all players, false if directed for a single player
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     *
     * @return the name of the recipient or null. Depending on the result of the isGlobal() method
     */
    public String getHead() {
        return global ?null:head;
    }

    /**
     *
     * @return the chat text message
     */
    public String getBody() {
        return body;
    }

    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }
}
