package it.polimi.ingsw.controller.userCommands;

/**
 * Command generated in the name choice phase.
 */
public class NameCommand extends UserCommand{

    private final String name;

    /**
     * Constructor for NameCommand.
     *
     * @param name the name chosen by the player
     */
    public NameCommand(String name){
        this.name = name;
    }

    /**
     * @return the chosen name
     */
    public String getName( ){
        return name;
    }

    /**
     * Sends the command to the specified UserListener, signaling the chosen name.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
