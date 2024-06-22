package it.polimi.ingsw.controller.userCommands;

/**
 * CLI-exclusive command. It returns the detailed information of a card.
 */
public class CardDetailCommand extends UserCommand {
    private final int id;

    /**
     * Constructs a CardDetailCommand with the specified card ID.
     *
     * @param id the ID of the card to retrieve details for
     */

    public CardDetailCommand(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the card which information is requested.
     *
     * @return the ID of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Sends the command to the specified UserListener.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
