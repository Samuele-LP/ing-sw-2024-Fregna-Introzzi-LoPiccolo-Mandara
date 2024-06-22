package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains information about how the player wants to place their starting card.
 */
public class StartingCardSideCommand extends UserCommand {

    private final boolean side;

    /**
     * Constructs a StartingCardSideCommand with the specified side for the starting card.
     *
     * @param side the side of the starting card (true for one side, false for the other side)
     */
    public StartingCardSideCommand(boolean side){
        this.side = side;
    }

    /**
     * Gets the side of the starting card.
     *
     * @return the side of the starting card (true for one side, false for the other side)
     */
    public boolean getSide() {
        return side;
    }

    /**
     * Sends the command to the specified UserListener, instructing it to place the starting card with the chosen side.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
