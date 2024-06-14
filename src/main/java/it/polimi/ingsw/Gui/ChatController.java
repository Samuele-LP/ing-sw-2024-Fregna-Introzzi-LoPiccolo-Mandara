package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatCommand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class ChatController implements GuiController {
    @FXML
    TextField message;
    @FXML
    TextField recipient;
    @FXML
    ComboBox<String> chatType;
    @FXML
    VBox chatView;
    @FXML
    ScrollPane scrollPane;
    public void initialize(List<String> chatLogs){
        chatType.getItems().addFirst("Global Chat");
        chatType.getItems().add("Private Chat");
        for(String s: chatLogs){
            updateChat(s);
        }
    }
    public void updateChat(String chatMessage){
        Text mes = new Text(chatMessage);
        mes.setStyle("-fx-font: 15 arial;");
        chatView.getChildren().add(mes);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> scrollPane.setVvalue(1.0)));
        timeline.setCycleCount(1);
        timeline.play();
    }
    @FXML
    private void sendMessage(){
        String chatType = this.chatType.getSelectionModel().getSelectedItem();
        if(message.getCharacters().isEmpty()||chatType==null){
            return;
        }
        if(chatType.equals("Global Chat")){
            ClientController.getInstance().receiveCommand(new ChatCommand(true,null,message.getCharacters().toString()));
        }else{
            if(recipient.getCharacters().isEmpty()){
                return;
            }
            ClientController.getInstance().receiveCommand(new ChatCommand(false,recipient.getCharacters().toString().trim(),message.getCharacters().toString()));
        }
        message.clear();
    }
}
