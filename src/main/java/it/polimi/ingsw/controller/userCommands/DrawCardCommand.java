package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DrawCardCommand extends UserCommand{

    private final PlayerDrawChoice choice;

    /**
     *
     * @return the choice
     */
    public PlayerDrawChoice getChoice() {
        return choice;
    }

    /**
     *
     * @param choice for setting
     */
    public DrawCardCommand(PlayerDrawChoice choice){
        this.choice = choice;
    }

    /**
     *
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
}
