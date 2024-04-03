package it.polimi.ingsw.network.client;

import java.io.*;
import java.net.*;

        // TUTTO DA FARE. QUESTA E' SOLO UNA BOZZA

public class ClientMain {
    try {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
