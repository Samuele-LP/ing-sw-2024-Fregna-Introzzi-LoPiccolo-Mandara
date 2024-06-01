package it.polimi.ingsw.Gui.guicontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PreLobbyController {

    @FXML
    private Button gameRulesButton;

    @FXML
    private Button leaveButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private void handleGameRulesButtonAction() {
        System.out.println("Game Rules button clicked!");
    }

    @FXML
    private void handleLeaveButtonAction() {
        System.out.println("Leave button clicked!");
    }

    @FXML
    private void handleJoinLobbyButtonAction() {
        String ip = ipTextField.getText();
        System.out.println("Joining lobby with IP: " + ip);
    }
}
