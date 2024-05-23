package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

public class GenericMessage extends ServerToClientMessage {
    private String message;

    public GenericMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}
