package it.polimi.ingsw.controller.userCommands;

/**
 * When the player wants to view another player's field this command is sent
 */
public class ShowOtherFieldCommand extends UserCommand{
    private final String opponentName;

    public ShowOtherFieldCommand(String opponentName) {
        this.opponentName = opponentName;
    }

    /**
     *
     * @return the name of the player whose field is to be shown
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
