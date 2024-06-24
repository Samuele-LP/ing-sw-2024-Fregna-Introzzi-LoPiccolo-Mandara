package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.Gui.LoadedScene;
import it.polimi.ingsw.Gui.controllers.ChatController;
import it.polimi.ingsw.Gui.controllers.field.OpponentFieldController;
import it.polimi.ingsw.Gui.controllers.field.OwnerFieldController;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.Field.PlayerFieldViewGui;
import javafx.application.Platform;

import java.util.List;

/**
 * Class representing the game view in a graphical user interface.
 */
public class GameViewGui extends GameView {

    /**
     * Updates the view when an opponent makes a move.
     *
     * @param name the name of the opponent who made a move
     */
    @Override
    public void opponentMadeAMove(String name) {
        if (GuiApplication.getCurrentScene().equals(LoadedScene.OPP_FIELD)) {
            OpponentFieldController controller = (OpponentFieldController) GuiApplication.getCurrentController();
            controller.reload(name);
        }
    }

    /**
     * Displays a message on the current GUI scene.
     *
     * @param s the message to be displayed
     */
    @Override
    public void display(String s) {
        Platform.runLater(() -> GuiApplication.getCurrentController().displayText(s, 1500));
    }

    /**
     * Sets the choices for the secret objective.
     *
     * @param firstChoice  the first secret objective choice
     * @param secondChoice the second secret objective choice
     */
    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        secretObjectiveChoices[0] = firstChoice;
        secretObjectiveChoices[1] = secondChoice;
    }

    /**
     * Displays the secret objectives for the player to choose from.
     */
    @Override
    public void showSecretObjectives() {
        Platform.runLater(() -> GuiApplication.loadObjectiveChoice(secretObjectiveChoices[0], secretObjectiveChoices[1]));
    }

    /**
     * Displays the starting card information for the player.
     */
    @Override
    public void printStartingInfo() {
        Platform.runLater(() -> GuiApplication.loadSideChoice(this.startingCardID));
    }

    /**
     * Displays the interface for the player to choose a name.
     */
    @Override
    public void nameChoice() {
        Platform.runLater(() -> GuiApplication.loadNameChoice(null));
    }

    /**
     * Informs the player that their chosen name is not available and prompts them to choose another.
     *
     * @param previousName the previously chosen name that is not available
     */
    @Override
    public void nameNotAvailable(String previousName) {
        Platform.runLater(() -> GuiApplication.loadNameChoice(previousName));
    }

    /**
     * Displays the waiting screen for the player.
     */
    @Override
    public void waitingForStart() {
        Platform.runLater(GuiApplication::loadWaitingScreen);
    }

    /**
     * Displays the interface for choosing the number of players.
     */
    @Override
    public void chooseNumPlayers() {
        Platform.runLater(GuiApplication::loadNumPlayerChoice);
    }

    /**
     * Displays the interface for choosing a color for the player's pawn.
     *
     * @param showNotAvailable whether to show that a previously chosen color is not available
     */
    @Override
    public void colourChoice(boolean showNotAvailable) {
        Platform.runLater(() -> GuiApplication.loadColourChoice(showNotAvailable));
    }

    /**
     * Loads the interface for placing a card during the player's turn.
     */
    @Override
    public void placingACard() {
        Platform.runLater(() -> {
                    GuiApplication.loadOwnField(this.getOpponentNames(), firstPlayerName, this.getPlayerHand(),
                            ((PlayerFieldViewGui) ownerField).getAsSimpleField(), scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                            commonObjectives, secretObjectiveChoices[0], true);
                    GuiApplication.getCurrentController().displayText("It's your turn!", 2000);
                }
        );
    }

    /**
     * Loads the interface for drawing a card during the player's turn.
     *
     * @param initialPhase whether it is the initial phase of the game
     */
    @Override
    public void drawingACard(boolean initialPhase) {
        Platform.runLater(() -> {
                    GuiApplication.loadOwnField(this.getOpponentNames(), firstPlayerName,this.getPlayerHand(),
                            ((PlayerFieldViewGui) ownerField).getAsSimpleField(), scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                            commonObjectives, secretObjectiveChoices[0], false);
                    OwnerFieldController controller = (OwnerFieldController) GuiApplication.getCurrentController();
                    controller.drawingPhase();
                }
        );
    }

    /**
     * Updates the view after a card has been drawn by the player.
     */
    @Override
    public void receivedDrawnCard() {
        this.goToOwnerField();
    }

    /**
     * Updates the shared field view.
     */
    @Override
    public void sharedFieldUpdate() {
        if (commonObjectives[0] <= 0) {
            return;
        }

        if (GuiApplication.getCurrentScene().equals(LoadedScene.OPP_FIELD)) {
            Platform.runLater(() -> ((OpponentFieldController) GuiApplication.getCurrentController()).reload(null));
        } else if (GuiApplication.getCurrentScene().equals(LoadedScene.OWN_FIELD)) {
            this.goToOwnerField();
        }
    }

    /**
     * Loads the player's own field view.
     */
    @Override
    public void goToOwnerField() {
        Platform.runLater(() -> GuiApplication.loadOwnField(this.getOpponentNames(),firstPlayerName, this.getPlayerHand(),
                ((PlayerFieldViewGui) ownerField).getAsSimpleField(), scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                commonObjectives, secretObjectiveChoices[0], false)
        );
    }

    /**
     * Loads the opponent's field view.
     *
     * @param opponentName the name of the opponent whose field is to be shown
     */
    @Override
    public void goToOpponentField(String opponentName) {
        Platform.runLater(() -> GuiApplication.loadOppField(this.getOpponentNames(),firstPlayerName, ((PlayerFieldViewGui) opponentFields.get(opponentName)).getAsSimpleField(),
                scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck, commonObjectives));
    }

    /**
     * Updates the chat with a received message.
     *
     * @param s the received chat message
     */
    @Override
    public void receivedChat(String s) {
        if (LoadedScene.CHAT.equals(GuiApplication.getCurrentScene())) {
            Platform.runLater(() ->
                    ((ChatController) GuiApplication.getCurrentController()).updateChat(s));
        } else {
            Platform.runLater(() -> GuiApplication.getCurrentController().displayText("You received a chat message!", 500));
        }
    }

    /**
     * Displays the chat history.
     *
     * @param chatLogs the chat history logs
     */
    @Override
    public void displayChat(List<String> chatLogs) {
        Platform.runLater(() -> GuiApplication.loadChat(chatLogs));
    }

    /**
     * Displays the final leaderboard and the winners of the game.
     *
     * @param finalPlayerScore the final score track of the players
     * @param winners          the list of winners
     * @param disconnection    whether the game ended due to a disconnection
     */
    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners, boolean disconnection) {
        Platform.runLater(() -> GuiApplication.loadFinalScreen(finalPlayerScore, winners, disconnection));
    }

    /**
     * Displays the disconnection screen.
     */
    @Override
    public void disconnection() {
       Platform.runLater(GuiApplication :: loadInitialDisconnection);
    }

    /**
     * Displays the connection refused screen.
     */
    @Override
    public void connectionRefused() {
        Platform.runLater(GuiApplication :: loadConnectionRefused);
    }
}
