package it.polimi.ingsw.view;

import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.controller.ClientControllerState;

import java.util.List;

public class GameViewGui extends GameView{

    @Override
    public void printCommonField() {

    }

    @Override
    public void printOwnerField() {

    }

    @Override
    public void printOpponentField(String name) {

    }

    /**
     * This method, according to the current state of the controller, decides what scene to display / what text to display on top
     * of the current scene
     */
    @Override
    public void display(String s, ClientControllerState state) {
        switch (state){

        }
    }

    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {

    }

    @Override
    public void showSecretObjectives() {

    }

    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners) {

    }

    @Override
    public void printHand() {

    }

    @Override
    public void printStartingCard() {
        GuiApplication.loadSideChoice(this.startingCardID,true);
    }
}
