package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NumberOfPlayersCommand;
import javafx.fxml.FXML;

/**
 * Controller class for the number of players choice screen.
 * This class handles the user interactions for selecting the number of players in the game.
 */
public class NumPlayerChoiceController extends GuiController {

    /**
     * Handles the event when the button for 2 players is clicked.
     * Sends a command to the server indicating the selection of 2 players
     * and then loads the waiting screen.
     */
    @FXML
    public void twoClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(2));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Handles the event when the button for 3 players is clicked.
     * Sends a command to the server indicating the selection of 3 players
     * and then loads the waiting screen.
     */
    @FXML
    public void threeClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(3));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Handles the event when the button for 4 players is clicked.
     * Sends a command to the server indicating the selection of 4 players
     * and then loads the waiting screen.
     */
    @FXML
    public void fourClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(4));
        GuiApplication.loadWaitingScreen();
    }
}
