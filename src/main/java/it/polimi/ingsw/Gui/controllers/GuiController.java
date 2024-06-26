package it.polimi.ingsw.Gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
     */
    public void displayText(String message){
        HBox.setHgrow(genericText, Priority.ALWAYS);
        VBox.setVgrow(genericText, Priority.ALWAYS);

        genericText.setVisible(true);
        genericText.setText(message);
    }
}
