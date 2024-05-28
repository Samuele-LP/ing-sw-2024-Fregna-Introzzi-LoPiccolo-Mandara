package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Pong;
import it.polimi.ingsw.network.messages.ServerToClientMessage;
import it.polimi.ingsw.network.messages.serverToClient.*;

/**
 * Adds methods to handle all possible messages coming from the server
 */
public interface ClientSideMessageListener {

    /**
     * Is called when an unsupported ServerToClientMessage is received
     */
    void handle(ServerToClientMessage m);

    /**
     * When an availablePosition message is received the client should be shown in the CLI/GUI which position they are
     */
    void handle(AvailablePositionsMessage m);

    /**
     * This message gives permission to choose how many players will participate in a game.
     * The answer to this message should only be a NumberOfPlayersMessage
     */
    void handle(ChooseHowManyPlayersMessage m);

    /**
     * This is received when a NumberOfPlayersMessage has been sent when the client didn't have permission
     */
    void handle(ClientCantStartGameMessage m);
    /**
     * Message received if the player tries to draw from an empty deck.
     * The answer to this message should be another DrawCardMessage
     */
    void handle(EmptyDeckMessage m);

    /**
     * Message received if the player tries to draw from an empty visible card position.
     * The answer to this message should be another DrawCardMessage
     */
    void handle(EmptyDrawnCardPositionMessage m);

    /**
     * Message received when a player has correctly drawn a card.
     * The controller should now block most of the messages the client is trying to send
     */
    void handle(EndPlayerTurnMessage m);

    //TODO: modify when implementing multiple games FA
    /**
     * The client has tried to connect to a lobby that had already started.
     */
    void handle(GameAlreadyStartedMessage m);

    /**
     * When a game is ending after a disconnection the client should show the winner/s and explain why the game has ended
     */
    void handle(GameEndingAfterDisconnectionMessage m);

    /**
     * When the game has ended according to the rules the winners should be shown
     */
    void handle(GameEndingMessage m);

    /**
     * The correct  number of players has been reached, the client should now choose how to place their starting card
     */
    void handle(GameStartingMessage m);

    /**
     * The player has tried to place in an unavailable position, a PlaceCardMessage or an AvailablePositionsMessage
     * is the expected response to this message
     */
    void handle(IllegalPlacementPositionMessage m);

    /**
     * The client has been notified that they are now successfully connected to a game lobby
     */
    void handle(LobbyFoundMessage m);

    //TODO: modify when implementing multiple games FA
    /**
     * The client has tried to connect to a lobby that is already full.
     */
    void handle(LobbyFullMessage m);

    /**
     * The client should now save the name they have chosen and wait for the next message
     */
    void handle(NameChosenSuccessfullyMessage m);

    /**
     * The client should change name and send a new ChooseNameMessage
     */
    void handle(NameNotAvailableMessage m);

    /**
     * Message received after a player tried to place a gold card facing up without enough resources.
     * a PlaceCardMessage or an AvailablePositionsMessage is the expected response to this message
     */
    void handle(NotEnoughResourcesMessage m);

    /**
     * The client has just received information about another player's placement and should now update the view.
     * No message should be sent in response
     */
    void handle(OtherPlayerTurnUpdateMessage m);

    /**
     * Receiving this message makes the client skip their turn.
     * The expected response should be no action by the client
     */
    void handle(PlayerCantPlayAnymoreMessage m);

    /**
     * The only expected response to this message should be a ChosenSecretObjectiveMessage
     */
    void handle(SecretObjectiveChoiceMessage m);

    /**
     * The client should update the current hand shown and the shared field information
     */
    void handle(SendDrawncardMessage m);

    //TODO: choose to keep or remove this according to the multiple games FA
    /**
     *
     */
    void handle(ServerCantStartGameMessage m);

    /**
     * The client has received information about how the shared field has changed after another player has drawn a card.
     * There is no expected response to this message.
     */
    void handle(SharedFieldUpdateMessage m);

    /**
     * The client is notified that their turn has started so the expected response is
     * either a PlaceCarMessage or a RequestAvailablePositionsMessage
     */
    void handle(StartPlayerTurnMessage m);

    /**
     * The client updates the view according to the placement, the expected response is a DrawCardMessage
     */
    void handle(SuccessfulPlacementMessage m);

    /**
     * @param m is a message that contains information of interest to the player.
     */
    void handle(GenericMessage m);

    /**
     * @param pong is the response to a Ping sent by the server
     */
    void handle(Pong pong);

    /**
     * The listener is notified that the next move by the player should be the choice of the colour
     */
    void handle(ChooseColourMessage m);

    /**
     * Response to a ChosenColourMessage if the colour was not available
     */
    void handle(ColourAlreadyChosenMessage m);
    /**
     * Response to a ChosenColourMessage if the message did not contain a valid colour
     */
    void handle(NotAColourMessage m);
    void handle(InitialPhaseDisconnectionMessage m);
    void handle(ReceivedChatMessage m);
    /**
     * The listener is notified of a disconnection. This method implements the necessary procedures to handle a disconnection
     */
    void disconnectionHappened();
}