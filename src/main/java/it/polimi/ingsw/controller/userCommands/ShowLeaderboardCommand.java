package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. The player asks to see the leaderboard by sending this command
 */
public class ShowLeaderboardCommand extends UserCommand{
    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

}
