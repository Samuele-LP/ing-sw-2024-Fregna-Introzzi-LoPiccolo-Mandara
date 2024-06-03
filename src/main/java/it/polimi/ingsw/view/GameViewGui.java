package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientControllerState;

import java.io.IOException;
import java.util.List;

public class GameViewGui extends GameView{
    @Override
    public void gameStarting(List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective, String firstPlayerName) throws IOException {

    }

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
     * Method that prints a message as either CLI or GUI according to how the program was started
     */
    @Override
    public void showText(String s, ClientControllerState state) {

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

    }
}
