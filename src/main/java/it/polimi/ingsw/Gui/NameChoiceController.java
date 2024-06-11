package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NameCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class NameChoiceController {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label waitLabel;

    @FXML
    private ImageView waitingImageView;

    @FXML
    private Label chatLabel;

    @FXML
    private Button chatButton;

    @FXML
    private Label errorMessage;

    @FXML
    public void initialize() {

        sendButton.setOnMouseClicked(event ->{
            String name = nameTextField.getText().trim();
            if (name.isEmpty()) {
                emptyName();
            } else {
                errorMessage.setVisible(false);
                NameCommand nameCommand = new NameCommand(name);
                ClientController.getInstance().receiveCommand(nameCommand);
            }
        });
    }

    private void emptyName() {
        errorMessage.setText("Please enter your name.");
        errorMessage.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> errorMessage.setVisible(false)));
        timeline.play();
    }
}
