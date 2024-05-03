package it.polimi.ingsw.controller.userCommands;

public class SideStartingCardCommand extends UserCommand{
    private final String command = "str_side";
    private final int side;

    public SideStartingCardCommand(int side){
        this.side = side;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
