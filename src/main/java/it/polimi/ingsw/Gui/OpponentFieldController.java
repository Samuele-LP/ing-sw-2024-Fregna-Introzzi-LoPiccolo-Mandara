package it.polimi.ingsw.Gui;

import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.ClientControllerState;
import it.polimi.ingsw.controller.userCommands.ChatLogCommand;
import it.polimi.ingsw.controller.userCommands.ShowFieldCommand;
import it.polimi.ingsw.controller.userCommands.ShowOtherFieldCommand;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class OpponentFieldController {
    @FXML AnchorPane field;
    @FXML ComboBox<String> switchView;
    @FXML Button switchButton;
    @FXML ImageView goldTop;
    @FXML ImageView firstGold;
    @FXML ImageView secondGold;
    @FXML ImageView resourceTop;
    @FXML ImageView firstResource;
    @FXML ImageView secondResource;
    @FXML ImageView objTop;
    @FXML ImageView firstObj;
    @FXML ImageView secondObj;
    List<String> opponentNames;
    String fieldOwner, playerName;
    public void displayText(String s, ClientControllerState state){
        if(state.equals(ClientControllerState.REQUESTING_PLACEMENT)){

        }else if(state.equals(ClientControllerState.REQUESTING_DRAW_CARD)){

        }
    }
    public void initialize(String fieldOwner, String playerName, List<String> opponentNames,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs) {
        objTop.setImage(getCardImage(99,false));
        this.opponentNames = opponentNames;
        this.fieldOwner = fieldOwner;
        this.playerName = playerName;
        switchView.getItems().addAll(this.opponentNames);
        switchView.getItems().add("Player Chat");
        if(!fieldOwner.equals(playerName)){
            switchView.getItems().remove(fieldOwner);
            switchView.getItems().add("Your Field");
        }

        if(goldDeck.getTopColour()!=null){
            goldTop.setImage(getCardImage(goldDeck.getTopColour()== CardType.fungi?
                    41:(goldDeck.getTopColour()==CardType.plant?51:(goldDeck.getTopColour()==CardType.animal?61:7-1)),false));
        }
        if(resDeck.getTopColour()!=null){
            resourceTop.setImage(getCardImage(goldDeck.getTopColour()== CardType.fungi?1:(goldDeck.getTopColour()==CardType.plant?11
                    :(goldDeck.getTopColour()==CardType.animal?21:31)),false));
        }
        if(resDeck.getFirstVisible()!=null){
            firstResource.setImage(getCardImage(resDeck.getFirstVisible(),true));
        }
        if(resDeck.getSecondVisible()!=null){
            secondResource.setImage(getCardImage(resDeck.getSecondVisible(),true));
        }
        if(goldDeck.getFirstVisible()!=null){
            firstGold.setImage(getCardImage(goldDeck.getFirstVisible(),true));
        }
        if(goldDeck.getFirstVisible()!=null){
            secondGold.setImage(getCardImage(goldDeck.getSecondVisible(),true));
        }
        firstObj.setImage((getCardImage(commonObjs[0],true)));
        secondObj.setImage((getCardImage(commonObjs[1],true)));
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

    private Image getCardImage(int cardID, boolean top) {
        if(top) return new Image(GuiApplication.class.getResource("Cards/Front/" + cardID + ".png").toExternalForm());
        else return new Image(GuiApplication.class.getResource("Cards/Back/" + cardID + ".png").toExternalForm());
    }
}

