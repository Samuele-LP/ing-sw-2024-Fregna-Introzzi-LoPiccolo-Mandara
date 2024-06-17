package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.StartingCardSideCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;

/**
 * Used when a player has to choose their starting card side or the side they are placing their card in
 */
public class SideChoiceController implements GuiController {
    @FXML
    private Button close;
    @FXML
    ImageView backImage;
    @FXML
    ImageView frontImage;

    @FXML
    private void onBackPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(false));
    }

    @FXML
    private void onFrontPress() {
        ClientController.getInstance().receiveCommand(new StartingCardSideCommand(true));
    }

    public void initialize(int cardId) throws FileNotFoundException {
        close.setVisible(false);
        String cardImg = cardId + ".png";
        Image img = new Image(GuiApplication.class.getResource("Cards/Front/" + cardImg).toExternalForm());
        frontImage.setImage(img);
        img = new Image(GuiApplication.class.getResource("Cards/Back/" + cardImg).toExternalForm());
        backImage.setImage(img);
    }
}
