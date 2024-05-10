package it.polimi.ingsw.network.messages.clientToServer;


import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Ping message sent by the client to the server
 */
public class Ping extends ClientToServerMessage {
    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {

    }
}
