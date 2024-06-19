package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The user wants to see their hand.
 */
public class ShowHandCommand extends UserCommand{
    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
