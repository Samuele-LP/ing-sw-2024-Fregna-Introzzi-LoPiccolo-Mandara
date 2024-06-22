package it.polimi.ingsw.controller.userCommands;

/**
 * Command that contains information about the chosen secret objective.
 */
public class SecretObjectiveCommand extends UserCommand {

    private final int objective;

    /**
     * Constructor for SecretObjectiveCommand.
     *
     * @param objective the ID of the chosen secret objective
     */
    public SecretObjectiveCommand(int objective){
        this.objective = objective;
    }

    /**
     * @return the ID of the chosen secret objective
     */
    public int getObjective() {
        return objective;
    }

    /**
     * Sends the command to the specified UserListener, providing information about the chosen secret objective.
     *
     * @param lis the UserListener that will process this command
     */
    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
