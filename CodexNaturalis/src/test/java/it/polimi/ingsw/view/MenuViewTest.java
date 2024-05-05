package it.polimi.ingsw.view;

import static org.junit.Assert.*;

import it.polimi.ingsw.controller.userCommands.UserListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

public class MenuViewTest {
    MenuView test = new MenuView();
    @Test
    public void PrintMenu(){test.printMenu(); }
    UserListener lis;
    @Test
    public void PrintCommand(){test.commandMenu("starting_game", lis);}

}