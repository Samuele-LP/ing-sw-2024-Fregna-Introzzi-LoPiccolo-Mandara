package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ColourCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for handling colour choice in the GUI.
 */
public class ColourChoiceController extends GuiController {

    @FXML
    private Label errorLabel;

    /**
     * Handles the selection of the red colour.
     * Sends a ColourCommand with the red colour code to the ClientController and loads the waiting screen.
     */
    @FXML
    private void red() {
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiRed));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Handles the selection of the blue colour.
     * Sends a ColourCommand with the blue colour code to the ClientController and loads the waiting screen.
     */
    @FXML
    private void blue() {
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiBlue));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Handles the selection of the green colour.
     * Sends a ColourCommand with the green colour code to the ClientController and loads the waiting screen.
     */
    @FXML
    private void green() {
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiGreen));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Handles the selection of the yellow colour.
     * Sends a ColourCommand with the yellow colour code to the ClientController and loads the waiting screen.
     */
    @FXML
    private void yellow() {
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiYellow));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Initializes the controller.
     * Sets the visibility of the error label and the generic text based on the errorTxt parameter.
     *
     * @param errorTxt indicates whether to show the error message
     */
    public void initialize(boolean errorTxt) {
        errorLabel.setVisible(false);
        genericText.setVisible(false);

        if (errorTxt) {
            errorLabel.setVisible(true);
        }
    }
}
