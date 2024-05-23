package it.polimi.ingsw.view.Field;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.view.GameViewCli;

import java.util.ArrayList;
import java.util.Optional;

public class PlayerFieldViewCli extends PlayerFieldView {
    public PlayerFieldViewCli(){
        super();
    }
    public PlayerFieldViewCli(SimpleField simpleField) {
        super(simpleField.getCards(),simpleField.getSymbols());
    }

    /**
     * Prints the field
     */
    @Override
    public ArrayList<String> printField() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("   ||" +
                ConstantValues.ansiGreen +"Plant:"+ConstantValues.ansiEnd +visibleSymbols.getOrDefault(TokenType.plant,0)+
                "\u001B[35m\u001B[49m Insect:"+ConstantValues.ansiEnd +visibleSymbols.getOrDefault(TokenType.insect,0)+
                ConstantValues.ansiBlue +" Animal:"+ConstantValues.ansiEnd +visibleSymbols.getOrDefault(TokenType.animal,0)+
                ConstantValues.ansiRed +" Fungi:"+ConstantValues.ansiEnd +visibleSymbols.getOrDefault(TokenType.fungi,0));
        for(int i=highestY+1;i>=lowestY-1;i--){
            lines.add(printHorizontalSeparator());
            lines.add("   ||"+printRow(0,i));
            lines.add(printYCoordinateNumbers(i)+ "|"+printRow(1,i));
            lines.add("   ||"+printRow(2,i));
        }
        lines.add(printHorizontalSeparator());
        lines.add(printXCoordinateNumbers());
        lines.add(printHorizontalSeparator());
        lines.add("   ||Ink:"+visibleSymbols.getOrDefault(TokenType.ink,0)+
                " Quill:"+visibleSymbols.getOrDefault(TokenType.quill,0)+" Scroll:"+visibleSymbols.getOrDefault(TokenType.scroll,0));
        if(!(availablePositions==null)&&!availablePositions.isEmpty()) {
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
                    asciiArt = GameViewCli.printCardAsciiFront(temp.get().getID() );
                }else {
                    asciiArt = GameViewCli.printCardAsciiBack(temp.get().getID() );
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
}
