package it.polimi.ingsw.controller.userCommands;

/**
 * When this command is generated the view switches, if possible, to the player's field
 */
public class ShowFieldCommand extends UserCommand{

    public ShowFieldCommand(){
    }

    /**
     *
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
