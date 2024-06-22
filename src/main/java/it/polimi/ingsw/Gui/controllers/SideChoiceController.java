package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.StartingCardSideCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;

/**
 * Controller class for handling the side choice of a starting card.
 * This class is used when a player has to choose their starting card side or the side they are placing their card in.
 */
public class SideChoiceController extends GuiController {

    @FXML
    private Button close;

    @FXML
    ImageView backImage;

    @FXML
    ImageView frontImage;

    /**
     * Sends a command to choose the back side of the card.
     */
    @FXML
    private void onBackPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(false));
    }

    /**
     * Sends a command to choose the front side of the card.
     */
    @FXML
    private void onFrontPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(true));
    }

    /**
     * Initializes the controller with the card images.
     * @param cardId the ID of the card to display
     * @throws FileNotFoundException if the card image is not found
     */
    public void initialize(int cardId) throws FileNotFoundException {
        close.setVisible(false);
        String cardImg = cardId + ".png";
        Image img = new Image(GuiApplication.class.getResource("Cards/Front/" + cardImg).toExternalForm());
        frontImage.setImage(img);
        img = new Image(GuiApplication.class.getResource("Cards/Back/" + cardImg).toExternalForm());
        backImage.setImage(img);
    }
}
