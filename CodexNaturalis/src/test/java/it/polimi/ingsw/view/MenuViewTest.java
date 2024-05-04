package it.polimi.ingsw.view;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

public class MenuViewTest {
    MenuView test = new MenuView(2, "Vital", "Giorgio");
    @Test
    public void printMenu(){test.printMenu(2); }

    String name = new String("H");
    @Test
    public void CommandMenu(){test.printCommandMenu(name);}
}