package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.view.MenuView;

import java.util.Collections;
import java.util.Scanner;

public class ClientMain {
    public static boolean stop;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'Rmi' if you want to use Rmi. !!Any other input will start the program with Socket!!");
        if (scanner.nextLine().equalsIgnoreCase("rmi")) {
            ConstantValues.usingSocket = false;
            System.out.println("Now using RMI");
        }else {
            System.out.println("Now using Socket");
        }
        System.out.println("Type 'GUI' if you want to use the GUI. !!Any other input will start the program with the CLI!!");
        if (scanner.nextLine().equalsIgnoreCase("gui")) {
            ConstantValues.usingCLI=false;
            System.out.println("Starting the GUI...");
            //TODO: start GUI program
        }else {
            System.out.println("Starting the CLI...");
        }
        MenuView menu = new MenuView();
        UserListener listener = ClientController.getInstance();
        stop = false;
        MenuView.printMainMenu();
        while (!stop) {
            String userInput = scanner.nextLine();
            if (userInput.equals("quit")) {
                stop = true;
            }
            menu.commandMenu(userInput, listener);
        }
        scanner.close();
    }

}
