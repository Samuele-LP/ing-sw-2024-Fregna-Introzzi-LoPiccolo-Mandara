package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import it.polimi.ingsw.controller.userCommands.UserListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PreLobbyController implements GuiController {

    public Button joinButton;

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

    UserListener listener;


    private void initialize() {

        int port = Integer.parseInt(portTextField.getText());
        String ip = ipTextField.getText();

        JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(port, ip);
        EventHandler<MouseEvent> joinHandler = joinLobbyCommand.getCommandHandler(listener);

        joinLobbyButton.setOnMouseClicked(joinHandler);
    }

}
