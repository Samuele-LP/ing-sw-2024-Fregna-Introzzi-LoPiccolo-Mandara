package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.controller.ClientControllerState;

public abstract class UserCommand{
    /**
     *
     * @param listener is the command passed by the player
     */
    public void sendCommand(UserListener listener) {}
}
