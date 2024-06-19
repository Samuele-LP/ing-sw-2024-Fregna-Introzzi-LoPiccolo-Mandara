package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The secret objective or, if it hasn't been set, the secret objective choices are shown
 */
public class ShowObjectivesCommand extends UserCommand{
    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
