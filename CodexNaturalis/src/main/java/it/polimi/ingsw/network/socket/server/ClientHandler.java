package it.polimi.ingsw.network.socket.server;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

            String s;
            while ((s = in.readLine()) != null) {
                System.out.println("Received from client: " + s);
                if (s.equals("END")) break;
            }

            System.out.println("Ending connession...");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}