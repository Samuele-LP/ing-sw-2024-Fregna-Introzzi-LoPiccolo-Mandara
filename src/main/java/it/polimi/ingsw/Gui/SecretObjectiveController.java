package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.SecretObjectiveCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SecretObjectiveController implements GuiController {
    private int firstCard, secondCard;
    @FXML
    private Button first;
    @FXML
    private Button second;
    @FXML
    private ImageView firstObj;
    @FXML
    private ImageView secondObj;

    @FXML
    private void onFirstClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(firstCard));
    }

    @FXML
    private void onSecondClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(secondCard));
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
