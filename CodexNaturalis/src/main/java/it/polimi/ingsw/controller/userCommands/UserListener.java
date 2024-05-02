package it.polimi.ingsw.controller.userCommands;

public interface UserListener {
    /**
     *
     * @param cmd is used to connect to the lobby
     */
    public void receiveCommand(JoinLobbyCommand cmd);

    /**
     *
     * @param cmd request the available position of a card
     */
    public void receiveCommand(AvailablePositionCommand cmd);

}









