package it.polimi.ingsw.controller.userCommands;

public class NumberOfPlayerCommand extends UserCommand{
    private final String command = "number_player";
    int NumberOfPlayer;

    public String getCommand() {
        return command;
    }

    /**
     *
     * @return
     */
    public int getNumberOfPlayer() {
        return NumberOfPlayer;
    }

    /**
     *
     * @param NumberOfPlayer
     */
    NumberOfPlayerCommand(int NumberOfPlayer){
        this.NumberOfPlayer = NumberOfPlayer;
    }

    /**
     *
     * @return the number of the first player has chosen
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
