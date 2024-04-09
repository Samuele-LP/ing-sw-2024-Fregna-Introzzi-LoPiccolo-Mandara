package it.polimi.ingsw.network.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class CardGameServer {
    try {
        ServerSocket serverSocket = new ServerSocket(0);    //Specifing "0" as socket implies that an available socket will be choosen automatically
        public int socket = serverSocket.getLocalPort();

        System.out.println("Server started. Waiting for players...");

        // Accept players
        Socket player1Socket = serverSocket.accept();
        System.out.println("Player 1 connected.");
        Socket player2Socket = serverSocket.accept();
        System.out.println("Player 2 connected.");
        Socket player1Socket = serverSocket.accept();
        System.out.println("Player 3 connected.");
        Socket player2Socket = serverSocket.accept();
        System.out.println("Player 4 connected.");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connession accepted from " + clientSocket.getInetAddress());
            new Thread(new ClientHandler(clientSocket)).start();
        }



        // Close sockets
        player1Socket.close();
        player2Socket.close();
        player3Socket.close();
        player4Socket.close();
        serverSocket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
