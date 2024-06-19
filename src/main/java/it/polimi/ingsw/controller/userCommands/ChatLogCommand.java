package it.polimi.ingsw.controller.userCommands;

/**
 * Command used to see the entirety of the chat
 */
public class ChatLogCommand extends UserCommand{
    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
