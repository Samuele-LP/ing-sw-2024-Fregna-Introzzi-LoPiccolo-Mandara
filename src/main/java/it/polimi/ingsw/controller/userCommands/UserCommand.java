package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class UserCommand {
    /**
     *
     * @param  lis is the command passed by the player
     */
    public abstract void sendCommand(UserListener lis);


    /**
     * @param lis
     * @return
     */
    public abstract EventHandler<MouseEvent> getCommandHandler(UserListener lis);
}
