package it.polimi.ingsw.network.messages.clientToServer;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

/**
 * Message used by a temporarily disconnected Client (maybe due to a connection error) to try to reconnect to a game
 * before it automatically ends.
 * Note: the subsequent message sent by Server is ClientFieldCheckValidityMessage, used in order to check if the
 * Client has the expected field (so that no cheating through this mechanic can be done)
 */

public class ClientTryReconnectionMessage extends ClientToServerMessage {

    private final String name;

    public ClientTryReconnectionMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute(ServerSideMessageListener lis, ClientHandler sender) {
        lis.handle(this, sender);
    }


}
