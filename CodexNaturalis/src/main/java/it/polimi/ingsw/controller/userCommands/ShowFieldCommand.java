package it.polimi.ingsw.controller.userCommands;

public class ShowFieldCommand extends UserCommand{
    private final String command = "show_field";
    private int NumberOfField;

    /**
     *
     * @return
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @return NumberOfField
     */
    public int getNumberOfField() {
        return NumberOfField;
    }

    /**
     *
     * @param NumberOfField
     */
    public ShowFieldCommand(int NumberOfField){
        this.NumberOfField = NumberOfField;
    }

    /**
     *
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
