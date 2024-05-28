package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

public class ReceivedChatMessage extends ServerToClientMessage {
    private final String body;

    public ReceivedChatMessage(String sender, String body, boolean isGlobal) {
        if(isGlobal){
            this.body= sender+" sent a message to everyone:<   "+body+"   >";
        }else {
            this.body= sender+" whispered:<   "+body+"   >";
        }
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }

    public String getBody() {
        return body;
    }
}
