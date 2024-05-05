package it.polimi.ingsw.controller.userCommands;

public abstract class UserCommand {
    /**
     *
     * @param  lis is the command passed by the player
     */
    public abstract void sendCommand(UserListener lis);
}
