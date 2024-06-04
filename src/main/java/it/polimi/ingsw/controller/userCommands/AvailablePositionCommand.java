package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javax.swing.text.Position;

public class AvailablePositionCommand extends UserCommand{
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }
}
