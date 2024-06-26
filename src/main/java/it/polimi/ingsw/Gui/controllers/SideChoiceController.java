package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.StartingCardSideCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Controller class for handling the side choice of a starting card.
 * This class is used when a player has to choose his starting card side or the side they are placing their card in.
 */
public class SideChoiceController extends GuiController {

    @FXML
    private Button close;

    @FXML
    ImageView backImage;

    @FXML
    ImageView frontImage;

    /**
     * Sends the StartingCardSideCommand with false argument to the ClientController when the player chooses the
     * back of the card by clicking on the button
     */
    @FXML
    private void onBackPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(false));
    }

    /**
     * Sends the StartingCardSideCommand with true argument to the ClientController when the player chooses the
     * front of the card by clicking on the button
     */
    @FXML
    private void onFrontPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(true));
    }

    /**
     * Initializes the controller with the card images.
     *
     * @param cardId the ID of the card to display
     */
    public void initialize(int cardId) {
        close.setVisible(false);
        String cardImg = cardId + ".png";
        Image img = new Image(Objects.requireNonNull(GuiApplication.class.getResource("Cards/Front/" + cardImg)).toExternalForm());
        frontImage.setImage(img);
        img = new Image(Objects.requireNonNull(GuiApplication.class.getResource("Cards/Back/" + cardImg)).toExternalForm());
        backImage.setImage(img);
    }
}
