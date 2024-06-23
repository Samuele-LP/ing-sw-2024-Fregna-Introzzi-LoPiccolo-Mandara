package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.view.MenuView;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientMain {
    public static boolean stop;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type 'CLI' if you want to use the CLI. !!\nAny other input will start the program with the GUI!!\n\n");

        if (!scanner.nextLine().equalsIgnoreCase("cli")) {
            System.out.println("If you don't want the gui to be always on top of other windows type 'n'/'no'\nAny other input will be interpreted as 'yes'\n\n");
            String choice= scanner.nextLine();
            ConstantValues.alwaysOnTop = !( choice.equalsIgnoreCase("n")||choice.equalsIgnoreCase("no"));
            ConstantValues.usingCLI = false;
            System.out.println("Starting the GUI with Socket...");
            GuiApplication.main(args);
        } else {
            System.out.println("Starting the CLI with Socket...");
            MenuView menu = new MenuView();
            UserListener listener = ClientController.getInstance();
            stop = false;
            MenuView.printMainMenu();
            while (!stop) {
                String userInput = "";
                try {
                    userInput = scanner.nextLine();
                }catch (NoSuchElementException e){//This is used to avoid problems if the program is closed with Ctrl+C in the terminal
                    userInput = null;
                }
                if (stop) {
                    System.out.println("Press Enter to close the program...");
                    scanner.nextLine();
                    scanner.close();
                    System.exit(1);
                    return;
                }
                menu.commandMenu(userInput, listener);
            }
        }
        scanner.close();
    }
}
