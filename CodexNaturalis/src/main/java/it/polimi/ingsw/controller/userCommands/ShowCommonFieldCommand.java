package it.polimi.ingsw.controller.userCommands;

public class ShowCommonFieldCommand extends UserCommand{

    /**
     * @param lis is passed the command  by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
