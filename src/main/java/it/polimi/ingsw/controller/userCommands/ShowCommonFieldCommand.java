package it.polimi.ingsw.controller.userCommands;

/**
 * Cli-exclusive command, only the common field is printed
 */
public class ShowCommonFieldCommand extends UserCommand{
    /**
     * @param lis is passed the command  by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
