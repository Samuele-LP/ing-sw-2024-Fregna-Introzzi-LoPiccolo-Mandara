package it.polimi.ingsw.Gui.guicontroller;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import it.polimi.ingsw.controller.userCommands.UserListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.List;

public class PreLobbyController implements GuiController{

    public Button joinButton;

    @FXML
    private Button gameRulesButton;

    @FXML
    private Button leaveButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button joinLobbyButton;

    UserListener listener;


    private void initialize() {

        int port = 12345;
        String ip = ipTextField.getText();

        JoinLobbyCommand joinLobbyCommand = new JoinLobbyCommand(port, ip);
        EventHandler<MouseEvent> joinHandler = joinLobbyCommand.getCommandHandler(listener);

        joinLobbyButton.setOnMouseClicked(joinHandler);
    }

}
