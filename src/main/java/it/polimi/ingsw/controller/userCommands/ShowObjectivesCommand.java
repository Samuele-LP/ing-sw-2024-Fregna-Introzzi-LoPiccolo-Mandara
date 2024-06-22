package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The secret objective or, if it hasn't been set, the secret objective choices are shown.
 */
public class ShowObjectivesCommand extends UserCommand {

    /**
     * Sends the command to the specified UserListener, instructing it to show the secret objective or choices.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
