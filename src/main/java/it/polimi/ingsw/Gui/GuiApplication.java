package it.polimi.ingsw.Gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.GameViewGui;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

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
        Parent root = null;
        try {
            System.out.println("Classpath: " + System.getProperty("java.class.path"));
            URL fxmlResource = getClass().getResource("PreLobby.fxml");
            System.out.println("FXML Resource: " + fxmlResource);
            root = FXMLLoader.load(Objects.requireNonNull(fxmlResource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


        //FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PreLobby.fxml"));
        /*try {
            primaryStage.setScene(new Scene(loader.load(), 700, 700, false, SceneAntialiasing.BALANCED));
            primaryStage.show();
            //PreLobbyController preLobbyController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static void main(String[] args) {
        //controller = new ClientController(); todo set public
        //view = new GameViewGui();
        launch();
    }
}
