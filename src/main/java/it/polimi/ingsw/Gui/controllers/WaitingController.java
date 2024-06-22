package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatLogCommand;
import javafx.fxml.FXML;

/**
 * Controller class for handling actions in the waiting screen.
 */
public class WaitingController extends GuiController {

    /**
     * Handles the action of going to the chat.
     * Sends a ChatLogCommand to the ClientController.
     */
    @FXML
    private void goToChat(){
        ClientController.getInstance().receiveCommand(new ChatLogCommand());
    }
}
