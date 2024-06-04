package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ShowCommonFieldCommand extends UserCommand{

    /**
     * @param lis is passed the command  by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }
}
