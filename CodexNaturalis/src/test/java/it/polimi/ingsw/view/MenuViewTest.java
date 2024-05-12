package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.UserListener;
import org.junit.Test;

public class MenuViewTest {
    MenuView test = new MenuView();
    @Test
    public void PrintMainMenu(){test.printMainMenu(); }
    UserListener lis;
    @Test
    public void PrintCommand(){test.commandMenu("starting_game", lis);}

}