package it.polimi.ingsw.controller.userCommands;

public interface UserListener {

    /**
     * Receives a command to connect to the lobby.
     *
     * @param cmd the command to join the lobby
     */
    void receiveCommand(JoinLobbyCommand cmd);

    /**
     * Receives a command when the player chooses to leave the game.
     *
     * @param cmd the command to end the game
     */
    void receiveCommand(EndGameCommand cmd);

    /**
     * Receives a command to request the number of players.
     *
     * @param cmd the command to specify the number of players
     */
    void receiveCommand(NumberOfPlayersCommand cmd);

    /**
     * Receives a command to choose a player's name.
     *
     * @param cmd the command to set the player's name
     */
    void receiveCommand(NameCommand cmd);

    /**
     * Receives a command containing information about how to place a card.
     *
     * @param cmd the command with card placement details
     */
    void receiveCommand(PlaceCardCommand cmd);

    /**
     * Receives a command for the player to choose the side of the initial card.
     *
     * @param cmd the command to select the starting card side
     */
    void receiveCommand(StartingCardSideCommand cmd);

    /**
     * Receives a command for the player to choose the secret objective.
     *
     * @param cmd the command to select the secret objective
     */
    void receiveCommand(SecretObjectiveCommand cmd);

    /**
     * Receives a command to choose a card to draw.
     *
     * @param cmd the command to draw a card
     */
    void receiveCommand(DrawCardCommand cmd);

    /**
     * Receives a command to show the player's field.
     *
     * @param cmd the command to display the player's field
     */
    void receiveCommand(ShowFieldCommand cmd);

    /**
     * Receives a command containing the name of the player whose field is to be shown.
     *
     * @param cmd the command to display another player's field
     */
    void receiveCommand(ShowOtherFieldCommand cmd);

    /**
     * Receives a command to show the leaderboard.
     */
    void receiveCommand(ShowLeaderboardCommand cmd);

    /**
     * Receives a command to show the player's hand.
     */
    void receiveCommand(ShowHandCommand cmd);

    /**
     * Receives a command to display the common field.
     *
     * @param cmd the command to show the common field
     */
    void receiveCommand(ShowCommonFieldCommand cmd);

    /**
     * Receives a command to display the player's objectives.
     *
     * @param cmd the command to show the player's objectives
     */
    void receiveCommand(ShowObjectivesCommand cmd);

    /**
     * Receives a command with information on the player's color choice.
     *
     * @param cmd the command to choose a color
     */
    void receiveCommand(ColourCommand cmd);

    /**
     * Receives a command to request details on a card with the specified ID.
     *
     * @param cmd the command to get card details
     */
    void receiveCommand(CardDetailCommand cmd);

    /**
     * Receives a chat command containing information about the chat message's body and its recipients.
     *
     * @param chatCommand the command with chat message details
     */
    void receiveCommand(ChatCommand chatCommand);

    /**
     * Receives a command to show the chat logs.
     *
     * @param chatLogCommand the command to display chat logs
     */
    void receiveCommand(ChatLogCommand chatLogCommand);
}
