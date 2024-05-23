package it.polimi.ingsw.controller.userCommands;

/**
 * The player asks to see the leaderboard by sending this command
 */
public class ShowLeaderboardCommand extends UserCommand{
    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
