package it.polimi.ingsw.Gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class PopUpController implements GuiController {
    @FXML
    private Label text;
    @FXML
    private Button close;
    public void initialize(String s){
        close.setOnMouseClicked(event->{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        text.setText(s);
    }

}
