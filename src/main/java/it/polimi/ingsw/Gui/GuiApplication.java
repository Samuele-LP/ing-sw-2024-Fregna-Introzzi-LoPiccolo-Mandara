package it.polimi.ingsw.Gui;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.controllers.*;
import it.polimi.ingsw.Gui.controllers.field.OpponentFieldController;
import it.polimi.ingsw.Gui.controllers.field.OwnerFieldController;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiApplication extends Application {
    private static  Stage primaryStage;
    private static GuiController currentController = null;
    private static LoadedScene currentScene = null;
    private static boolean canChangeScene = true;
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
        primaryStage.setAlwaysOnTop(ConstantValues.alwaysOnTop);
        primaryStage.setTitle("Codex Naturalis");
    }

    private void setPrimaryStage(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        }
    }

    private void loadPreLobby() {
        if(!canChangeScene){
            return;
        }
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
    public static void loadSideChoice(int cardId){
        if(!canChangeScene){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("SideChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            SideChoiceController controller = loader.getController();
            controller.initialize(cardId);
            currentController = controller;
            currentScene = LoadedScene.SIDE_CHOICE;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadObjectiveChoice(int first, int second){
        if(!canChangeScene){
            return;
        }
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
        if(!canChangeScene){
            return;
        }
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
        if(!canChangeScene){
            return;
        }
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
    public static void loadChat(List<String> chatLogs){
        if(!canChangeScene){
            return;
        }
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
    public static void loadOwnField(List<String> opponentNames,String firstPlayerName, List<Integer> playerHand,
                                    SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                    DeckViewGui resDeck, int[] commonObjs, int secretObj,boolean isPlayerTurn){
        if(!canChangeScene){
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OwnerField.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
            OwnerFieldController controller = loader.getController();
            controller.initialize(opponentNames,firstPlayerName, playerHand, playerField, scoreTrack, goldDeck, resDeck, commonObjs, secretObj,isPlayerTurn);
            currentController = controller;
            currentScene = LoadedScene.OWN_FIELD;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadOppField(List<String> opponentNames,String firstPlayerName,
                                 SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                 DeckViewGui resDeck, int[] commonObjs){
        if(!canChangeScene){
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OpponentField.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
            OpponentFieldController controller = loader.getController();
            controller.initialize(opponentNames,firstPlayerName, playerField, scoreTrack, goldDeck, resDeck, commonObjs);
            currentController = controller;
            currentScene = LoadedScene.OPP_FIELD;
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public static void loadWaitingScreen(){
        if(!canChangeScene){
            return;
        }
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
        if(!canChangeScene){
            return;
        }
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
        if(!canChangeScene){
            return;
        }
        canChangeScene = false;
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("FinalScreen.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            currentScene = LoadedScene.FINAL_SCREEN;
            primaryStage.setScene(newScene);
            ((FinalScreenController) currentController).initialize(finalPlayerScore,winners,disconnection);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadInitialDisconnection() {
        if(!canChangeScene){
            return;
        }
        canChangeScene = false;
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PreGameDisconnection.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            currentScene = LoadedScene.DISCONNECTION;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void loadConnectionRefused(){
        if(!canChangeScene){
            return;
        }
        canChangeScene = false;
        try{
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("ConnectionRefused.fxml"));
            Scene newScene = new Scene(loader.load());
            currentScene = LoadedScene.CONNECTION_REFUSED;
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
