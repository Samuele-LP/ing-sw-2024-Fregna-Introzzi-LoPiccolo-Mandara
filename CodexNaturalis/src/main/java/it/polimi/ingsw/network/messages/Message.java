package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.MessageListener;
import it.polimi.ingsw.network.messages.clientToServer.StartGameMessage;

import java.io.Serializable;

/**
 * Message class, is used to define all messages exchanged between the client and the server
 * Implements Serializable as it will be handled by Socket
 */
public abstract class Message implements Serializable {

    /**
     * Method that passes the message to the recipient such that an adequate response message can be generated
     * @param lis is either the GameController or the ClientController
     */

    StartGameMessage mes = new StartGameMessage();
    public void execute(MessageListener lis){
        lis.handle(mes);
    }
}