package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class StartingCardSideCommand extends UserCommand{
    private final boolean side;

    public StartingCardSideCommand(boolean side){
        this.side = side;
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

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }
}
