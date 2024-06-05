package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.GameViewGui;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {

    private static  Stage primaryStage;
    private static GameViewGui view;
    private static ClientController controller;

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
            PreLobbyController preLobbyController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        view = new GameViewGui();
        launch();
    }
}
