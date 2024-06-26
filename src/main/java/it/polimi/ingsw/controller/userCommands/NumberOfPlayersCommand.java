package it.polimi.ingsw.controller.userCommands;

/**
 * Command generated if the player was the first connection to the server,
 * granting them the right to choose the number of players in the game.
 */
public class NumberOfPlayersCommand extends UserCommand{

    private final int numPlayers;

    /**
     * Constructor for NumberOfPlayersCommand.
     *
     * @param numPlayers the number of players chosen by the first connected player
     */
    public NumberOfPlayersCommand(int numPlayers){
        this.numPlayers = numPlayers;
    }

    /**
     * @return the number of players chosen for the game
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Sends the command to the specified UserListener, signaling the number of players.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
