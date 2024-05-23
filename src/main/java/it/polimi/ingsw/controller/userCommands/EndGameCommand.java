package it.polimi.ingsw.controller.userCommands;

public class EndGameCommand extends UserCommand{
    /**
     *
     * @param lis is the listener used to leave the lobby
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
