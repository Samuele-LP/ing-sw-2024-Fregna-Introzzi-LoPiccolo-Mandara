package it.polimi.ingsw.controller.userCommands;

public class SideStartingCardCommand {
    private final String command = "str_side";
    private final int side;

    public SideStartingCardCommand(int side){
        this.side = side;
    }
}
