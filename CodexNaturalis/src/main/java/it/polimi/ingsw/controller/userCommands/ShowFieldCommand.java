package it.polimi.ingsw.controller.userCommands;

public class ShowFieldCommand extends UserCommand{

    public ShowFieldCommand(){
    }

    /**
     *
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
