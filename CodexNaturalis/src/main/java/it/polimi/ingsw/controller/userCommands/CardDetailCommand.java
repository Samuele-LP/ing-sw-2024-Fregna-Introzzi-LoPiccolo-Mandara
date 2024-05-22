package it.polimi.ingsw.controller.userCommands;

public class CardDetailCommand extends UserCommand{
    private final int id;

    public CardDetailCommand(int id) {
        this.id = id;
    }

    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    public int getId() {
        return id;
    }
}
