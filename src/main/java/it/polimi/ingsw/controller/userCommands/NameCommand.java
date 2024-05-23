package it.polimi.ingsw.controller.userCommands;

public class NameCommand extends UserCommand{
    private final String name;

    /**
     *
     * @param name is the name chosen by the player
     */
    public NameCommand(String name){
        this.name = name;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
    public String getName( ){
        return name;
    }
}
