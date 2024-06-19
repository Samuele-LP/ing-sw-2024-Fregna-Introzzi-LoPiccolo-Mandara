package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. It returns the detailed information of a card
 */
public class CardDetailCommand extends UserCommand{
    private final int id;

    public CardDetailCommand(int id) {
        this.id = id;
    }

    /**
     * @param lis is the command p
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    /**
     * @return the id of the card which information is requested
     */
    public int getId() {
        return id;
    }
}
