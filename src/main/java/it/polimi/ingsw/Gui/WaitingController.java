package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatLogCommand;
import javafx.fxml.FXML;

public class WaitingController implements GuiController{
    @FXML
    private void goToChat(){
        ClientController.getInstance().receiveCommand(new ChatLogCommand());
    }
}
