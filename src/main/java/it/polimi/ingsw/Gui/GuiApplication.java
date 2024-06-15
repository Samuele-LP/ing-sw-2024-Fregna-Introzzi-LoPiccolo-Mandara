package it.polimi.ingsw.Gui;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GuiApplication extends Application {
    private static  Stage primaryStage;
    private static GuiController currentController = null;
    private static LoadedScene currentScene = null;
    public static void main(String[] args) {
        launch();
        System.exit(1);
    }
    public static GuiController getCurrentController(){
        return currentController;
    }
    public static LoadedScene getCurrentScene(){
        return currentScene;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        loadPreLobby();
        primaryStage.show();
        primaryStage.setTitle("Codex Naturalis");
    }

    private void setPrimaryStage(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        }
    }
    private void loadPreLobby() {
        primaryStage.setResizable(false); // to set a static window

        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PreLobby.fxml"));
        try {
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
            currentController = loader.getController();
            currentScene = LoadedScene.PRE_LOBBY;
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
            currentScene = LoadedScene.SIDE_CHOICE;
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
            currentScene = LoadedScene.SECRET_CHOICE;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadNameChoice(String previousName){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("NameChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            NameChoiceController controller = loader.getController();
            controller.initialize(previousName);
            currentController = controller;
            currentScene = LoadedScene.NAME_CHOICE;
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
            currentScene = LoadedScene.NUM_CHOICE;
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
            currentScene = LoadedScene.CHAT;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadOwnField(String fieldOwner, String playerName, List<String> opponentNames, List<Integer> playerHand,
                                    SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                    DeckViewGui resDeck, int[] commonObjs, int secrObj){//TODO: change some parameters
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OwnerField.fxml"));
            Scene newScene = new Scene(loader.load());
            OwnerFieldController controller = loader.getController();
            controller.initialize(fieldOwner, playerName, opponentNames, playerHand, playerField, scoreTrack, goldDeck, resDeck, commonObjs, secrObj);
            currentController = controller;
            currentScene = LoadedScene.OWN_FIELD;
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
            currentController = controller;
            currentScene = LoadedScene.OPP_FIELD;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadWaitingScreen(){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("Waiting.fxml"));
            Scene newScene = new Scene(loader.load());
            currentScene = LoadedScene.WAITING;
            currentController = loader.getController();
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadColourChoice(boolean errorMessage){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("ColourChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            ColourChoiceController controller = loader.getController();
            controller.initialize(errorMessage);
            currentController = controller;
            currentScene = LoadedScene.COLOUR_CHOICE;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadFinalScreen(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection){
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("FinalScreen.fxml"));
            Scene newScene = new Scene(loader.load());
            //currentController = loader.getController();
            currentScene = LoadedScene.FINAL_SCREEN;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
