package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CardDetailCommand extends UserCommand{
    private final int id;

    public CardDetailCommand(int id) {
        this.id = id;
    }

    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    @Override
    public EventHandler<MouseEvent> getCommandHandler(UserListener lis) {
        return null;
    }

    public int getId() {
        return id;
    }
}
