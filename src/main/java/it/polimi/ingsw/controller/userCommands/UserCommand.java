package it.polimi.ingsw.controller.userCommands;

/**
 * Abstract class used to define commands sent by the user to the client-side controller
 */
public abstract class UserCommand {
    /**
     *
     * @param  lis handles the command passed by the player
     */
    public abstract void sendCommand(UserListener lis);
}
