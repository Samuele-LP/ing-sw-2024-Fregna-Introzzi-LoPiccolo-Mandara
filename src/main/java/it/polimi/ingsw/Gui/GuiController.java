package it.polimi.ingsw.Gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public interface GuiController {
    /**
     * Displays a popUp with the input string as its text
     * @param duration duration of the popUp milliseconds
     */
    static void loadPopUp(String message,int duration){
        Stage popUp = new Stage();
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PopUp.fxml"));
        try {
            popUp.setScene(new Scene(loader.load()));
        }catch (IOException e){
            throw new RuntimeException();
        }
        ((PopUpController)loader.getController()).initialize(message);
        popUp.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration), e -> popUp.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
