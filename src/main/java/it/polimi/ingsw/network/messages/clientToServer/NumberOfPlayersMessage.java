package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

/**
 * Message that contains information about how many players will play the game.
 * Checks on the validity of the number will be done by the client.
 */
public class NumberOfPlayersMessage extends ClientToServerMessage {

    private final int number;

    /**
     * Constructor for the NumberOfPlayersMessage.
     *
     * @param num the number of players that will play the game
     */
    public NumberOfPlayersMessage(int num){
        number = num;
    }

    /**
     * Returns the number of players that will play the game.
     *
     * @return the number of players
     */
    public int getNumber(){
        return number;
    }

    /**
     * Executes the message using the given listener and sender.
     *
     * @param lis    the listener to handle the message
     * @param sender the client handler that sent the message
     */
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandlerSocket sender) {
        lis.handle(this, sender);
    }
}
