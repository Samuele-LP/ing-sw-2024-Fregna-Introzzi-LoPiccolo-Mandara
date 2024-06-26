package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Controller class for the pre-lobby screen.
 * This class handles the user interactions on the pre-lobby screen,
 * including joining a lobby and leaving the application.
 */
public class PreLobbyController extends GuiController {
    @FXML
    private Button leaveButton;
    @FXML
    private TextField ipTextField;
    @FXML
    private Button joinLobbyButton;

    /**
     * Initializes the pre-lobby screen.
     * Sets up the event handlers for the buttons and text field.
     */
    @FXML
    private void initialize() {
        leaveButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));

        joinLobbyButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));

        genericText.setFill(Color.RED);

        joinLobbyButton.setOnMouseClicked(event -> {
            String ip = ipTextField.getText().trim();

            if (ip.isEmpty()) {
                displayText(null);
            } else {
                JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(ip);
                ClientController.getInstance().receiveCommand(joinLobbyCommand);
            }
        });

        leaveButton.setOnMouseClicked(mouseEvent -> System.exit(1));
    }

    /**
     * Displays a pop-up with the specified message for a given duration.
     *
     * @param message the message to display
     */
    @Override
    public void displayText(String message) {
        if (message != null) {
            genericText.setText(message);
        }

        genericText.setVisible(true);
    }
}
