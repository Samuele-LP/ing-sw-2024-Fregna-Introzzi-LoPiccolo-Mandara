package it.polimi.ingsw.network.socket.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public java.net.ServerSocket serverSocket;
    public List<String> namePlayers;
    public List<Socket> playersSocket;
    public boolean gameBegin = false;

    public Server() throws IOException {
        this.serverSocket = serverSocketInitialization();
        int socket = serverSocket.getLocalPort();
        System.out.println("Server started. Waiting for players...");

        playersSocket = new ArrayList<>(4);
        while(playersSocket.size()<4)
            addPlayer();
        // after game is starter NB: BISOGNA AGGIUNGERE UNA CONDIZIONE NEL WHILE CHE TERMINI SOLO QUANDO IL SERVER
        // DECIDE DI INIZIARE LA PARTITA
        acceptPlayers();

        for(Socket s : playersSocket) {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Buffer
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        endGame(serverSocket);
    }

    /**
     * Initialize server socket
     * @return ServerSocket()
     * @throws IOException
     */
    private java.net.ServerSocket serverSocketInitialization() throws IOException {
        return new java.net.ServerSocket(0);
    }

    /**
     * Add a player to the list of players
     */
    private void addPlayer(){
        Socket playerXSocket = new Socket();
        playersSocket.add(playerXSocket);
        namePlayers.add();
        System.out.println("Player " + playersSocket.size() +  " added.");
    }

    private void acceptPlayers() throws IOException {
        for (Socket s : playersSocket){
            s = serverSocket.accept();
            System.out.println("Player " + playersSocket.indexOf(s) + " connected.");
        }
    }

    /**
     * All Sockets gets closed
     * @param serverSocket
     * @throws IOException
     */
    private void endGame(java.net.ServerSocket serverSocket) throws IOException {
        for (Socket s : playersSocket)
            s.close();
        serverSocket.close();
    }
}

