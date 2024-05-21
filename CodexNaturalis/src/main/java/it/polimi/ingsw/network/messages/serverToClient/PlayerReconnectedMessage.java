package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

public class PlayerReconnectedMessage extends ServerToClientMessage {

    private final SharedFieldUpdateMessage sharedField;
    private final List<Integer> playerHand;
    private final SimpleField ownerField;
    private List<SimpleField> opponentFields;


    public PlayerReconnectedMessage(SharedFieldUpdateMessage sharedField, List<Integer> playerHand, SimpleField ownerField, List<SimpleField> opponentFields) {
        this.sharedField = sharedField;
        this.playerHand = playerHand;
        this.ownerField = ownerField;
        this.opponentFields = opponentFields;
    }


    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}

