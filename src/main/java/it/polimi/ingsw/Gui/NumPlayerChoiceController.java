package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NumberOfPlayerCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class NumPlayerChoiceController extends GuiController {
    @FXML
    public void twoClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(2));
        GuiApplication.loadWaitingScreen();
    }
    @FXML
    public void threeClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(3));
        GuiApplication.loadWaitingScreen();
    }
    @FXML
    public void fourClicked(){
        ClientController.getInstance().receiveCommand(new NumberOfPlayerCommand(4));
        GuiApplication.loadWaitingScreen();
    }
}
