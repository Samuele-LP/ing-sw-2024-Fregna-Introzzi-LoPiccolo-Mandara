package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * The ChatMessage class represents a message sent from the client to the server
 * for chat purposes. It can be either a global message to all players or a private
 * message to a specific player.
 */
public class ChatMessage extends ClientToServerMessage {

    /**
     * Indicates whether the message is global (to all players) or private (to a specific player).
     */
    private final boolean global;

    /**
     * Contains the name of the recipient if it's a private message, or is null if it's for the general chat.
     */
    private final String head;

    /**
     * The content of the chat message.
     */
    private final String body;

    /**
     * Constructs a ChatMessage with the specified parameters.
     *
     * @param global whether the message is global or private.
     * @param head the name of the recipient if the message is private, or null if the message is global.
     * @param body the content of the chat message.
     */
    public ChatMessage(boolean global, String head, String body) {
        this.global = global;
        this.head = head;
        this.body = body;
    }

    /**
     * Executes the message using the provided server-side message listener.
     *
     * @param lis the server-side message listener that handles the ChatMessage.
     * @param sender the client handler that sent the message.
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this,sender);
    }

    /**
     * Returns whether the message is global or private.
     *
     * @return true if the message is directed to all players, false if directed to a single player.
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Returns the name of the recipient if the message is private, or null if the message is global.
     *
     * @return the name of the recipient or null, depending on the result of the isGlobal() method.
     */
    public String getHead() {
        return global ?null:head;
    }

    /**
     * Returns the content of the chat message.
     *
     * @return the chat text message.
     */
    public String getBody() {
        return body;
    }
}
