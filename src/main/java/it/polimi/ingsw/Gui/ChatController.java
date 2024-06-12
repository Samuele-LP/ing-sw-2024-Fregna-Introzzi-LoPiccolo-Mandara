package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatCommand;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class ChatController {
    @FXML
    TextField message;
    @FXML
    ComboBox<String> recipient;
    @FXML
    VBox chatView;
    @FXML
    ScrollPane scrollPane;
    public void initialize(List<String> players,List<String> chatLogs){
        recipient.getItems().addFirst("Global Chat");
        for(int i=0;i<players.size();i++){
            recipient.getItems().add(i+1,players.get(i));
        }
        for(String s: chatLogs){
            updateChat(s);
        }
    }
    public void updateChat(String chatMessage){
        Text mes = new Text(chatMessage);
        mes.setStyle("-fx-font: 18 arial;");
        chatView.getChildren().add(mes);
        scrollPane.setVvalue(1.0);
    }
    @FXML
    private void sendMessage(){
        String selection = recipient.getSelectionModel().getSelectedItem();
        if(message.getCharacters().isEmpty()||selection==null){
            return;
        }
        if(selection.equals("Global Chat")){
            ClientController.getInstance().receiveCommand(new ChatCommand(true,null,message.getCharacters().toString()));
        }else{
            ClientController.getInstance().receiveCommand(new ChatCommand(false,selection,message.getCharacters().toString()));
        }
        message.clear();
    }
}
