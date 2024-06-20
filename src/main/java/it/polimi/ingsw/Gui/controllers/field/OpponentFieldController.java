package it.polimi.ingsw.Gui.controllers.field;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.ChatLogCommand;
import it.polimi.ingsw.controller.userCommands.ShowFieldCommand;
import it.polimi.ingsw.controller.userCommands.ShowOtherFieldCommand;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;

import java.util.List;

public class OpponentFieldController extends FieldController{
    public void initialize(List<String> opponentNames,String firstPlayerName,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs) {

        this.opponentNames = opponentNames;
        this.firstPlayerName = firstPlayerName;
        this.fieldOwner = playerField.getName();

        switchView.getItems().addAll(this.opponentNames);
        switchView.getItems().remove(fieldOwner);
        switchView.getItems().add("Player Chat");
        switchView.getItems().add("Your Field");

        showScoreTrack(scoreTrack);
        showDecks(goldDeck, resDeck,commonObjs);
        showCards(playerField);
        updateVisibleSymbols(playerField.getSymbols());
    }

    @FXML
    private void switchTo() {
        String selected = switchView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        if (opponentNames.contains(selected)){
            ClientController.getInstance().receiveCommand(new ShowOtherFieldCommand(selected));
        }else if(selected.equals("Player Chat")){
            ClientController.getInstance().receiveCommand(new ChatLogCommand());
        }else {
            ClientController.getInstance().receiveCommand(new ShowFieldCommand());
        }
    }

    public void reload(String name) {//If the currently loaded field received an update then it's reloaded
        if(name==null||name.equals(fieldOwner)){
            ClientController.getInstance().receiveCommand(new ShowOtherFieldCommand(name));
        }
    }
}

