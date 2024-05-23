package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.List;

public class PlayerReconnectedMessage extends ServerToClientMessage {

    private final SharedFieldUpdateMessage sharedField;
    private final List<Integer> playerHand;
    private final SimpleField ownerField;
    private final List<SimpleField> opponentFields;
    private final int firstCommonObjective;
    private final int secondCommonObjective;


    public PlayerReconnectedMessage(SharedFieldUpdateMessage sharedField, List<Integer> playerHand, SimpleField ownerField, List<SimpleField> opponentFields, int firstCommonObjective, int secondCommonObjective) {
        this.sharedField = sharedField;
        this.playerHand = playerHand;
        this.ownerField = ownerField;
        this.opponentFields = opponentFields;
        this.firstCommonObjective = firstCommonObjective;
        this.secondCommonObjective = secondCommonObjective;
    }


    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
    public SharedFieldUpdateMessage getSharedField() {
        return sharedField;
    }
    public List<Integer> getPlayerHand() {
        return playerHand;
    }
    public List<SimpleField> getOpponentFields() {
        return opponentFields;
    }
    public SimpleField getOwnerField() {
        return ownerField;
    }
    public int getSecondCommonObjective() {
        return secondCommonObjective;
    }
    public int getFirstCommonObjective() {
        return firstCommonObjective;
    }
}

