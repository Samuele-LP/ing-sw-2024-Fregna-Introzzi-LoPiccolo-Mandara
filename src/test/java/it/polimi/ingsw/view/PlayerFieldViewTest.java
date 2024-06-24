package it.polimi.ingsw.view;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.view.Field.PlayerFieldView;
import it.polimi.ingsw.view.Field.PlayerFieldViewCli;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerFieldViewTest {
    PlayerFieldView test = new PlayerFieldViewCli("");

    public PlayerFieldViewTest() throws IOException {
    }

    @Before
    public void setUp() {
        GameView v = new GameViewCli();
        Map<TokenType, Integer> visibleSymbols = new HashMap<>();
        visibleSymbols.put(TokenType.fungi, 4);
        visibleSymbols.put(TokenType.animal, 7);
        visibleSymbols.put(TokenType.plant, 8);
        visibleSymbols.put(TokenType.insect, 9);
        visibleSymbols.put(TokenType.blocked, 32);
        visibleSymbols.put(TokenType.empty, 2);
        visibleSymbols.put(TokenType.ink, 0);
        visibleSymbols.put(TokenType.quill, 34);
        visibleSymbols.put(TokenType.scroll, 2);
        test.updateField(85, 0, 0, true, visibleSymbols);
        test.updateField(11, -2, 1, true, visibleSymbols);
        test.updateField(21, 2, 3, true, visibleSymbols);
        test.updateField(51, 1, 0, true, visibleSymbols);
        test.updateField(1, 0, 1, true, visibleSymbols);
        List<Point> list = new ArrayList<>();
        list.add(new Point(1, 1));
        list.add(new Point(54, 43));
        list.add(new Point(15, 65));
        test.updateAvailablePositions(list);
    }

    @Test
    public void printField() {
        for (String s : ((PlayerFieldViewCli) test).printField()) {
            System.out.println(s);
        }
    }
}