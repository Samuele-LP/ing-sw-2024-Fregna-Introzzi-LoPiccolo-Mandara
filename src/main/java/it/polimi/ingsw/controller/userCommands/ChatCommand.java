package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains the information to send a chat message.
 */
public class ChatCommand extends UserCommand {

    private final boolean global;

    /**
     * Contains the name of the recipient or is null if it's for the general chat.
     */
    private final String head;

    private final String body;

    /**
     * Constructs a ChatCommand with the specified parameters.
     *
     * @param global indicates if the message is for all players (true) or a specific player (false)
     * @param head the name of the recipient if the message is not global, otherwise null
     * @param body the text of the chat message
     */
    public ChatCommand(boolean global, String head, String body) {
        this.global = global;
        this.head = head;
        this.body = body;
    }

    /**
     * Indicates whether the message is directed to all players or a single player.
     *
     * @return true if directed to all players, false if directed to a single player
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Returns the name of the recipient or null if the message is global.
     *
     * @return the name of the recipient if the message is not global, otherwise null
     */
    public String getHead() {
        return global ? null : head;
    }

    /**
     * Returns the chat text message.
     *
     * @return the text of the chat message
     */
    public String getBody() {
        return body;
    }

    /**
     * Sends the command to the specified UserListener.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
