package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.SecretObjectiveCommand;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Controller class for handling the selection of a secret objective.
 * This class is used when a player has to choose between two secret objectives.
 */
public class SecretObjectiveController extends GuiController {

    private int firstCard, secondCard;

    @FXML
    private ImageView firstObj;

    @FXML
    private ImageView secondObj;

    /**
     * Sends the SecretObjectiveCommand to the ClientController when the player chooses the first objectiveCard
     * of the two by clicking on the first button
     */
    @FXML
    private void onFirstClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(firstCard));
        GuiApplication.loadWaitingScreen();

    }

    /**
     * Sends the SecretObjectiveCommand to the ClientController when the player chooses the second objectiveCard
     * of the two by clicking on the second button
     */
    @FXML
    private void onSecondClick() {
        ClientController.getInstance().receiveCommand(new SecretObjectiveCommand(secondCard));
        GuiApplication.loadWaitingScreen();
    }

    /**
     * Initializes the controller with the secret objective cards.
     *
     * @param firstCard  the ID of the first secret objective card
     * @param secondCard the ID of the second secret objective card
     */
    public void initialize(int firstCard, int secondCard) {
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        String cardImg = this.firstCard + ".png";
        Image img = new Image(Objects.requireNonNull(GuiApplication.class.getResource("Cards/Front/" + cardImg)).toExternalForm());
        firstObj.setImage(img);
        cardImg = this.secondCard + ".png";
        img = new Image(Objects.requireNonNull(GuiApplication.class.getResource("Cards/Front/" + cardImg)).toExternalForm());
        secondObj.setImage(img);
    }
}
