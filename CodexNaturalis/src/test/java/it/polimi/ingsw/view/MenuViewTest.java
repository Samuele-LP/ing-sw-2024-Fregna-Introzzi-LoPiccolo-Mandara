package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.UserListener;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MenuViewTest {
    MenuView test = new MenuView();
    UserListener lis;

    List <String> lecters = Arrays.asList("c", "g", "h", "n", "np", "r");

    @Test
    public void PrintMainMenu(){
        test.printMainMenu();
    }

    @Test
    public void PrintCommand(){test.commandMenu(
            "starting_game", lis);
    }

    @Test
    public void PrintRandomCommand(){
        Random rand = new Random();
        String randomElement = lecters.get(rand.nextInt(lecters.size()));
        System.out.println(randomElement);
        test.commandMenu(randomElement, lis);
    }

}