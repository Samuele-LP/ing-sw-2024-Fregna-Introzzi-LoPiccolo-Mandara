package it.polimi.ingsw.controller.userCommands;

/**
 * Command used to see the entirety of the chat.
 */
public class ChatLogCommand extends UserCommand {

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
