package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains information about the chosen secret objective
 */
public class SecretObjectiveCommand extends UserCommand{
    private final int objective;
    /**
     *
     * @return the objective
     */
    public int getObjective() {
        return objective;
    }
    public SecretObjectiveCommand(int objective){
        this.objective = objective;
    }

    /**
     *
     * @param lis handles the command passed by the player
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
