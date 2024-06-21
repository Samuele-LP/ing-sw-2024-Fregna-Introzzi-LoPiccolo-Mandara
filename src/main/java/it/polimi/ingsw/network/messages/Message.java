package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * The Message class serves as a base class for all messages exchanged between the client and the server.
 * It implements the Serializable interface to allow message objects to be transmitted over a network using sockets.
 */
public abstract class Message implements Serializable {
}