package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class NameCommand extends UserCommand{
    private final String name;

    /**
     *
     * @param name is the name chosen by the player
     */
    public NameCommand(String name){
        this.name = name;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }

    public String getName( ){
        return name;
    }
}
