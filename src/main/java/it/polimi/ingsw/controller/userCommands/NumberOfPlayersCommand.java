package it.polimi.ingsw.controller.userCommands;

/**
 * Command generated if the player was the first connection to the server, they have the right to choose the number of players in the game
 */
public class NumberOfPlayersCommand extends UserCommand{
    int numPlayers;
    public int getNumPlayers() {
        return numPlayers;
    }
    public NumberOfPlayersCommand(int numPlayers){
        this.numPlayers = numPlayers;
    }
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

}
