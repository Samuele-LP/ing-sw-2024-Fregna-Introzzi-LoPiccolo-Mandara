package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.Point;

import javax.swing.text.Position;

public class AvailablePositionCommand extends UserCommand{
    private Point AvailablePosition;
    private final String command = "avb_position";

    AvailablePositionCommand(Point AvailablePosition){
        this.AvailablePosition = AvailablePosition;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
