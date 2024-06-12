package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.ServerRMI;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.view.MenuView;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        System.out.println("ServerIP: " + String.valueOf(InetAddress.getLocalHost()));

        ServerRMI serverRMI = new ServerRMI();
        serverRMI.start(1234);
        serverRMI.start();
        System.out.println("RMI Server created!");

        SocketServer socketServer = new SocketServer();
        socketServer.start(4321);
        socketServer.start();
        System.out.println("Socket Server created!");
    }
}
