package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.UserListener;
import it.polimi.ingsw.view.MenuView;

import java.util.Scanner;

public class ClientMain {
    public static boolean stop;
    public static void main(String[] args) {
        MenuView menu = new MenuView();
        Scanner scanner= new Scanner(System.in);
        UserListener listener = ClientController.getInstance();
        stop=false;
        MenuView.printMainMenu();
        while (!stop){
            String userInput=scanner.nextLine();
            if(userInput.equals("quit")){
                stop=true;
            }
            menu.commandMenu(userInput,listener);
        }
        scanner.close();
    }

}
