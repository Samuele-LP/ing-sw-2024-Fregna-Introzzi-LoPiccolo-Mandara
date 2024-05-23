package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.Point;

public class PlaceCardCommand extends UserCommand{
    private final int xPosition;
    private final int yPosition;
    private final int cardID;
    private final boolean isFacingUP;

    public PlaceCardCommand(int xPosition,int yPosition,boolean isFacingUP,int cardID){
        this.cardID=cardID;
        this.isFacingUP=isFacingUP;
        this.xPosition=xPosition;
        this.yPosition=yPosition;
    }
    public int getXPosition() {
        return xPosition;
    }
    public int getYPosition() {
        return yPosition;
    }
    public int getCardID(){
        return cardID;
    }
    public boolean isFacingUP(){
        return isFacingUP;
    }
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
