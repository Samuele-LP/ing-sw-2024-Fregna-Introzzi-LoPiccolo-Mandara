package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ShowFieldCommand;
import it.polimi.ingsw.controller.userCommands.StartingCardSideCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

/**
 * Used when a player has to choose their starting card side or the side they are placing their card in
 */
public class SideChoiceController implements GuiController {
    private int card;
    private boolean startingCard;
    private Stage stage;
    @FXML
    private Button close;
    @FXML
    ImageView backImage;
    @FXML
    ImageView frontImage;
    @FXML
    private void onBackPress() {
        if(startingCard){
            ClientController.getInstance().receiveCommand(new StartingCardSideCommand(false));
        }else {
            //TODO: if we actually use this scene as part of the placing actions then we must introduce new variables to memorize the command
        }
    }

    @FXML
    private void onFrontPress() {
        if(startingCard){
            ClientController.getInstance().receiveCommand(new StartingCardSideCommand(true));
        }else {
         //TODO:
        }
    }
    public void initialize(int cardId, boolean isStartingCard) throws FileNotFoundException {
        if(isStartingCard){
            close.setVisible(false);
        }else{
            close.setOnMouseClicked(mouseEvent -> {
                ClientController.getInstance().receiveCommand(new ShowFieldCommand());
            });
        }
        this.card = cardId;
        this.startingCard = isStartingCard;
        String cardImg= this.card+".png";
        Image img = new Image(GuiApplication.class.getResource("Cards/Front/"+cardImg).toExternalForm());
        frontImage.setImage(img);
        img = new Image(GuiApplication.class.getResource("Cards/Back/"+cardImg).toExternalForm());
        backImage.setImage(img);
    }
}
