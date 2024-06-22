package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The player asks to see the leaderboard by sending this command.
 */
public class ShowLeaderboardCommand extends UserCommand {

    /**
     * Sends the command to the specified UserListener, instructing it to show the leaderboard.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
