package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message that signals a player's disconnection from the game (voluntarily)
 */
public class ClientDisconnectedVoluntarily extends Message{
    private boolean disconnectedStatus = false;

    public clientDisconnected{ disconnectedStatus = true; }

    /**
     * @return wheter the client is disconnected or not
     * NB: Subsequently the whole game will end
     */
    public getClientDisconnectedStatus(){ return disconnectedStatus; }
}
