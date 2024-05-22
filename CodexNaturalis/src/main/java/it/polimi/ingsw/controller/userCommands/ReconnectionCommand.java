package it.polimi.ingsw.controller.userCommands;

public class ReconnectionCommand extends UserCommand{
    private final String ip;
    private final int port;
    private final String name;

    public ReconnectionCommand(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    /**
     * @param lis is the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
