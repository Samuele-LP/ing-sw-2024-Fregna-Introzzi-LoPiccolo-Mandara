package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.server.ClientHandler;

/**
 * The message listener interface contains methods that are necessary to pass messages to controller
 * in order to manage the game logic
 */
public interface ServerSideMessageListener {

    /**
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender is the reference to who has sent the message
     */
    void handle(DrawCardMessage mes, ClientHandler sender);

    /**
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender is the reference to who has sent the message
     */
    void handle(PlaceCardMessage mes, ClientHandler sender);

    /**
     * @param mes    is the message with the secretObjective card the player chose between the twos dealt
     * @param sender is the reference to who has sent the message
     */
    void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender);


    /**
     * @param mes    is used to choose the side of the starting card
     * @param sender is the reference to who has sent the message
     */
    void handle(ChooseStartingCardSideMessage mes, ClientHandler sender);

    /**
     * @param mes    when a player is looking for a lobby
     * @param sender is the reference to who has sent the message
     */
    void handle(FindLobbyMessage mes, ClientHandler sender);

    /**
     * @param mes    is used by the first player to choose how big is the lobby
     * @param sender is the reference to who has sent the message
     */
    void handle(NumberOfPlayersMessage mes, ClientHandler sender);

    /**
     * @param mes    is the message used by the players for knowing where they can place a card
     * @param sender is the reference to who has sent the message
     */
    void handle(RequestAvailablePositionsMessage mes, ClientHandler sender);

    /**
     * @param mes    is used if the connection between the client and the server
     * @param sender is the reference to who has sent the message
     */
    void handle(ClientTryReconnectionMessage mes, ClientHandler sender);

    /**
     * @param mes    when a player have to leave the lobby
     * @param sender is the reference to who has sent the message
     */
    void handle(ClientDisconnectedVoluntarilyMessage mes, ClientHandler sender);

    /**
     * @param mes    is the name choosen by the player
     * @param sender is the reference to who has sent the message
     */
    void handle(ChooseNameMessage mes, ClientHandler sender);

    /*
     * @param ping is the ping sent by a connected client
     * @param sender is the client who sent the ping
     */
    void handle(Ping ping, ClientHandler sender);

    /**
     * The listener is notified of a disconnection
     * @param clientHandler is the client who was disconnected
     */
    void disconnectionHappened(ClientHandler clientHandler);

    /**
     * @param mes is the message containing the chosen color for the pawn
     * @param sender is the reference to who has sent the message
     */
    void handle(ChosenColourMessage mes, ClientHandler sender);
}
