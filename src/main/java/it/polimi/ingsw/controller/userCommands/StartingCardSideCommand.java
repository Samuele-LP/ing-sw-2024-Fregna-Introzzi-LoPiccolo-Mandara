package it.polimi.ingsw.controller.userCommands;

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
