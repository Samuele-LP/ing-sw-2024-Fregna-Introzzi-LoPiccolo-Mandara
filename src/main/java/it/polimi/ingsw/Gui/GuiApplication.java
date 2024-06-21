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

/**
 * This class contains all the methods needed to load all the scenes of this Gui app
 */
public class GuiApplication extends Application {
    private static Stage primaryStage;
    private static GuiController currentController = null;
    private static LoadedScene currentScene = null;
    private static boolean canChangeScene = true;

    public static void main(String[] args) {
        launch();
        System.exit(1);
    }

    public static GuiController getCurrentController() {
        return currentController;
    }

    public static LoadedScene getCurrentScene() {
        return currentScene;
    }

    /**
     * This method starts the Gui and loads PreLobby scene
     *
     * @param primaryStage
     * @throws Exception
     */
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

    /**
     * This loads the initial screen in which the client has to insert the ip to connect to the server
     */
    private void loadPreLobby() {
        if (!canChangeScene) {
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

    /**
     * This loads the SideChoice scene in which the player has to choose the startingCard side
     *
     * @param cardId is the id of the starting card which is assigned automatically
     */
    public static void loadSideChoice(int cardId) {
        if (!canChangeScene) {
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

    /**
     * This loads the SecretObjectiveChoice to every player who connected successfully
     *
     * @param first  is the first objectiveCard choice
     * @param second is the second objectiveCard
     */
    public static void loadObjectiveChoice(int first, int second) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("SecretObjectiveChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            SecretObjectiveController controller = loader.getController();
            controller.initialize(first, second);
            currentController = controller;
            currentScene = LoadedScene.SECRET_CHOICE;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This loads the NameChoice scene right after the connection is accepted
     *
     * @param previousName
     */
    public static void loadNameChoice(String previousName) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("NameChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            NameChoiceController controller = loader.getController();
            controller.initialize(previousName);
            currentController = controller;
            currentScene = LoadedScene.NAME_CHOICE;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This loads the NumPlayerChoice to the first player who connected exclusively who has to choose how many players
     * will play in this lobby
     */
    public static void loadNumPlayerChoice() {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("NumPlayerChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            currentScene = LoadedScene.NUM_CHOICE;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method loads the Chat scene when a player wants to send a message to one or all the players
     *
     * @param chatLogs
     */
    public static void loadChat(List<String> chatLogs) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("Chat.fxml"));
            Scene newScene = new Scene(loader.load());
            ChatController controller = loader.getController();
            controller.initialize(chatLogs);
            currentController = controller;
            currentScene = LoadedScene.CHAT;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method loads the OwnerField scene
     *
     * @param opponentNames   contains the opponents names
     * @param firstPlayerName is the first player name in the game order
     * @param playerHand      contains the three playable cards in the player's hand
     * @param playerField     contains the infos in the cards that the player placed
     * @param scoreTrack
     * @param goldDeck
     * @param resDeck
     * @param commonObjs
     * @param secretObj
     * @param isPlayerTurn    is true if the player has to place or draw, false otherwise
     */
    public static void loadOwnField(List<String> opponentNames, String firstPlayerName, List<Integer> playerHand,
                                    SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                    DeckViewGui resDeck, int[] commonObjs, int secretObj, boolean isPlayerTurn) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OwnerField.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
            OwnerFieldController controller = loader.getController();
            controller.initialize(opponentNames, firstPlayerName, playerHand, playerField, scoreTrack, goldDeck, resDeck, commonObjs, secretObj, isPlayerTurn);
            currentController = controller;
            currentScene = LoadedScene.OWN_FIELD;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This loads the OpponentField when a player wants to visit another player's field
     *
     * @param opponentNames   contains the names of the opponents
     * @param firstPlayerName is the first player name in the game order
     * @param playerField     contains the player's field infos
     * @param scoreTrack
     * @param goldDeck
     * @param resDeck
     * @param commonObjs
     */
    public static void loadOppField(List<String> opponentNames, String firstPlayerName,
                                    SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                                    DeckViewGui resDeck, int[] commonObjs) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("OpponentField.fxml"));
            Scene newScene = new Scene(loader.load());
            primaryStage.setScene(newScene);
            OpponentFieldController controller = loader.getController();
            controller.initialize(opponentNames, firstPlayerName, playerField, scoreTrack, goldDeck, resDeck, commonObjs);
            currentController = controller;
            currentScene = LoadedScene.OPP_FIELD;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This loads the Waiting scene when the player has to wait his turn
     */
    public static void loadWaitingScreen() {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("Waiting.fxml"));
            Scene newScene = new Scene(loader.load());
            currentScene = LoadedScene.WAITING;
            currentController = loader.getController();
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This loads the ColourChoice scene when the player has to choose his pawn colour in the pre game
     *
     * @param errorMessage
     */
    public static void loadColourChoice(boolean errorMessage) {
        if (!canChangeScene) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("ColourChoice.fxml"));
            Scene newScene = new Scene(loader.load());
            ColourChoiceController controller = loader.getController();
            controller.initialize(errorMessage);
            currentController = controller;
            currentScene = LoadedScene.COLOUR_CHOICE;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method loads the FinalScreen scene when the game ends
     *
     * @param finalPlayerScore contains the player points when the game ends
     * @param winners          contains the name of the players who won
     * @param disconnection    true is the game is ended by a disconnection, false otherwise
     */
    public static void loadFinalScreen(ImmutableScoreTrack finalPlayerScore, List<String> winners, boolean disconnection) {
        if (!canChangeScene) {
            return;
        }
        canChangeScene = false;
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("FinalScreen.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            currentScene = LoadedScene.FINAL_SCREEN;
            primaryStage.setScene(newScene);
            ((FinalScreenController) currentController).initialize(finalPlayerScore, winners, disconnection);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method loads the PreGameDisconnection scene when a client disconnects in the initial phase of the game
     */
    public static void loadInitialDisconnection() {
        if (!canChangeScene) {
            return;
        }
        canChangeScene = false;
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("PreGameDisconnection.fxml"));
            Scene newScene = new Scene(loader.load());
            currentController = loader.getController();
            currentScene = LoadedScene.DISCONNECTION;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method loads the ConnectionRefused when the connection is refused by the server
     */
    public static void loadConnectionRefused() {
        if (!canChangeScene) {
            return;
        }
        canChangeScene = false;
        try {
            FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("ConnectionRefused.fxml"));
            Scene newScene = new Scene(loader.load());
            currentScene = LoadedScene.CONNECTION_REFUSED;
            primaryStage.setScene(newScene);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
