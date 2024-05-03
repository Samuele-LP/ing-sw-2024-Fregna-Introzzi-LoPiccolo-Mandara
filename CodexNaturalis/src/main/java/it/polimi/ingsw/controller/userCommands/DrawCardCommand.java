package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.model.enums.PlayerDrawChoice;

public class DrawCardCommand extends UserCommand{
    private final String command = "drw_card";
    private PlayerDrawChoice choice;

    public PlayerDrawChoice getChoice() {
        return choice;
    }

    DrawCardCommand(PlayerDrawChoice choise){
        this.choice = choise;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
