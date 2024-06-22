package it.polimi.ingsw.controller.userCommands;

/**
 * Command generated to switch the view, if possible, to the player's field.
 */
public class ShowFieldCommand extends UserCommand {

    /**
     * Constructs a ShowFieldCommand.
     */
    public ShowFieldCommand() {}

    /**
     * Sends the command to the specified UserListener, instructing it to show the player's field.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
