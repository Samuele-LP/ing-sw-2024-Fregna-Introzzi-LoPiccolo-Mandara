package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.ServerRMI;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.view.MenuView;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type 'RMI' if you want to use RMI. !!Any other input will start the program with Socket!!");

        if (scanner.nextLine().equalsIgnoreCase("rmi")) {
            System.out.println("Now using RMI");
            ServerRMI serverRMI = new ServerRMI();
            serverRMI.start(1234);
            serverRMI.start();
        } else {
            System.out.println("Now using Socket");
            SocketServer socketServer = new SocketServer();
            socketServer.start(4321);
            socketServer.start();
        }
    }
}
