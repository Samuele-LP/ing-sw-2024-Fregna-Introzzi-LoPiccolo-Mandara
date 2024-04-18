package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message that can be sent only during the final phase, sent if a player tries to draw a visible card from a spot that has no visible crd on it
 */
public class EmptyDrawnCardPositionMessage extends Message {
}
