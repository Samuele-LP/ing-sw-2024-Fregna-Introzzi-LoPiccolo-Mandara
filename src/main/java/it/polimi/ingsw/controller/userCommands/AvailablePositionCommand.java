package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.Point;

import javax.swing.text.Position;

public class AvailablePositionCommand extends UserCommand{
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
