package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatCommand;
import it.polimi.ingsw.controller.userCommands.ShowFieldCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

/**
 * Controller for the Chat scene allowing player to send and receive messages in a private chat with another player
 * or in the global chat with everyone
 */
public class ChatController extends GuiController {

    @FXML
    private TextField message;
    @FXML
    private TextField recipient;
    @FXML
    private ComboBox<String> chatType;
    @FXML
    private VBox chatView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button closeChat;

    /**
     * Initializes the chat controller
     *
     * @param chatLogs contains all the chat messages that were sent and received after the user joined the game
     */
    public void initialize(List<String> chatLogs) {
        chatView.setSpacing(5);
        genericText.setVisible(false);
        closeChat.setOnMouseClicked(mouseEvent -> ClientController.getInstance().receiveCommand(new ShowFieldCommand()));
        chatType.getItems().addFirst("Global Chat");
        chatType.getItems().add("Private Chat");

        for (String s : chatLogs) {
            updateChat(s);
        }
    }

    /**
     * This method updates the chat displaying the new messages sent by the players
     *
     */
    public void updateChat(String chatMessage) {
        Text mes = new Text(chatMessage);
        mes.setStyle("-fx-font: 15 arial;");
        chatView.getChildren().add(mes);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> scrollPane.setVvalue(1.0)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * This method is used to send a new message in the chat
     */
    @FXML
    private void sendMessage() {
        String chatType = this.chatType.getSelectionModel().getSelectedItem();

        if (message.getCharacters().isEmpty() || chatType == null) {
            return;
        }

        if (chatType.equals("Global Chat")) {
            ClientController.getInstance().receiveCommand(new ChatCommand(true, null, message.getCharacters().toString()));
        } else {
            if (recipient.getCharacters().isEmpty()) {
                return;
            }
            ClientController.getInstance().receiveCommand(new ChatCommand(false, recipient.getCharacters().toString().trim(), message.getCharacters().toString()));
        }

        message.clear();
    }
}
