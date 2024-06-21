package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

/**
 * The ReceivedChatMessage class represents a message received in the chat, either globally or as a whisper.
 */
public class ReceivedChatMessage extends ServerToClientMessage {

    /**
     * The content of the chat message.
     */
    private final String body;

    /**
     * Constructs a ReceivedChatMessage with the specified sender, message body, and whether the message is global.
     *
     * @param sender  the sender of the message.
     * @param body    the content of the message.
     * @param isGlobal whether the message is global or a whisper.
     */
    public ReceivedChatMessage(String sender, String body, boolean isGlobal) {
        if (isGlobal) {
            this.body= sender + " sent a message to everyone:<   " + body + "   >";
        } else {
            this.body= sender + " whispered:< " + body + " >";
        }
    }

    /**
     * Returns the content of the chat message.
     *
     * @return the content of the message.
     */
    public String getBody() {
        return body;
    }

    /**
     * Executes the message using the provided client-side message listener.
     *
     * @param lis the client-side message listener that handles the ReceivedChatMessage.
     */
    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
