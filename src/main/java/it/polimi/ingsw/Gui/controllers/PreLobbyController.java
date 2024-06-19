package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PreLobbyController extends GuiController {
    @FXML
    private Button leaveButton;
    @FXML
    private TextField ipTextField;
    @FXML
    private Button joinLobbyButton;
    /**
     * Method to initialize the preLobby scene with the ip and port fields and the button to join the lobby
     */
    @FXML
    private void initialize() {
        leaveButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));
        joinLobbyButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));
        genericText.setFill(Color.RED);
        joinLobbyButton.setOnMouseClicked(event -> {
            String ip = ipTextField.getText().trim();

            if (ip.isEmpty()) {
                displayText(null,2000);
            } else {
                JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(ConstantValues.usingSocket ? ConstantValues.socketPort : ConstantValues.rmiPort, ip);
                ClientController.getInstance().receiveCommand(joinLobbyCommand);
            }
        });
        leaveButton.setOnMouseClicked(mouseEvent -> System.exit(1));


    }
    /**
     * Displays a popUp with the input string as its text
     * @param duration duration of the popUp milliseconds
     */
    @Override
    public void displayText(String message, int duration) {
        if(message!=null){
            genericText.setText(message);
        }
        genericText.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
            genericText.setVisible(false);
            genericText.setText("Fill the box before joining the lobby");
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
