package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Ping;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.server.ClientHandler;

/**
 * The message listener interface contains methods that are necessary to pass messages to the controller
 * in order to manage the game logic.
 */
public interface ServerSideMessageListener {

    /**
     * Handles the message containing information about the card the player wants to draw.
     *
     * @param mes    the message containing details about the card draw
     * @param sender the reference to the client who sent the message
     */
    void handle(DrawCardMessage mes, ClientHandler sender);

    /**
     * Handles the message containing information about the card the player wants to place,
     * where they want to place it, and on which side.
     *
     * @param mes    the message containing details about the card placement
     * @param sender the reference to the client who sent the message
     */
    void handle(PlaceCardMessage mes, ClientHandler sender);

    /**
     * Handles the message with the secret objective card the player chose between the two dealt.
     *
     * @param mes    the message containing details about the chosen secret objective
     * @param sender the reference to the client who sent the message
     */
    void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender);


    /**
     * Handles the message used to choose the side of the starting card.
     *
     * @param mes    the message containing details about the chosen side of the starting card
     * @param sender the reference to the client who sent the message
     */
    void handle(ChooseStartingCardSideMessage mes, ClientHandler sender);

    /**
     * Handles the message when a player is looking for a lobby.
     *
     * @param mes    the message indicating a player is searching for a lobby
     * @param sender the reference to the client who sent the message
     */
    void handle(FindLobbyMessage mes, ClientHandler sender);

    /**
     * Handles the message used by the first player to choose the size of the lobby.
     *
     * @param mes    the message containing details about the chosen number of players
     * @param sender the reference to the client who sent the message
     */
    void handle(NumberOfPlayersMessage mes, ClientHandler sender);

    /**
     * Handles the message when a player has to leave the lobby.
     *
     * @param mes    the message indicating a player is voluntarily disconnecting
     * @param sender the reference to the client who sent the message
     */
    void handle(ClientDisconnectedVoluntarilyMessage mes, ClientHandler sender);

    /**
     * Handles the message containing the name chosen by the player.
     *
     * @param mes    the message containing the chosen name
     * @param sender the reference to the client who sent the message
     */
    void handle(ChooseNameMessage mes, ClientHandler sender);

    /**
     * Handles the ping message sent by a connected client.
     *
     * @param ping   the ping message
     * @param sender the client who sent the ping
     */
    void handle(Ping ping, ClientHandler sender);

    /**
     * The listener is notified of a disconnection.
     *
     * @param clientHandler the client who was disconnected
     */
    void disconnectionHappened(ClientHandler clientHandler);

    /**
     * Handles the message containing the chosen color for the pawn.
     *
     * @param mes    the message containing the chosen color
     * @param sender the reference to the client who sent the message
     */
    void handle(ChosenColourMessage mes, ClientHandler sender);

    /**
     * Handles the chat message containing the text of a player's chat message and its recipients.
     *
     * @param chatMessage the message containing the chat text and recipients
     * @param sender      the client who sent the message
     */
    void handle(ChatMessage chatMessage, ClientHandler sender);
}
