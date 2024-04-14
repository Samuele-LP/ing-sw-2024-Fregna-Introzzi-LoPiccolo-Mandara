package it.polimi.ingsw.network.client;

import java.io.*;
import java.net.*;

public class ClientMain {
    private final String hostname = "127.0.0.1";  // DA ALLOCARE DINAMICAMENTE
    private final int port = 1234; // DA ALLOCARE DINAMICAMENTE

    public ClientMain() {
        try {
            Socket socket = new Socket(hostname, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        } catch (UnknownHostException e){
            System.err.println("Unexisting host " + hostname);
            System.exit(1);
        } catch (IOException e){
            System.err.println("I/O unobtainable with host " + hostname);
            System.exit(1);
        }
    }

    public void startConnection(){

    }
}
