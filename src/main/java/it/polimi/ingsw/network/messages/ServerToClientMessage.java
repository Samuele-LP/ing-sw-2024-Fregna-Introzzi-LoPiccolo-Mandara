package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.ClientSideMessageListener;

public abstract class ServerToClientMessage extends Message{
    public abstract void execute(ClientSideMessageListener lis);
}
