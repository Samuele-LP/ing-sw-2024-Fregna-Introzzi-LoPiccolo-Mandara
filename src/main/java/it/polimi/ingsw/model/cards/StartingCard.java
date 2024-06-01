package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that adds the information necessary to handle  a starting card
 */
public class StartingCard extends PlayableCard {
    private final TokenType centralSymbol1, centralSymbol2,centralSymbol3;

    /**
     * @param ID id of the card
     * @param topRight top right corner
     * @param topLeft top left corner
     * @param bottomLeft bottom left corner
     * @param bottomRight bottom right corner
     * @param backTopRight back top right corner
     * @param backTopLeft back top left corner
     * @param backBottomLeft back bottom left corner
     * @param backBottomRight back bottom right corner
     * @param colour colour of the card
     * @param centralSymbol1 first central symbol
     * @param centralSymbol2 second central symbol
     * @param centralSymbol3 third central symbol
     */
    public StartingCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour, TokenType centralSymbol1, TokenType centralSymbol2, TokenType centralSymbol3) {
        super(ID, topRight, topLeft, bottomLeft, bottomRight, backTopRight, backTopLeft, backBottomLeft, backBottomRight, colour);
        this.centralSymbol1 = centralSymbol1;
        this.centralSymbol2 = centralSymbol2;
        this.centralSymbol3 = centralSymbol3;
    }

    /**
     * This method returns a List of TokenType, they are the symbols that are in the center of the card, and they can be only 1 symbol, 2 symbols
     * or 3 symbols.
     * There is always at least one symbol; if there are less than 3 then the corresponding attribute is set  to TokenType.empty
     * @return centralSymbols
     */
    public List<TokenType> getCentralSymbols(){
        List<TokenType>central = new ArrayList<>();
        central.add(centralSymbol1);
        if(centralSymbol2!=TokenType.empty){
            central.add(centralSymbol2);
        }
        if(centralSymbol3!=TokenType.empty){
            central.add(centralSymbol3);
        }
        return central;
    }
    @Override
    public String printCardInfo() {
        String s=super.printCardInfo()+"This card has in its center on the front: "+centralSymbol1.toString();
        if(centralSymbol2!=TokenType.empty){
            s=s+", "+centralSymbol2.toString();
        }
        if(centralSymbol3!=TokenType.empty) {
            s = s + ", " + centralSymbol3.toString();
        }
        return s+"\n";
    }
    /**
     * This method returns the ascii art for the front of the card
     * @return an array containing two strings. The first is the top line, the second is the bottom line.
     */
    public String[] asciiArtFront(){
        String[] art = super.asciiArtFront();
        art[1]=centralSymbol3.toString()+centralSymbol1.toString()+centralSymbol2.toString();
        return art;
    }
    /**
     * This method returns the ascii art for the back of the card
     * @return an array containing two strings. The first is the top line, the second is the bottom line.
     */
    public String[] asciiArtBack(){
        String c1 = backTopLeft.toString();
        String c2 = backTopRight.toString();
        String c3 = backBottomLeft.toString();
        String c4 = backBottomRight.toString();
        String[] art = new String[3];
        art[0] =c1+"   "+c2;
        art[1]="\u001B[49m"+"         "+"\u001B[0m";
        art[2] =c3+"   "+c4;
        return art;
    }
}
