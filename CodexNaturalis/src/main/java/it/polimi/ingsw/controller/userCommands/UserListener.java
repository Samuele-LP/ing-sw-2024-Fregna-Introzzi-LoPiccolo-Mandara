package it.polimi.ingsw.controller.userCommands;

public interface UserListener {
    /**
     *
     * @param cmd is used to connect to the lobby
     */
    public void receiveCommand(JoinLobbyCommand cmd);

    /**
     *
     * @param cmd requests the available position for a card
     */
    public void receiveCommand(AvailablePositionCommand cmd);

    /**
     *
     * @param cmd is used when the player choose to leave the lobby
     */
    public void receiveCommand(EndGameCommand cmd);

    /**
     *
     * @param cmd is used to reque
     */
    public void receiveCommand(NumberOfPlayerCommand cmd);

    /**
     *
     * @param cmd is used to choose a name
     */
    public void receiveCommand(NameCommand cmd);

    /**
     *
     * @param cmd
     */
    public void receiveCommand(PlaceCardCommand cmd);


}









