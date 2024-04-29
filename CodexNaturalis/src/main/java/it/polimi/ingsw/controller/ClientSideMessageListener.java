package it.polimi.ingsw.controller;

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
    void handle(AvailablePositionsMessage m);
    void handle(ChooseHowManyPlayersMessage m);
    void handle(ClientCantStartGameMessage m);
    void handle(ClientFieldCheckValidityMessage m);
    void handle(EmptyDeckMessage m);
    void handle(EmptyDrawnCardPositionMessage m);
    void handle(EndPlayerTurnMessage m);
    void handle(GameAlreadyStartedMessage m);
    void handle(GameEndingAfterDisconnectionMessage m);
    void handle(GameEndingMessage m);
    void handle(GameStartingMessage m);
    void handle(IllegalPlacementPositionMessage m);
    void handle(LobbyFoundMessage m);
    void handle(LobbyFullMessage m);
    void handle(NameChosenSuccessfullyMessage m);
    void handle(NameNotAvailableMessage m);
    void handle(NotEnoughResourcesMessage m);
    void handle(OtherPlayerTurnUpdateMessage m);
    void handle(PlayerCantPlayAnymoreMessage m);
    void handle(SecretObjectiveChoiceMessage m);
    void handle(SendDrawncardMessage m);
    void handle(ServerCantStartGameMessage m);
    void handle(SharedFieldUpdateMessage m);
    void handle(StartPlayerTurnMessage m);
    void handle(SuccessfulPlacementMessage m);
}