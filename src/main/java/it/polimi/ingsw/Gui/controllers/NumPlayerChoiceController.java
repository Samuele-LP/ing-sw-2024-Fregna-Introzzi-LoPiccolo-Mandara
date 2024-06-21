package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NumberOfPlayersCommand;
import javafx.fxml.FXML;

public class NumPlayerChoiceController extends GuiController {
    @FXML
    public void twoClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(2));
        GuiApplication.loadWaitingScreen();
    }

    @FXML
    public void threeClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(3));
        GuiApplication.loadWaitingScreen();
    }

    @FXML
    public void fourClicked() {
        ClientController.getInstance().receiveCommand(new NumberOfPlayersCommand(4));
        GuiApplication.loadWaitingScreen();
    }
}
