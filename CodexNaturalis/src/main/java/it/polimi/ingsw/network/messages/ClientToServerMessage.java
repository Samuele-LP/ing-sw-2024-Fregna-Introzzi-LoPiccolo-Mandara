package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;
import it.polimi.ingsw.network.server.ClientHandlerSocket;

public abstract class ClientToServerMessage extends Message{
    public abstract void execute(ServerSideMessageListener lis, ClientHandlerSocket sender);
}
