package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.model.enums.PlayerDrawChoice;

/**
 * Command formed in the drawing phase of the game, tells the controller where to draw from
 */
public class DrawCardCommand extends UserCommand{

    private final PlayerDrawChoice choice;

    /**
     * @return the choice
     */
    public PlayerDrawChoice getChoice() {
        return choice;
    }
    /**
     * @param choice represents where the player wants to draw from
     */
    public DrawCardCommand(PlayerDrawChoice choice){
        this.choice = choice;
    }

    /**
     *
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
