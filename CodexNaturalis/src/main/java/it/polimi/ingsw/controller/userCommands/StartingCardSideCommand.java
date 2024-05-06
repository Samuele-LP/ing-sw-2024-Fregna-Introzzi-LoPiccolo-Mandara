package it.polimi.ingsw.controller.userCommands;

public class StartingCardSideCommand extends UserCommand{
    private final String command = "str_side";
    private final boolean side;

    public StartingCardSideCommand(boolean side){
        this.side = side;
    }

    /**
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @return the side
     */
    public boolean getSide() {
        return side;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
