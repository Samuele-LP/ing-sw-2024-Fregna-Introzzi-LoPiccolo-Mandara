package it.polimi.ingsw.controller.userCommands;

/**
 * Command used to choose the colour of the player's pawn.
 */
public class ColourCommand extends UserCommand{

    private final String chosenColour;

    /**
     * Constructs a ColourCommand with the specified colour.
     *
     * @param chosenColour the colour chosen by the player
     */
    public ColourCommand(String chosenColour) {
        this.chosenColour = chosenColour;
    }

    /**
     * Gets the chosen colour.
     *
     * @return a String that corresponds to the ANSI color code value of either red, blue, green, or yellow
     */
    public String getChosenColour() {
        return chosenColour;
    }

    /**
     * Sends the command to the specified UserListener.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
