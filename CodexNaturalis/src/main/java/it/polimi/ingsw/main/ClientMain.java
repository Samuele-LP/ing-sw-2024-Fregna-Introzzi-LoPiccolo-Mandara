package it.polimi.ingsw.main;

import it.polimi.ingsw.network.socket.client.ClientSocket;

public class ClientMain {
    public static void main(String[] args) {
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.start();
    }
}
