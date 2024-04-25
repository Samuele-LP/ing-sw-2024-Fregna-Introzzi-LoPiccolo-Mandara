package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.clientToServer.*;

/**
 * The message listener interface contains methods that are necessary to pass messages to controller to manage the game logic
 */
public interface MessageListener {

    /**
     * @param mes is the message containing infos about the card the player wants to draw
     */
    void HandleDrawCardMessage(DrawCardMessage mes);

    /**
     * @param mes is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     */
    void HandlePlaceCard(PlaceCardMessage mes);

    /**
     * @param mes is the message with the secretObjective card the player chose between the twos dealt
     */
    void HandleChoosenSecretObjective(ChosenSecretObjectiveMessage mes);

    /**
     * @param mes that allows the starting of the game
     */
    void HandleStartGame(StartGameMessage mes);

    /**
     *
     * @param mes is used to choose the side of the starting card
     */

    void HandleChooseStartingCardSideMessage(ChooseStartingCardSideMessage mes);

    /**
     *
     * @param mes when a player is looking for a lobby
     */
    void HandleFindLobby(FindLobbyMessage mes);

    /**
     *
     * @param mes is used by the first player to choose how big is the lobby
     */
    void HandleNumberOfPlayer(NumberOfPlayersMessage mes);

    /**
     *
     * @param mes is the message used by the players for knowing where they can place a card
     */
    void HandleRequestAvailablePosition(RequestAvailablePositionsMessage mes);

    /**
     *
     * @param mes is used if the connection between the client and the server
     */
    void HandleClientTryRecconnection(ClientTryReconnectionMessage mes);

    /**
     *
     * @param mes when a player have to leave the lobby
     */
    void HandleClientDisconnectedVoluntarily(ClientDisconnectedVoluntarilyMessage mes);

    /**
     *
     * @param mes is the name choosen by the player
     */
    void HandleChooseName(ChooseNameMessage mes);
}
