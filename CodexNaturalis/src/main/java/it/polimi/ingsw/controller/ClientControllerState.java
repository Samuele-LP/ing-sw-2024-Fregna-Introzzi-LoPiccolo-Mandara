package it.polimi.ingsw.controller;

/**
 * Enum used to understand how the controller should behave on reception of  messages and commands
 */
public enum ClientControllerState {
    CHOOSING_NAME(false),
    CHOOSING_NUMBER_OF_PLAYERS(false),
    CONNECTING(false),
    ENDING_CONNECTION(false),
    INIT(false),
    WAITING_FOR_NAME_CONFIRMATION(false),
    WAITING_FOR_START(false),
    DISCONNECTED(false),
    CHOOSING_STARTING_CARD_FACE(true),
    CHOOSING_OBJECTIVE(true),
    GAME_ENDING(true),
    GAME_SOFT_LOCKED(true),
    OTHER_PLAYER_TURN(true),
    REQUESTING_DRAW_CARD(true),
    REQUESTING_PLACEMENT(true),
    WAITING_FOR_DRAW_CONFIRMATION(true),
    WAITING_FOR_PLACEMENT_CONFIRMATION(true),
    INITIAL_PHASE(true),
    CHOOSING_COLOUR(true)
    ;
    final boolean gameOngoing;
    ClientControllerState(boolean b) {
        gameOngoing=b;
    }
}
