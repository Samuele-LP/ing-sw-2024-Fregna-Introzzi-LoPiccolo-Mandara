package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains the information to send a chat message
 */
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
     * @return the name of the recipient or null. Depending on the result of the isGlobal() method
     */
    public String getHead() {
        return global ?null:head;
    }

    /**
     * @return the chat text message
     */
    public String getBody() {
        return body;
    }

    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

}
