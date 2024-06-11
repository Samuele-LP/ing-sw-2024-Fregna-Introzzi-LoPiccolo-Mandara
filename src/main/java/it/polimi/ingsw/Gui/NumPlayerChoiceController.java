package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NumberOfPlayerCommand;
import javafx.fxml.FXML;

public class NumPlayerChoiceController implements GuiController {
    @FXML
    public void twoClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(2));
    }
    @FXML
    public void threeClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(3));
    }
    @FXML
    public void fourClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(4));
    }
}
