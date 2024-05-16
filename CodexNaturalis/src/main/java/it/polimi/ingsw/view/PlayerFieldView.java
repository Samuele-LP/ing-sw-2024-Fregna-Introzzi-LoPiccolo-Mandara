package it.polimi.ingsw.view;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.TokenType;

import java.io.IOException;
import java.util.*;

/**
 * Contains information about a layer's field and provides methods to show it to the user
 */
public class PlayerFieldView {
    /**
     * Private class to memorize how a card has been placed
     */
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
    private final List<SimpleCard> simpleCards;
    private Map<TokenType,Integer> visibleSymbols= new HashMap<>();
    private int lowestX=0, lowestY=0;
    private int highestX=0, highestY=0;
    public PlayerFieldView() throws IOException {
        this.simpleCards = new ArrayList<>();
    }

    /**
     * Adds a card to the player's playing field and resets the list of all available positions; it will be necessary to ask it again to the server
     * @param placeID ID of the placed card
     * @param placedX x position
     * @param placedY y position
     * @param isFacingUp side of the card to be shown
     */
    public void updateField(int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType,Integer> visibleSymbols) {
        highestX= Math.max(placedX, highestX);
        highestY= Math.max(placedY, highestY);
        lowestX= Math.min(placedX, lowestX);
        lowestY= Math.min(placedY, lowestY);
        simpleCards.add(new SimpleCard(placedX,placedY,isFacingUp,placeID));
        this.visibleSymbols=new HashMap<>(visibleSymbols);
        availablePositions=null;
    }

    /**
     * Updates the lis of available positions.
     * The list is set to null if it hasn't been updated after a call to updateField
     * @param availablePositions the list of all positions available for the next move
     */
    public void updateAvailablePositions(List<Point> availablePositions){
        this.availablePositions=availablePositions;
    }

    /**
     * Prints the field for the CLI
     */
    public ArrayList<String> printField(){
        ArrayList<String> lines = new ArrayList<>();
        lines.add("   ||" +
                "\u001B[32m\u001B[49m Plant:\u001B[0m"+visibleSymbols.get(TokenType.plant)+
                "\u001B[35m\u001B[49m Insect:\u001B[0m"+visibleSymbols.get(TokenType.insect)+
                "\u001B[34m\u001B[49m Animal:\u001B[0m" +visibleSymbols.get(TokenType.animal)+
                "\u001B[31m\u001B[49m Fungi:\u001B[0m"+visibleSymbols.get(TokenType.fungi));
        for(int i=highestY+1;i>=lowestY-1;i--){
            lines.add(printHorizontalSeparator());
            lines.add("   ||"+printRow(0,i));
            lines.add(printYCoordinateNumbers(i)+ "|"+printRow(1,i));
            lines.add("   ||"+printRow(2,i));
        }
        lines.add(printHorizontalSeparator());
        lines.add(printXCoordinateNumbers());
        lines.add(printHorizontalSeparator());
        lines.add("   ||Ink:"+visibleSymbols.get(TokenType.ink)+" Quill:"+visibleSymbols.get(TokenType.quill)+" Scroll:"+visibleSymbols.get(TokenType.scroll));
        if(!(availablePositions==null)&&!availablePositions.isEmpty()) {
            int nextLine=0;
            StringBuilder pos= new StringBuilder();
            lines.add("There are these following available positions for you to place a card in:");
            for(Point p :availablePositions){
                pos.append(p.toString());
            }
            lines.add(pos.toString());
        }
        return lines;
    }

    /**
     * Print the x coordinate numbers under the playing field
     */
    private String printXCoordinateNumbers() {
        StringBuilder coords= new StringBuilder("X→→||");
        for(int i = lowestX -1; i<=highestX+1;i++){
            coords.append("   ").append(printNumberAsThreeCharacters(i)).append("   |");
        }
        return coords.toString();
    }

    /**
     * Each field position is divided in 3 rows, this method is executed three times using values of asciiRow from 0 to 2
     * @param asciiRow refers to the position in the array returned by the methods asciiArt of the cards
     * @param i refers to the y position on the field
     */
    private String printRow(int asciiRow, int i) {
        StringBuilder row= new StringBuilder();
        for(int j=lowestX-1;j<=highestX+1;j++){
            Optional<SimpleCard> temp= getCardAtPosition(j,i);
            if(temp.isPresent()&&temp.get().getID()<=86){
                String[] asciiArt;
                if(temp.get().isFacingUp()) {
                    asciiArt = GameView.printCardAsciiFront(temp.get().getID() );
                }else {
                    asciiArt = GameView.printCardAsciiBack(temp.get().getID() );
                }
                row.append("\u001B[30;47m").append(asciiArt[asciiRow]).append("\u001B[0m").append("|");
            } else{
                row.append("         |");//9 spaces
            }
        }
        return row.toString();
    }

    /**
     * Prints the y coordinate indicator
     * @param y is the y coordinate
     */
    private String printYCoordinateNumbers(int y) {
        return printNumberAsThreeCharacters(y)+"|";
    }

    /**
     * Prints a separator as long as the field
     */
    private String printHorizontalSeparator(){
        return "   ||" + "---------|".repeat(Math.max(0, highestX + 1 - (lowestX - 1) + 1));
    }

    /**
     * Prints the number as exactly three characters so that they can be centered correctly
     * @param n is the number to be printed
     */
    private String printNumberAsThreeCharacters(int n) {
        String num;
        if(n>=-9&&n<0){
            num="-0"+Math.abs(n);
        } else if (n>=0&&n<10){
            num="+0"+n;
        }else if(n<=-10){
            num = Integer.toString(n);
        }
        else {
            num="+"+n;
        }
        return num;
    }

    /**
     * From the cards memorized as SimpleCards it returns an Optional containing information on whether a card is present
     * @param x is the x position of the card
     * @param y is the y position of the cards
     * @return an Optional containing null if the card isn't present, an Optional containing the reference to the SimpleCard otherwise
     */
    private Optional<SimpleCard> getCardAtPosition(int x, int y){
        return simpleCards.stream().filter(card->card.getX()==x&&card.getY()==y).findFirst();
    }

}
