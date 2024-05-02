package it.polimi.ingsw.controller.userCommands;

import it.polimi.ingsw.Point;

public class PlaceCardCommand extends UserCommand{
    private final String command = "plc_card";
    private Point position;

    public String getCommand() {
        return command;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void sendCommand(UserListener lis) {
        lis.receiveCommand(this);
    }
}
