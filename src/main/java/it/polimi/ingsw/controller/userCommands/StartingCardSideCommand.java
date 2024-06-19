package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains information about how the player wants to place their starting card
 */
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
}
