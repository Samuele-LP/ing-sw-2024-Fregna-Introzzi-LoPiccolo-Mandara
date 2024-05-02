package it.polimi.ingsw.controller.userCommands;

public class JoinLobbyCommand extends UserCommand {
    private final String command = "join_lobby";
    private int port;
    private String ip;

    /**
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     *
     * @return the port (a number of 16 bit)
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     * @param ip
     */
    public JoinLobbyCommand(int port, String ip){
        this.ip = ip;
        this.port = port;
    }

    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
