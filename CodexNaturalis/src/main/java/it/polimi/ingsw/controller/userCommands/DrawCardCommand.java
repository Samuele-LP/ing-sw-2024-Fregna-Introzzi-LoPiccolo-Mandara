package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.model.enums.PlayerDrawChoice;

public class DrawCardCommand extends UserCommand{
    private final String command = "drw_card";
    private PlayerDrawChoice choice;

    /**
     *
     * @return the choice
     */
    public PlayerDrawChoice getChoice() {
        return choice;
    }

    /**
     *
     * @param choise for setting
     */
    DrawCardCommand(PlayerDrawChoice choise){
        this.choice = choise;
    }

    /**
     *
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
