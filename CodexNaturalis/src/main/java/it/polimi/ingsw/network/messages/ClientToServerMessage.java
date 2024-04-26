package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ServerSideMessageListener;

public abstract class ClientToServerMessage extends Message{
    public abstract void execute(ServerSideMessageListener lis);
}
