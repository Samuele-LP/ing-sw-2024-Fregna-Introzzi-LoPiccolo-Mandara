package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.UserListener;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MenuViewTest {
    private final MenuView test = new MenuView();
    private final UserListener lis = ClientController.getInstance();

    private final List<String> lecters = Arrays.asList("c", "g", "h", "n", "np", "r");

    @Test
    public void PrintMainMenu() {
        MenuView.printMainMenu();
    }

    @Test
    public void PrintGameMenu() {
        MenuView.printGameMenu();
    }

    @Test
    public void PrintCommand() {
        test.commandMenu("connect", lis);
    }

    @Test
    public void PrintRandomCommand() {
        Random rand = new Random();
        String randomElement = lecters.get(rand.nextInt(lecters.size()));
        System.out.println(randomElement);
        test.commandMenu(randomElement, lis);
    }

}