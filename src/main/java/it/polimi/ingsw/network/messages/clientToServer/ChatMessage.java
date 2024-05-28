package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

public class ChatMessage extends ClientToServerMessage {
    private final boolean global;
    /**
     * Contains the name of the recipient or is null if it's for the general chat
     */
    private final String head;
    private final String body;

    public ChatMessage(boolean global, String head, String body) {
        this.global = global;
        this.head = head;
        this.body = body;
    }
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this,sender);
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
}
