package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The user wants to see their hand.
 */
public class ShowHandCommand extends UserCommand {

    /**
     * Sends the command to the specified UserListener, instructing it to show the player's hand.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
