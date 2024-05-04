package it.polimi.ingsw.controller.userCommands;

/**
 * The user wants to see their hand.
 */
public class ShowHandCommand extends UserCommand{
    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
