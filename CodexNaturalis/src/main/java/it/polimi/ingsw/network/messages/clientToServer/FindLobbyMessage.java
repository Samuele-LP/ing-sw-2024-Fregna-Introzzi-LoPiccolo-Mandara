package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;

public class FindLobbyMessage extends ClientToServerMessage {

    @Override
    public void execute(ServerSideMessageListener lis) {
        lis.handle(this);
    }
}
