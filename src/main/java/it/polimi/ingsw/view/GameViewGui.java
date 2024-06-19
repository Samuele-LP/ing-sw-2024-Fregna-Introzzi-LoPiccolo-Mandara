package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.*;
import it.polimi.ingsw.Gui.controllers.ChatController;
import it.polimi.ingsw.Gui.controllers.field.OpponentFieldController;
import it.polimi.ingsw.Gui.controllers.field.OwnerFieldController;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.Field.PlayerFieldViewGui;
import javafx.application.Platform;

import java.util.List;

public class GameViewGui extends GameView {

    @Override
    public void opponentMadeAMove(String name) {
        if (GuiApplication.getCurrentScene().equals(LoadedScene.OPP_FIELD)) {
            OpponentFieldController controller = (OpponentFieldController) GuiApplication.getCurrentController();
            controller.reload(name);
        }
    }

    /**
     * This method, according to the current state of the controller, decides what text to display on top
     * of the current scene
     */
    @Override
    public void display(String s) {
        Platform.runLater(() -> GuiApplication.getCurrentController().displayText(s, 1500));
    }

    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        secretObjectiveChoices[0] = firstChoice;
        secretObjectiveChoices[1] = secondChoice;
    }

    @Override
    public void showSecretObjectives() {
        Platform.runLater(() -> GuiApplication.loadObjectiveChoice(secretObjectiveChoices[0], secretObjectiveChoices[1]));
    }

    @Override
    public void printStartingInfo() {
        Platform.runLater(() -> GuiApplication.loadSideChoice(this.startingCardID));
    }

    /**
     * Displays the necessary interface to choose a name
     */
    @Override
    public void nameChoice() {
        Platform.runLater(() -> GuiApplication.loadNameChoice(null));
    }

    @Override
    public void nameNotAvailable(String previousName) {
        Platform.runLater(() -> GuiApplication.loadNameChoice(previousName));
    }

    @Override
    public void waitingForStart() {
        Platform.runLater(GuiApplication::loadWaitingScreen);
    }

    @Override
    public void chooseNumPlayers() {
        Platform.runLater(GuiApplication::loadNumPlayerChoice);
    }

    @Override
    public void colourChoice(boolean showNotAvailable) {
        Platform.runLater(() -> GuiApplication.loadColourChoice(showNotAvailable));
    }

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

    @Override
    public void receivedDrawnCard() {
        this.goToOwnerField();
    }

    @Override
    public void sharedFieldUpdate() {
        if(commonObjectives[0]<=0){
            return;
        }
        if (GuiApplication.getCurrentScene().equals(LoadedScene.OPP_FIELD)) {
            Platform.runLater(() -> ((OpponentFieldController) GuiApplication.getCurrentController()).reload(null));
        } else if (GuiApplication.getCurrentScene().equals(LoadedScene.OWN_FIELD)) {
            this.goToOwnerField();
        }
    }

    @Override
    public void goToOwnerField() {
        Platform.runLater(() -> GuiApplication.loadOwnField(this.getOpponentNames(),firstPlayerName, this.getPlayerHand(),
                ((PlayerFieldViewGui) ownerField).getAsSimpleField(), scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                commonObjectives, secretObjectiveChoices[0], false)
        );
    }

    @Override
    public void goToOpponentField(String opponentName) {
        Platform.runLater(() -> GuiApplication.loadOppField(this.getOpponentNames(),firstPlayerName, ((PlayerFieldViewGui) opponentFields.get(opponentName)).getAsSimpleField(),
                scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck, commonObjectives));
    }

    @Override
    public void receivedChat(String s) {
        if (LoadedScene.CHAT.equals(GuiApplication.getCurrentScene())) {
            Platform.runLater(() ->
                    ((ChatController) GuiApplication.getCurrentController()).updateChat(s));
        } else {
            Platform.runLater(() -> GuiApplication.getCurrentController().displayText("You received a chat message!", 500));
        }
    }

    @Override
    public void displayChat(List<String> chatLogs, List<String> playerNames) {
        Platform.runLater(() -> GuiApplication.loadChat(playerNames, chatLogs));
    }

    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners, boolean disconnection) {
        Platform.runLater(() -> GuiApplication.loadFinalScreen(finalPlayerScore, winners, disconnection));
    }

    @Override
    public void disconnection() {
       Platform.runLater(GuiApplication::loadInitialDisconnection);
    }

}
