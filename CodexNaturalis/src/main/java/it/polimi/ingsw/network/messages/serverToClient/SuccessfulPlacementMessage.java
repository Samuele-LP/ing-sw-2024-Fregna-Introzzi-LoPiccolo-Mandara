package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message sent to the client after a valid move has been made
 * This message also serves to inform the player that they now have
 * to draw a card
 */
public class SuccessfulPlacementMessage extends Message{
    /*
    TODO: DECIDE HOW TO UPDATE THE VIEW OF THE PLAYER'S FIELD AFTER A MOVE
           IDEA: Make this message contain another message that is used
                 to give a player information on a playing field that
                 isn't theirs
     */
}
