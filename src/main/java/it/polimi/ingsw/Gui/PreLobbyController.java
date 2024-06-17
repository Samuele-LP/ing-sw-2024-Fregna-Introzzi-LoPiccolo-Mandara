package it.polimi.ingsw.Gui;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import it.polimi.ingsw.controller.userCommands.UserListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PreLobbyController implements GuiController {


    @FXML
    private Button leaveButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Label errorMessage;

    /**
     * Method to initialize the preLobby scene with the ip and port fields and the button to join the lobby
     */
    @FXML
    private void initialize() {

        leaveButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));
        joinLobbyButton.setOnMouseClicked(e -> joinLobbyButton.setStyle("-fx-background-color: #005fa3; -fx-text-fill: white;"));

        joinLobbyButton.setOnMouseClicked(event -> {
            String ip = ipTextField.getText().trim();

            if (ip.isEmpty()) {
                cannotJoin();
            } else {
                JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(ConstantValues.usingSocket ? ConstantValues.socketPort : ConstantValues.rmiPort, ip);
                ClientController.getInstance().receiveCommand(joinLobbyCommand);
            }
        });
        leaveButton.setOnMouseClicked(mouseEvent -> System.exit(1));


    }

    /**
     * This is called when a player tries to join the lobby without correctly filling the ip and port fields and it displays an
     * error message to the screen
     */
    private void cannotJoin() {
        errorMessage.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> errorMessage.setVisible(false)));
        timeline.play();
    }
}
