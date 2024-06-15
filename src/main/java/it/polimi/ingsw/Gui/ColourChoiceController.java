package it.polimi.ingsw.Gui;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ColourCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColourChoiceController implements GuiController {
    @FXML
    Label errorLabel;
    @FXML
    private void red(){
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiRed));
        GuiApplication.loadWaitingScreen();
    }
    @FXML
    private void blue(){
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiBlue));
        GuiApplication.loadWaitingScreen();
    }
    @FXML
    private void green(){
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiGreen));
        GuiApplication.loadWaitingScreen();
    }
    @FXML
    private void yellow(){
        ClientController.getInstance().receiveCommand(new ColourCommand(ConstantValues.ansiYellow));
        GuiApplication.loadWaitingScreen();
    }
    public void initialize(boolean errorTxt){
        errorLabel.setVisible(false);
        errorLabel.setText("Change your choice, that colour was already chosen!");
        errorLabel.setTextFill(Color.RED);
        if(errorTxt){
            errorLabel.setVisible(true);
        }
    }
}
