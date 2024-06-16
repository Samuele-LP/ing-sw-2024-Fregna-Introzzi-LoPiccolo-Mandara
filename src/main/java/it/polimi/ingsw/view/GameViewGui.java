package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.*;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.Field.PlayerFieldViewCli;
import it.polimi.ingsw.view.Field.PlayerFieldViewGui;
import javafx.application.Platform;

import java.util.List;

public class GameViewGui extends GameView{

    @Override
    public void opponentMadeAMove(String name) {
        //TODO: update with the new card and show a notification
    }

    /**
     * This method, according to the current state of the controller, decides what text to display on top
     * of the current scene
     */
    @Override
    public void display(String s) {
        Platform.runLater(()-> GuiController.loadPopUp(s,1500));
    }

    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        secretObjectiveChoices[0]=firstChoice;
        secretObjectiveChoices[1]=secondChoice;
    }

    @Override
    public void showSecretObjectives() {
        Platform.runLater(()->GuiApplication.loadObjectiveChoice(secretObjectiveChoices[0], secretObjectiveChoices[1]));
    }

    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection) {
        Platform.runLater(()->GuiApplication.loadFinalScreen(finalPlayerScore,winners,disconnection));
    }

    @Override
    public void printStartingInfo() {
       Platform.runLater( ()->GuiApplication.loadSideChoice(this.startingCardID,true));
    }

    /**
     * Displays the necessary interface to choose a name
     */
    @Override
    public void nameChoice() {
        Platform.runLater(()->GuiApplication.loadNameChoice(null));
    }

    @Override
    public void nameNotAvailable(String previousName) {
        Platform.runLater(()->GuiApplication.loadNameChoice(previousName));    }

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
        Platform.runLater(()->GuiApplication.loadColourChoice(showNotAvailable));
    }

    @Override
    public void placingACard() {
        //TODO: add parameters to determine that it's in fact the player's turn
        Platform.runLater(()->GuiApplication.loadOwnField(this.playerName,this.getOpponentNames(),this.getPlayerHand(),
                ((PlayerFieldViewGui)ownerField).getAsSimpleField(),scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                commonObjectives,secretObjectiveChoices[0])
        );
    }

    @Override
    public void successfulPlacement(boolean initialPhase) {
        //TODO: update the gui with the new card
        if(!initialPhase){
            //Display message to notify that the player can now draw
        }

    }

    @Override
    public void receivedDrawnCard() {
        //TODO:update the visualization of the player's hand
    }

    @Override
    public void sharedFieldUpdate() {
        //TODO: update the visualization of the common field
    }
    @Override
    public void goToOwnerField(){
        Platform.runLater(()->GuiApplication.loadOwnField(this.playerName,this.getOpponentNames(),this.getPlayerHand(),
                ((PlayerFieldViewGui)ownerField).getAsSimpleField(),scoreTrack, (DeckViewGui) goldDeck, (DeckViewGui) resourceDeck,
                commonObjectives,secretObjectiveChoices[0])
        );
    }

    @Override
    public void goToOpponentField(String opponentName) {
        //TODO: change visualised field
    }

    @Override
    public void receivedChat(String s) {
            if(LoadedScene.CHAT.equals(GuiApplication.getCurrentScene())) {
                Platform.runLater(() ->
                        ((ChatController) GuiApplication.getCurrentController()).updateChat(s));
            }else{
               Platform.runLater(()-> GuiController.loadPopUp("You received a chat message!",500));
            }
    }

    @Override
    public void displayChat(List<String> chatLogs, List<String> playerNames) {
        GuiApplication.loadChat(playerNames,chatLogs);
    }

}
