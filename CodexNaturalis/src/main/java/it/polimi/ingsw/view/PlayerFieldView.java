package it.polimi.ingsw.view;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Card;
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
    private final List<Card> cardList;
    private List<Point> availablePositions=null;
    private final List<SimpleCard> cards;
    private Map<TokenType,Integer> visibleSymbols;
    private int lowestX=0, lowestY=0;
    private int highestX=0, highestY=0;
    private List<Integer> playerHand;
    public PlayerFieldView() throws IOException {
        this.cards = new ArrayList<>();
        cardList= Creation.getResourceCards();
        cardList.addAll(Creation.getGoldCards());
        cardList.addAll(Creation.getStartingCards());
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
        cards.add(new SimpleCard(placedX,placedY,isFacingUp,placeID));
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
    public void printField(){
        System.out.println("   ||" +
                "\u001B[33m\u001B[48;2;255;255;255m Plant:\u001B[0m"+visibleSymbols.get(TokenType.plant)+
                "\u001B[35m\u001B[48;2;255;255;255m Insect:\u001B[0m"+visibleSymbols.get(TokenType.insect)+
                "\u001B[34m\u001B[48;2;255;255;255m Animal:\u001B[0m" +visibleSymbols.get(TokenType.animal)+
                "\u001B[31m\u001B[48;2;255;255;255m Fungi:\u001B[0m"+visibleSymbols.get(TokenType.fungi));
        for(int i=highestY+1;i>=lowestY-1;i--){
            printHorizontalSeparator();
            System.out.print("   ||");
            printRow(0,i);
            printYCoordinateNumbers(i);
            System.out.print("|");
            printRow(1,i);
            System.out.print("   ||");
            printRow(2,i);
        }
        printHorizontalSeparator();
        printXCoordinateNumbers();
        System.out.println();
        System.out.println("   ||Ink:"+visibleSymbols.get(TokenType.ink)+" Quill:"+visibleSymbols.get(TokenType.quill)+" Scroll:"+visibleSymbols.get(TokenType.scroll));
        printHorizontalSeparator();
        if(!availablePositions.isEmpty()) {
            int nextLine=0;
            System.out.println("There are these following available positions for you to place a card in:");
            for(Point p :availablePositions){
                nextLine++;
                System.out.print(p+ " ");
                if(nextLine%10==0){
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    /**
     * Print the x coordinate numbers under the playing field
     */
    private void printXCoordinateNumbers() {
        System.out.print("X→→||");
        for(int i = lowestX -1; i<=highestX+1;i++){
            System.out.print("   ");
            printNumberAsThreeCharacters(i);
            System.out.print("   |");
        }
    }

    /**
     * Each field position is divided in 3 rows, this method is executed three times using values of asciiRow from 0 to 2
     * @param asciiRow refers to the position in the array returned by the methods asciiArt of the cards
     * @param i refers to the y position on the field
     */
    private void printRow(int asciiRow, int i) {
        for(int j=lowestX-1;j<=highestX+1;j++){
            Optional<SimpleCard> temp= getCardAtPosition(j,i);
            if(temp.isPresent()){
                String[] asciiArt;
                if(temp.get().isFacingUp()) {
                    asciiArt = ((PlayableCard) cardList.get(temp.get().getID() - 1)).asciiArtFront();
                }else {
                    asciiArt = ((PlayableCard) cardList.get(temp.get().getID() - 1)).asciiArtBack();
                }
                System.out.print(  "\u001B[30;47m" + asciiArt[asciiRow]+  "\u001B[0m" + "|");
            } else{
                System.out.print("         |");//9 spaces
            }
        }
        System.out.println();
    }

    /**
     * Prints the y coordinate indicator
     * @param y is the y coordinate
     */
    private void printYCoordinateNumbers(int y) {
        printNumberAsThreeCharacters(y);
        System.out.print("|");
    }

    /**
     * Prints a separator as long as the field
     */
    private void printHorizontalSeparator(){
        System.out.print("   ||");
        for(int j=lowestX-1;j<=highestX+1;j++){
            System.out.print("---------|");//9 symbols
        }
        System.out.println();
    }

    /**
     * Prints the number as exactly three characters so that they can be centered correctly
     * @param n is the number to be printed
     */
    private void printNumberAsThreeCharacters(int n) {
        if(n>=-9&&n<0){
            System.out.print("-0");
            System.out.print(Math.abs(n));
        } else if (n>=0&&n<10){
            System.out.print("+0");
            System.out.print(n);
        }else if(n<=-10){
            System.out.print(n);
        }
        else {
            System.out.print("+"+n);
        }
    }

    /**
     * From the cards memorized as SimpleCards it returns an Optional containing information on whether a card is present
     * @param x is the x position of the card
     * @param y is the y position of the cards
     * @return an Optional containing null if the card isn't present, an Optional containing the reference to the SimpleCard otherwise
     */
    private Optional<SimpleCard> getCardAtPosition(int x, int y){
        return cards.stream().filter(card->card.getX()==x&&card.getY()==y).findFirst();
    }

    /**
     * Updates the list of the cards in the hand of a player
     */
    public void updateHand(ArrayList<Integer> playerHand) {
        this.playerHand=playerHand;
    }
    /**
     * Updates the list of the cards in the hand of a player after a placement
     */
    public void updateHand(int lastPlayed) {
        this.playerHand.remove(lastPlayed);
    }

    /**
     * Returns the hand of the player. Used to check if the card they want to place is in their possession
     */
    public List<Integer> getPlayerHand(){
        return playerHand;
    }
    /**
     * Prints the hand of the player
     */
    public void printHand() {
        System.out.println("You have these following cards in your hand:\n");
        for(Integer i: playerHand){
            PlayableCard card = (PlayableCard) cardList.get(i-1);
            String[] asciiFront= card.asciiArtFront();
            String[] asciiBack= card.asciiArtBack();
            System.out.println("ID: "+i);
            System.out.println("|"+asciiFront[0]+"|     |"+asciiBack[0]+"|");
            System.out.println("|"+asciiFront[1]+"|     |"+asciiBack[1]+"|");
            System.out.println("|"+asciiFront[2]+"|     |"+asciiBack[2]+"|\n");
        }
    }

}
