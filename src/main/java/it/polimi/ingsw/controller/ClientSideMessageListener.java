package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Pong;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.serverToClient.*;

import java.rmi.Remote;

/**
 * ClientSideMessageListener interface defines methods to handle all possible messages coming from the server.
 * Each method corresponds to a specific type of message that can be received by the client.
 */
public interface ClientSideMessageListener extends Remote {

    /**
     * Called when an unsupported ServerToClientMessage is received.
     *
     * @param m the unsupported message received from the server.
     */
    void handle(ServerToClientMessage m);

    /**
     * Handles AvailablePositionsMessage to show available positions in the CLI/GUI.
     *
     * @param m the message containing available positions.
     */
    void handle(AvailablePositionsMessage m);

    /**
     * Handles ChooseHowManyPlayersMessage which gives permission to choose the number of players.
     * The response to this message should be a NumberOfPlayersMessage.
     *
     * @param m the message asking for the number of players.
     */
    void handle(ChooseHowManyPlayersMessage m);

    /**
     * Handles ClientCantStartGameMessage which is received when a NumberOfPlayersMessage is sent without permission.
     *
     * @param m the message indicating the client can't start the game.
     */
    void handle(ClientCantStartGameMessage m);

    /**
     * Handles EmptyDeckMessage which is received if the player tries to draw from an empty deck.
     * The response to this message should be another DrawCardMessage.
     *
     * @param m the message indicating the deck is empty.
     */
    void handle(EmptyDeckMessage m);

    /**
     * Handles EmptyDrawnCardPositionMessage which is received if the player tries to draw from an empty visible card position.
     * The response to this message should be another DrawCardMessage.
     *
     * @param m the message indicating the drawn card position is empty.
     */
    void handle(EmptyDrawnCardPositionMessage m);

    /**
     * Handles EndPlayerTurnMessage which is received when a player has correctly drawn a card.
     * The controller should now block most messages the client is trying to send.
     *
     * @param m the message indicating the end of the player's turn.
     */
    void handle(EndPlayerTurnMessage m);

    //TODO: modify when implementing multiple games FA
    /**
     * Handles GameAlreadyStartedMessage which is received when the client tries to connect to a lobby that has already started.
     *
     * @param m the message indicating the game has already started.
     */
    void handle(GameAlreadyStartedMessage m);

    /**
     * Handles GameEndingAfterDisconnectionMessage which is received when the game ends after a disconnection.
     * The client should show the winner(s) and explain why the game has ended.
     *
     * @param m the message indicating the game ended due to a disconnection.
     */
    void handle(GameEndingAfterDisconnectionMessage m);

    /**
     * Handles GameEndingMessage which is received when the game has ended according to the rules.
     * The client should show the winner(s).
     *
     * @param m the message indicating the game has ended.
     */
    void handle(GameEndingMessage m);

    /**
     * Handles GameStartingMessage which is received when the correct number of players has been reached.
     * The client should now choose how to place their starting card.
     *
     * @param m the message indicating the game is starting.
     */
    void handle(GameStartingMessage m);

    /**
     * Handles IllegalPlacementPositionMessage which is received when a player tries to place a card in an unavailable position.
     * The expected response is a PlaceCardMessage or an AvailablePositionsMessage.
     *
     * @param m the message indicating the placement position is illegal.
     */
    void handle(IllegalPlacementPositionMessage m);

    /**
     * Handles LobbyFoundMessage which is received when the client is successfully connected to a game lobby.
     *
     * @param m the message indicating the lobby has been found.
     */
    void handle(LobbyFoundMessage m);

    //TODO: modify when implementing multiple games FA
    /**
     * Handles LobbyFullMessage which is received when the client tries to connect to a full lobby.
     *
     * @param m the message indicating the lobby is full.
     */
    void handle(LobbyFullMessage m);

    /**
     * Handles NameChosenSuccessfullyMessage which is received when the client successfully chooses a name.
     * The client should save the chosen name and wait for the next message.
     *
     * @param m the message indicating the name was chosen successfully.
     */
    void handle(NameChosenSuccessfullyMessage m);

    /**
     * Handles NameNotAvailableMessage which is received when the chosen name is not available.
     * The client should change the name and send a new ChooseNameMessage.
     *
     * @param m the message indicating the name is not available.
     */
    void handle(NameNotAvailableMessage m);

    /**
     * Handles NotEnoughResourcesMessage which is received when a player tries to place a gold card facing up without enough resources.
     * The expected response is a PlaceCardMessage or an AvailablePositionsMessage.
     *
     * @param m the message indicating there are not enough resources to place the card.
     */
    void handle(NotEnoughResourcesMessage m);

    /**
     * Handles OtherPlayerTurnUpdateMessage which is received when another player makes a move.
     * The client should update the view with the new information. No response is expected.
     *
     * @param m the message indicating another player's move.
     */
    void handle(OtherPlayerTurnUpdateMessage m);

    /**
     * Handles PlayerCantPlayAnymoreMessage which is received when the client should skip their turn.
     * No action is expected in response.
     *
     * @param m the message indicating the player can't play anymore.
     */
    void handle(PlayerCantPlayAnymoreMessage m);

    /**
     * Handles SecretObjectiveChoiceMessage which is received when the player should choose a secret objective.
     * The expected response is a ChosenSecretObjectiveMessage.
     *
     * @param m the message indicating the player should choose a secret objective.
     */
    void handle(SecretObjectiveChoiceMessage m);

    /**
     * Handles SendDrawncardMessage which is received when the player has drawn a card.
     * The client should update the current hand and shared field information.
     *
     * @param m the message indicating the drawn card.
     */
    void handle(SendDrawncardMessage m);

    //TODO: choose to keep or remove this according to the multiple games FA
    /**
     * Handles ServerCantStartGameMessage.
     */
    void handle(ServerCantStartGameMessage m);

    /**
     * Handles SharedFieldUpdateMessage which is received when the shared field has changed after another player draws a card.
     * No response is expected.
     *
     * @param m the message indicating the shared field update.
     */
    void handle(SharedFieldUpdateMessage m);

    /**
     * Handles StartPlayerTurnMessage which is received when the player's turn has started.
     * The expected response is either a PlaceCardMessage or a RequestAvailablePositionsMessage.
     *
     * @param m the message indicating the start of the player's turn.
     */
    void handle(StartPlayerTurnMessage m);

    /**
     * Handles SuccessfulPlacementMessage which is received when the player successfully places a card.
     * The expected response is a DrawCardMessage.
     *
     * @param m the message indicating the successful placement of a card.
     */
    void handle(SuccessfulPlacementMessage m);

    /**
     * Handles GenericMessage which contains information of interest to the player.
     *
     * @param m the message containing generic information.
     */
    void handle(GenericMessage m);

    /**
     * Handles Pong message which is the response to a Ping sent by the server.
     *
     * @param pong the Pong message received from the server.
     */
    void handle(Pong pong);

    /**
     * Handles ChooseColourMessage which is received when the player should choose a colour.
     *
     * @param m the message indicating the player should choose a colour.
     */
    void handle(ChooseColourMessage m);

    /**
     * Handles ColourAlreadyChosenMessage which is received when the chosen colour is not available.
     *
     * @param m the message indicating the colour is already chosen.
     */
    void handle(ColourAlreadyChosenMessage m);

    /**
     * Handles NotAColourMessage which is received when the message did not contain a valid colour.
     *
     * @param m the message indicating the colour is invalid.
     */
    void handle(NotAColourMessage m);

    /**
     * Handles InitialPhaseDisconnectionMessage which is received when someone is disconnected during the initial phase.
     * The connection to the server is terminated.
     *
     * @param m the message indicating a disconnection during the initial phase.
     */
    void handle(InitialPhaseDisconnectionMessage m);

    /**
     * Handles ReceivedChatMessage which is received when a chat message is received.
     * The message can be either a private chat message or a global chat message.
     *
     * @param m the chat message received from the server.
     */
    void handle(ReceivedChatMessage m);

    /**
     * Notifies the listener of a disconnection. This method implements the necessary procedures to handle a disconnection.
     */
    void disconnectionHappened();

    /**
     * Notifies that the connection to the server could not be established.
     */
    void couldNotConnect();
}