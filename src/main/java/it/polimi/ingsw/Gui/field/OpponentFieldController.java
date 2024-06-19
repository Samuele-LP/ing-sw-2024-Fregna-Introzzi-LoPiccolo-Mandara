package it.polimi.ingsw.Gui.field;

import it.polimi.ingsw.Gui.GuiController;
import it.polimi.ingsw.Gui.field.FieldController;
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
    private String fieldOwner;
    public void initialize(List<String> opponentNames,String firstPlayerName,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs) {

        this.opponentNames = opponentNames;
        this.firstPlayerName = firstPlayerName;
        this.fieldOwner = playerField.getName();

        showScoreTrack(scoreTrack);

        switchView.getItems().addAll(this.opponentNames);
        switchView.getItems().remove(fieldOwner);
        switchView.getItems().add("Player Chat");
        switchView.getItems().add("Your Field");

        firstObj.setImage((getCardImage(commonObjs[0], true)));
        secondObj.setImage((getCardImage(commonObjs[1], true)));
        objTop.setImage(getCardImage(99, false));

        showDecks(goldDeck, resDeck);
        showCards(playerField);

        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
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

