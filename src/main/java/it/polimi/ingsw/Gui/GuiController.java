package it.polimi.ingsw.Gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public abstract class GuiController {
    @FXML
    protected Text genericText;
    /**
     * Displays a popUp with the input string as its text
     * @param duration duration of the popUp milliseconds
     */
    public void displayText(String message, int duration){
        HBox.setHgrow(genericText, Priority.ALWAYS);
        VBox.setVgrow(genericText, Priority.ALWAYS);

        genericText.setVisible(true);
        genericText.setText(message);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration), e -> genericText.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
