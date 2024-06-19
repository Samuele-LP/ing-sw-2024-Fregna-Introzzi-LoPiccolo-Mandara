package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains the ip of the server, as chosen by the player
 */
public class JoinLobbyCommand extends UserCommand {
    private final String ip;


    /**
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }
    public JoinLobbyCommand( String ip){
        this.ip = ip;
    }
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }

}
