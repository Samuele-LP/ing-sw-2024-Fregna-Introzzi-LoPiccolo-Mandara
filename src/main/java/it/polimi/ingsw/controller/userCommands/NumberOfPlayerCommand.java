package it.polimi.ingsw.controller.userCommands;

public class NumberOfPlayerCommand extends UserCommand{
    int NumberOfPlayer;
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
    public NumberOfPlayerCommand(int NumberOfPlayer){
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
