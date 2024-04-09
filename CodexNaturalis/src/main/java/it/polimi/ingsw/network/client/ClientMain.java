package it.polimi.ingsw.network.client;

import java.io.*;
import java.net.*;

public class ClientMain(String serverIP) throws InvalidServerIP{
    try {
        Socket socket = new Socket(serverIP, 12345); //DOUBT: how to dinamically alloc a valid socket?
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void startConnection(){

}
