package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {

    private static  Stage primaryStage;
    private final static ClientController controller=ClientController.getInstance();//creates the ClientController at the start of the program

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        preLobby();
        primaryStage.show();
        primaryStage.setTitle("Codex Naturalis");
        loadObjectiveChoice(92,100);
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
            PreLobbyController preLobbyController = loader.getController();
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
            primaryStage.setScene(newScene);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
