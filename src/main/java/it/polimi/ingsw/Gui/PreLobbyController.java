package it.polimi.ingsw.Gui;

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
    private Button gameRulesButton;

    @FXML
    private Button leaveButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Label errorMessage;

    UserListener listener;

    public void couldNotConnect(String s){
        Stage popUp = new Stage();
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PopUp.fxml"));
        try {
            popUp.setScene(new Scene(loader.load()));
        }catch (IOException e){
            throw new RuntimeException();
        }
        ((PopUpController)loader.getController()).initialize(s);
        popUp.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> popUp.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }
    @FXML
    private void initialize() {
        joinLobbyButton.setOnMouseClicked(event -> {
            String ip = ipTextField.getText().trim();
            String portText = portTextField.getText().trim();

            if (ip.isEmpty() || portText.isEmpty()) {
                cannotJoin();
            } else {
                try {
                    int port = Integer.parseInt(portText);
                    JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(port, ip);
                    ClientController.getInstance().receiveCommand(joinLobbyCommand);
                } catch (NumberFormatException e) {
                    cannotJoin();
                }
            }
        });
    }

    private void cannotJoin() {
        errorMessage.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> errorMessage.setVisible(false)));
        timeline.play();
    }
}
