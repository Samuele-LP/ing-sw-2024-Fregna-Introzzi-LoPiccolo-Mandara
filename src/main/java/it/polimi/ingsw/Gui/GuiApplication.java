package it.polimi.ingsw.Gui;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiApplication extends Application {
    private static  Stage primaryStage;
    public static GuiController currentController = null;
    public static void main(String[] args) {
        launch();
        System.exit(1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        preLobby();
        primaryStage.show();
        primaryStage.setTitle("Codex Naturalis");
    }

    private void setPrimaryStage(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        }
    }
    private void preLobby() {
        primaryStage.setResizable(false); // to set a static window

        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PreLobby.fxml"));
        try {
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
            currentController = loader.<PreLobbyController>getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void loadSideChoice(int cardId,boolean isStartingCard){
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("SideChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            SideChoiceController controller = loader.getController();
            controller.initialize(cardId,isStartingCard);
            currentController = controller;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadObjectiveChoice(int first, int second){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("SecretObjectiveChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            SecretObjectiveController controller = loader.getController();
            controller.initialize(first,second);
            currentController = controller;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadNameChoice(){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("NameChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            NameChoiceController controller = loader.getController();
            controller.initialize();
            currentController = controller;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadNumPlayerChoice(){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("NumPlayerChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadChat(List<String> players, List<String> chatLogs){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("Chat.fxml"));
            Scene newScene = new Scene(loader.load());
            ChatController controller = loader.getController();
            controller.initialize(chatLogs);
            currentController = controller;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadField(String fieldOwner, String playerName, List<String> opponentNames, List<Integer> playerHand,
                                 SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                 DeckViewGui resDeck, int[] commonObjs, int secrObj){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OwnerField.fxml"));
            Scene newScene = new Scene(loader.load());
            OwnerFieldController controller = loader.getController();
            controller.initialize(fieldOwner, playerName, opponentNames, playerHand, playerField, scoreTrack, goldDeck, resDeck, commonObjs, secrObj);
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public static void loadOppField(String fieldOwner, String playerName, List<String> opponentNames,
                                 SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                 DeckViewGui resDeck, int[] commonObjs){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OpponentField.fxml"));
            Scene newScene = new Scene(loader.load());
            OpponentFieldController controller = loader.getController();
            controller.initialize(fieldOwner, playerName, opponentNames, playerField, scoreTrack, goldDeck, resDeck, commonObjs);
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadWaitingScreen(){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("Waiting.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadFinalScreen(){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("FinalScreen.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
