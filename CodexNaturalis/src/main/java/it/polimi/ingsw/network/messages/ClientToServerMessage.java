package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.socket.server.ClientHandler;

public abstract class ClientToServerMessage extends Message{
    public abstract void execute(ServerSideMessageListener lis, ClientHandler sender);
}
