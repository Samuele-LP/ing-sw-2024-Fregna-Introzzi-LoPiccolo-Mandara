package it.polimi.ingsw.controller.userCommands;

/**
 * When the player wants to view another player's field, this command is sent.
 */
public class ShowOtherFieldCommand extends UserCommand {

    private final String opponentName;

    /**
     * Constructs a ShowOtherFieldCommand with the specified opponent's name.
     *
     * @param opponentName the name of the opponent whose field is to be shown
     */
    public ShowOtherFieldCommand(String opponentName) {
        this.opponentName = opponentName;
    }

    /**
     * Gets the name of the opponent whose field is to be shown.
     *
     * @return the name of the opponent
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * Sends the command to the specified UserListener, instructing it to show the specified opponent's field.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
