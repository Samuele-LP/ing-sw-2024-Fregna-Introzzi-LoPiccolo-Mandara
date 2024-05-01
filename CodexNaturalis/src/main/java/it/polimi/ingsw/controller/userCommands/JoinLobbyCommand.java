package it.polimi.ingsw.controller.userCommands;

import java.util.Scanner;

public class JoinLobbyCommand extends UserCommand {
    private final String command = "join_lobby";
    private int port;
    private int ip;

    /**
     *
     * @return the ip
     */
    public int getIp() {
        return ip;
    }

    /**
     *
     * @return the port (a number of 16 bit)
     */
    public int getPort() {
        return port;
    }
    JoinLobbyCommand(UserListener lis, int port, int ip){
        this.ip = ip;
        this.port = port;
        lis.receiveCommand(this.command);
    }
}
