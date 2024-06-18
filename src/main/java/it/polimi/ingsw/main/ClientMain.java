package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.view.MenuView;

import java.io.File;
import java.util.Scanner;

public class ClientMain {
    public static boolean stop;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(new File("").getAbsolutePath());
        System.out.println("Type 'RMI' if you want to use RMI. !!\nAny other input will start the program with Socket!!\n\n");

        if (scanner.nextLine().equalsIgnoreCase("rmi")) {
            ConstantValues.usingSocket = false;
            System.out.println("Now using RMI");
        } else {
            System.out.println("Now using Socket");
        }

        System.out.println("Type 'GUI' if you want to use the GUI. !!\nAny other input will start the program with the CLI!!\n\n");

        if (scanner.nextLine().equalsIgnoreCase("gui")) {
            ConstantValues.usingCLI = false;
            System.out.println("Starting the GUI...");
            GuiApplication.main(args);
        } else {
            System.out.println("Starting the CLI...");
            MenuView menu = new MenuView();
            UserListener listener = ClientController.getInstance();
            stop = false;
            MenuView.printMainMenu();
            while (!stop) {
                String userInput = scanner.nextLine();
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
