package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NameCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Controller for handling the name choice view in the GUI.
 */
public class NameChoiceController extends GuiController {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label errorMessage;

    /**
     * Initializes the name choice view.
     *
     * @param previousName the previously entered name, if any, that was not available
     */
    @FXML
    public void initialize(String previousName) {
        if (previousName != null) {
            errorText(previousName + " was already chosen by someone!");
        }

        sendButton.setOnMouseClicked(event -> {
            String name = nameTextField.getText().trim();

            if (name.isEmpty()) {
                errorText("Please enter your name.");
            } else if (name.contains(" ")) {
                errorText("Your name cannot have spaces in it!");
            } else {
                errorMessage.setVisible(false);
                ClientController.getInstance().receiveCommand(new NameCommand(name));
            }
        });
    }

    /**
     * Displays an error message for a short duration.
     *
     * @param txt the error message text
     */
    private void errorText(String txt) {
        errorMessage.setText(txt);
        errorMessage.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> errorMessage.setVisible(false)));
        timeline.play();
    }
}
