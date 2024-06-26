package it.polimi.ingsw.Gui.controllers;

import javafx.fxml.FXML;

/**
 * Controller class for handling the initial disconnection scenario in the GUI.
 * Provides functionality to exit the application when a disconnection occurs at the initial stage.
 */
public class InitialDisconnectionController extends GuiController {

    /**
     * Handles the action of the exit button.
     * Exits the application when the exit button is pressed.
     */
    @FXML
    private void exitButtonAction(){
        System.exit(1);
    }
}
