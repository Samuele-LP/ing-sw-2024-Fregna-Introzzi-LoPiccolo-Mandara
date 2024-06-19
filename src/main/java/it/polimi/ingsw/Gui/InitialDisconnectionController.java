package it.polimi.ingsw.Gui;

import it.polimi.ingsw.Gui.controllers.GuiController;
import javafx.fxml.FXML;

public class InitialDisconnectionController extends GuiController {
    @FXML
    private void exitButtonAction(){
        System.exit(1);
    }
}
