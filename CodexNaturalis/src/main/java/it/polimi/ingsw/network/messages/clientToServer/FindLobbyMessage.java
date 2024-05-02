package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

public class FindLobbyMessage extends ClientToServerMessage {

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }
}
