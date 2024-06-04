package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Command used to choose the colour of the player
 */
public class ColourCommand extends UserCommand{
    private final String chosenColour;

    public ColourCommand(String chosenColour) {
        this.chosenColour = chosenColour;
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

    public String getChosenColour() {
        return chosenColour;
    }
}
