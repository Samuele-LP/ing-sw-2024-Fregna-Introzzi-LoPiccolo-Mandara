package it.polimi.ingsw.controller.userCommands;

/**
 * Abstract class used to define commands sent by the user to the client-side controller.
 * This class serves as a base for all specific user commands that can be sent.
 */
public abstract class UserCommand {

    /**
     * Sends the command to the specified UserListener.
     * Subclasses should implement this method to define how the command is processed by the listener.
     *
     * @param lis the UserListener that will handle this command
     */
    public abstract void sendCommand(UserListener lis);
}
