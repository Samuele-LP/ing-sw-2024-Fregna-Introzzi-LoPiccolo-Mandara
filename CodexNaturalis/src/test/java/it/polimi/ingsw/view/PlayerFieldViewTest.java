package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PlayerFieldViewTest {
    PlayerFieldView test= new PlayerFieldView();
    @Before
    public void setUp() throws Exception {
        Map<TokenType,Integer> visibleSymbols = new HashMap<>();
        visibleSymbols.put(TokenType.fungi,4);
        visibleSymbols.put(TokenType.animal,7);
        visibleSymbols.put(TokenType.plant,8);
        visibleSymbols.put(TokenType.insect,9);
        visibleSymbols.put(TokenType.blocked,32);
        visibleSymbols.put(TokenType.empty,2);
        visibleSymbols.put(TokenType.ink,0);
        visibleSymbols.put(TokenType.quill,34);
        visibleSymbols.put(TokenType.scroll,2);
        test.update(0,0,0,true,visibleSymbols);
        test.update(1,-9,8,true,visibleSymbols);
        test.update(2,2,10,true,visibleSymbols);
        test.update(10,1,0,false,visibleSymbols);
        test.update(33,0,1,false,visibleSymbols);
    }

    @Test
    public void printField() {
        test.printField();
    }
}