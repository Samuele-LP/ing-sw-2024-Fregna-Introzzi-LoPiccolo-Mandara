package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientControllerState;

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

        }
    }

    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        secretObjectiveChoices[0]=firstChoice;
        secretObjectiveChoices[1]=secondChoice;
        GuiApplication.loadObjectiveChoice(firstChoice,secondChoice);
    }

    @Override
    public void showSecretObjectives() {

    }

    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection) {

    }

    @Override
    public void printStartingInfo() {
        GuiApplication.loadSideChoice(this.startingCardID,true);
    }

    /**
     * Displays the necessary interface to choose a name
     */
    @Override
    public void nameChoice() {
        GuiApplication.loadNameChoice();
    }

    @Override
    public void nameNotAvailable(String clientName) {
        GuiApplication.loadNameChoice();//TODO: add parameter to notify that clientName is not available
    }

    @Override
    public void waitingForStart() {
        //TODO: create a waiting scene
    }

    @Override
    public void chooseNumPlayers() {
        GuiApplication.loadNumPlayerChoice();
    }

    @Override
    public void colourChoice(boolean showNotAvailable) {
        //TODO: colour choice
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
        //TODO: maybe add a small notification on the chat button.
    }

    @Override
    public void displayChat(List<String> chatLogs) {
        //TODO: switch to the chat window
    }

}
