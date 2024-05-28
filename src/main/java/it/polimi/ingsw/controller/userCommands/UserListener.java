package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.controller.ClientControllerState;
import it.polimi.ingsw.model.cards.Card;

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

    /**
     *
     * @param cmd is used by the player to choose the side of the initial card
     */
    public void receiveCommand(StartingCardSideCommand cmd);

    /**
     *
     * @param cmd is used by the player to choose the secret objective
     */
    public void receiveCommand(SecretObjectiveCommand cmd);

    /**
     *
     * @param cmd is used to choose a card to draw
     */
    public void receiveCommand(DrawCardCommand cmd);

    /**
     *
     * @param cmd is used to show the field of a player
     */
    public void receiveCommand(ShowFieldCommand cmd);

    /**
     *
     * @param cmd contains the name of the player whose field is to be shown
     */
    public void receiveCommand(ShowOtherFieldCommand cmd);

    /**
     *The view will show the leaderboard at that moment
     */
    public void receiveCommand(ShowLeaderboardCommand cmd);

    /**
     * The view will show the player their hand
     */
    public void receiveCommand(ShowHandCommand cmd);

    /**
     * @param cmd is used to display the common field
     */
    void receiveCommand(ShowCommonFieldCommand cmd);

    /**
     *
     * @param cmd is used to display the player's objectives
     */
    void receiveCommand(ShowObjectivesCommand cmd);
    ClientControllerState getListenerState();

    /**
     * The listener is sent information on the choice of the player
     */
    void receiveCommand(ColourCommand cmd);

    /**
     * The listener is asked details on a card with the id specified in the command
     */
    void receiveCommand(CardDetailCommand cmd);

    /**
     * @param cmd contains information about the name chosen for the reconnection and the server on which the user wants to reconnect to
     */
    void receiveCommand(ReconnectionCommand cmd);

    void receiveCommand(ChatCommand chatCommand);
}








