package it.polimi.ingsw.controller.userCommands;
public interface UserListener {

    /**
     *
     * @param cmd is used to connect to the lobby
     */
    void receiveCommand(JoinLobbyCommand cmd);

    /**
     *
     * @param cmd is used when the player choose to leave the lobby
     */
    void receiveCommand(EndGameCommand cmd);

    /**
     *
     * @param cmd is used to reque
     */
    void receiveCommand(NumberOfPlayersCommand cmd);

    /**
     *
     * @param cmd is used to choose a name
     */
    void receiveCommand(NameCommand cmd);

    /**
     *
     * @param cmd contains information about how to place a card
     */
    void receiveCommand(PlaceCardCommand cmd);

    /**
     *
     * @param cmd is used by the player to choose the side of the initial card
     */
    void receiveCommand(StartingCardSideCommand cmd);

    /**
     *
     * @param cmd is used by the player to choose the secret objective
     */
    void receiveCommand(SecretObjectiveCommand cmd);

    /**
     *
     * @param cmd is used to choose a card to draw
     */
    void receiveCommand(DrawCardCommand cmd);

    /**
     *
     * @param cmd is used to show the field of a player
     */
    void receiveCommand(ShowFieldCommand cmd);

    /**
     *
     * @param cmd contains the name of the player whose field is to be shown
     */
    void receiveCommand(ShowOtherFieldCommand cmd);

    /**
     *The view will show the leaderboard at that moment
     */
    void receiveCommand(ShowLeaderboardCommand cmd);

    /**
     * The view will show the player their hand
     */
    void receiveCommand(ShowHandCommand cmd);

    /**
     * @param cmd is used to display the common field
     */
    void receiveCommand(ShowCommonFieldCommand cmd);

    /**
     *
     * @param cmd is used to display the player's objectives
     */
    void receiveCommand(ShowObjectivesCommand cmd);

    /**
     * The listener is sent information on the choice of the player
     */
    void receiveCommand(ColourCommand cmd);

    /**
     * The listener is asked details on a card with the id specified in the command
     */
    void receiveCommand(CardDetailCommand cmd);

    /**
     *
     * @param chatCommand contains information about the chat message's body and its recipients
     */
    void receiveCommand(ChatCommand chatCommand);

    /**
     *
     * @param chatLogCommand tells the controller to show the chat logs
     */
    void receiveCommand(ChatLogCommand chatLogCommand);
}








