package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains the IP of the server, as chosen by the player.
 */
public class JoinLobbyCommand extends UserCommand {

    private final String ip;

    /**
     * Constructor for JoinLobbyCommand.
     *
     * @param ip the IP address of the server to join
     */
    public JoinLobbyCommand( String ip){
        this.ip = ip;
    }

    /**
     * @return the IP address of the server
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sends the command to the specified UserListener, signaling the intention to join the lobby.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
