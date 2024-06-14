package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.ChatController;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.Gui.PreLobbyController;
import it.polimi.ingsw.controller.ClientControllerState;
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
    public void display(String s, ClientControllerState state) {
        switch (state){
            case ENDING_CONNECTION -> Platform.runLater(()->((PreLobbyController)GuiApplication.currentController).couldNotConnect(s));
        }
    }

    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        secretObjectiveChoices[0]=firstChoice;
        secretObjectiveChoices[1]=secondChoice;
        Platform.runLater(()->GuiApplication.loadObjectiveChoice(firstChoice,secondChoice));
    }

    @Override
    public void showSecretObjectives() {

    }

    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection) {

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
        /*TODO independently of which field the player is viewing they will receive
        a notification of the turn starting and then it's their choice to switch to their own field
         */
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

    }

    @Override
    public void goToOpponentField(String opponentName) {
        //TODO: change visualised field
    }

    @Override
    public void receivedChat(String s) {
        //TODO: 1)Check if we are currently visualising the chat 2)If not we add a notification , if we are on the chat we call the update method
            Platform.runLater(()->
                    ((ChatController)GuiApplication.currentController).updateChat(s));
    }

    @Override
    public void displayChat(List<String> chatLogs, List<String> playerNames) {
        GuiApplication.loadChat(playerNames,chatLogs);
    }

}
