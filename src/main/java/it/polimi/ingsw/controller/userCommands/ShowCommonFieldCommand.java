package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command that instructs the system to print only the common field.
 */
public class ShowCommonFieldCommand extends UserCommand {

    /**
     * Sends the command to the specified UserListener, instructing it to show the common field.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
