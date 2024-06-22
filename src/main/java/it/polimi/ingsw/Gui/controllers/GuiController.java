package it.polimi.ingsw.Gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Abstract base controller class for GUI controllers in the application.
 */
public abstract class GuiController {

    @FXML
    protected Text genericText;

    /**
     * Displays a pop-up with the specified message for a given duration.
     *
     * @param message  the message to display
     * @param duration the duration to display the message, in milliseconds
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
