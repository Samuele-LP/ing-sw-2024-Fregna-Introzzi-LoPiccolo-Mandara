package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.util.ArrayList;
import java.util.List;

public class StartingCard extends PlayableCard{
    private final TokenType centralSymbol1, centralSymbol2,centralSymbol3;
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
}
