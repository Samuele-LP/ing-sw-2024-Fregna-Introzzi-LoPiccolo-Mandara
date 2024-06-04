package it.polimi.ingsw.controller.userCommands;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SecretObjectiveCommand extends UserCommand{
    private final int objective;
    /**
     *
     * @return the objective
     */
    public int getObjective() {
        return objective;
    }
    public SecretObjectiveCommand(int objective){
        this.objective = objective;
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
