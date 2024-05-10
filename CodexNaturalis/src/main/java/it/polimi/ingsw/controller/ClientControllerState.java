package it.polimi.ingsw.controller;

/**
 * Enum used to understand how the controller should behave on reception of  messages and commands
 */
public enum ClientControllerState {
    CHOOSING_NAME,
    CHOOSING_NUMBER_OF_PLAYERS,
    CHOOSING_OBJECTIVE,
    CHOOSING_STARTING_CARD_FACE,
    CONNECTING,
    ENDING_CONNECTION,
    GAME_ENDING,
    GAME_SOFT_LOCKED,
    INIT,
    OTHER_PLAYER_TURN,
    REQUESTING_DRAW_CARD,
    REQUESTING_PLACEMENT,
    WAITING_FOR_DRAW_CONFIRMATION,
    WAITING_FOR_NAME_CONFIRMATION,
    WAITING_FOR_PLACEMENT_CONFIRMATION,
    WAITING_FOR_START,

}
