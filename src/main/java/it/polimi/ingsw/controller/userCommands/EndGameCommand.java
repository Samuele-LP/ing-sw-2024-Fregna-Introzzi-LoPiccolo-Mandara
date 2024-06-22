package it.polimi.ingsw.controller.userCommands;

/**
 * Command generated when a player voluntarily quits the program.
 */
public class EndGameCommand extends UserCommand {

    /**
     * Sends the command to the specified UserListener, signaling the intention to leave the lobby.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
