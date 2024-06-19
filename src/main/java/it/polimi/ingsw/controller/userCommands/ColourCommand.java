package it.polimi.ingsw.controller.userCommands;

/**
 * Command used to choose the colour of the player's pawn
 */
public class ColourCommand extends UserCommand{
    private final String chosenColour;
    public ColourCommand(String chosenColour) {
        this.chosenColour = chosenColour;
    }

    /**
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    /**
     * @return a String that corresponds to the ansi color code value of either red blue, green or yellow
     */
    public String getChosenColour() {
        return chosenColour;
    }
}
