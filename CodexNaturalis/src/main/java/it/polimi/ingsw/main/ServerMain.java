package it.polimi.ingsw.main;

import it.polimi.ingsw.network.socket.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        System.out.println("ServerIP: " + String.valueOf(InetAddress.getLocalHost()));
        Server server = new Server();
        server.start(4321);
        server.start();
    }
}
