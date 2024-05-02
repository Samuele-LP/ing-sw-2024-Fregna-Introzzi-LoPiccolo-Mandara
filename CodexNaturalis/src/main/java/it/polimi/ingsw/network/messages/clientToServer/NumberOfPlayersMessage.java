package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Message that contains information about how many players will play the game.
 * Checks on the validity of the number will be done by the client.
 */
public class NumberOfPlayersMessage extends ClientToServerMessage {
    private final int number;
    public NumberOfPlayersMessage(int num){
        number= num;
    }

    /**
     *
     * @return the number of players that will play the game
     */
    public int getNumber(){
        return number;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }
}
