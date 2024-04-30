package it.polimi.ingsw.view;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.*;

public class PlayerFieldView {
    private class SimpleCard{
        private final int x;
        private final int y;
        private final boolean isFacingUp;
        private final int ID;

        private SimpleCard(int x, int y, boolean isFacingUp, int id) {
            this.x = x;
            this.y = y;
            this.isFacingUp = isFacingUp;
            ID = id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isFacingUp() {
            return isFacingUp;
        }

        public int getID() {
            return ID;
        }
        @Override
        public boolean equals(Object obj){
            if(obj instanceof SimpleCard other){
                return this.ID==other.ID;
            }
            return super.equals(obj);
        }
        @Override
        public int hashCode(){
            return ID;
        }
    }

    private List<Point> availablePositions=null;
    private final List<SimpleCard> cards;
    private Map<TokenType,Integer> visibleSymbols;
    private int lowestX=0, lowestY=0;
    private int highestX=0, highestY=0;
    private final static String fontUp ="\u001B[31;48m";
    private final static String fontDown ="\u001B[32;48m";
    private final static String fontEnd ="\u001B[0m";
    public PlayerFieldView() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the player's playing field and resets the list of all available positions; it will be necessary to ask it again to the server
     * @param placeID ID of the placed card
     * @param placedX x position
     * @param placedY y position
     * @param isFacingUp side of the card to be shown
     */
    public void update(int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType,Integer> visibleSymbols) {
        highestX= Math.max(placedX, highestX);
        highestY= Math.max(placedY, highestY);
        lowestX= Math.min(placedX, lowestX);
        lowestY= Math.min(placedY, lowestY);
        cards.add(new SimpleCard(placedX,placedY,isFacingUp,placeID));
        this.visibleSymbols=new HashMap<>(visibleSymbols);
        availablePositions=null;
    }

    /**
     *
     * @param availablePositions the list of all positions available for the next move
     */
    public void updateAvailablePositions(List<Point> availablePositions){
        this.availablePositions=availablePositions;
    }
    public void printField(){
        System.out.println();
        System.out.println("↓Y↓||"+"Fungi:"+visibleSymbols.get(TokenType.fungi)+" Animal:"+visibleSymbols.get(TokenType.animal)+" Insect:"+visibleSymbols.get(TokenType.insect)+" Plant:"+visibleSymbols.get(TokenType.plant));
        for(int i=highestY+1;i>=lowestY-1;i--){
            printCoordinateNumbers(i);
            System.out.print("|");
            for(int j=lowestX-1;j<=highestX+1;j++){
                Optional<SimpleCard> temp= getCardAtPosition(j,i);
                if(temp.isPresent()){
                    String fontToUse=temp.get().isFacingUp()?fontUp:fontDown;
                    if(temp.get().getID()<10){
                        System.out.print(fontToUse+"00"+ fontEnd);
                    }else if(temp.get().getID()<100){
                        System.out.print(fontToUse+"0"+ fontEnd);
                    }
                    System.out.print(fontToUse+temp.get().getID()+ fontEnd);
                    System.out.print("|");
                }else{
                    System.out.print("???|");
                }
            }
            System.out.println();
        }
        System.out.print("X-→||");
        for(int i=lowestX-1;i<=highestX+1;i++){
            printCoordinateNumbers(i);
        }
        System.out.println();
        System.out.println("   ||Ink:"+visibleSymbols.get(TokenType.ink)+" Quill:"+visibleSymbols.get(TokenType.quill)+" Scroll:"+visibleSymbols.get(TokenType.scroll));
    }

    private void printCoordinateNumbers(int i) {
        if(i>=-9&&i<0){
            System.out.print("-0");
            System.out.print(Math.abs(i)+"|");
        } else if (i>=0&&i<10){
            System.out.print("+0");
            System.out.print(i+"|");
        } else {
            System.out.print("+"+i+"|");
        }
    }

    private Optional<SimpleCard> getCardAtPosition(int x, int y){
        return cards.stream().filter(card->card.getX()==x&&card.getY()==y).findFirst();
    }
}
