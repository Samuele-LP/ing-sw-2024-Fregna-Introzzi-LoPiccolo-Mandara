package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.SecretObjectiveCommand;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SecretObjectiveController extends GuiController {
    private int firstCard, secondCard;
    @FXML
    private ImageView firstObj;
    @FXML
    private ImageView secondObj;
    @FXML
    private void onFirstClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(firstCard));
        GuiApplication.loadWaitingScreen();

    }

    @FXML
    private void onSecondClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(secondCard));
        GuiApplication.loadWaitingScreen();
    }

    public void initialize(int firstCard, int secondCard) {
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        String cardImg = this.firstCard + ".png";
        Image img = new Image(GuiApplication.class.getResource("Cards/Front/" + cardImg).toExternalForm());
        firstObj.setImage(img);
        cardImg = this.secondCard + ".png";
        img = new Image(GuiApplication.class.getResource("Cards/Front/" + cardImg).toExternalForm());
        secondObj.setImage(img);
    }
}
