package it.polimi.ingsw.main;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.network.server.SocketServer;

import java.io.IOException;
import java.net.InetAddress;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        System.out.println("ServerIP: " + InetAddress.getLocalHost() + "Port :"+ConstantValues.socketPort);
        new Thread (()-> {
            SocketServer socketServer = new SocketServer();
            socketServer.start(4321);
        }).start();
    }
}
