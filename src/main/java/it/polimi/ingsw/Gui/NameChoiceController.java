package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.NameCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class NameChoiceController implements GuiController {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label waitLabel;

    @FXML
    private ImageView waitingImageView;

    @FXML
    private Label chatLabel;

    @FXML
    private Button chatButton;

    @FXML
    private Label errorMessage;

    @FXML
    public void initialize(String previousName) {
        if(previousName!=null){
            errorText(previousName+" was already chosen by someone!");
        }
        sendButton.setOnMouseClicked(event ->{
            String name = nameTextField.getText().trim();
            if (name.isEmpty()) {
                errorText("Please enter your name.");
            }else if(name.contains(" ")){
                errorText("Your name cannot have spaces in it!");
            }
            else {
                errorMessage.setVisible(false);
                ClientController.getInstance().receiveCommand(new NameCommand(name));
            }
        });
    }

    private void errorText(String txt) {
        errorMessage.setText(txt);
        errorMessage.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> errorMessage.setVisible(false)));
        timeline.play();
    }
}
