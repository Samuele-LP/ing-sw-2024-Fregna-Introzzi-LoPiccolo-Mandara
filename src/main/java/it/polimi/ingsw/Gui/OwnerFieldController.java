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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

public class OwnerFieldController implements GuiController {
    @FXML AnchorPane field;
    @FXML ImageView firstHand;
    @FXML ImageView secondHand;
    @FXML ImageView thirdHand;
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
    @FXML ImageView secretObj;
    @FXML Button secretObjButton;
    @FXML ImageView bluePawn;
    @FXML ImageView yellowPawn;
    @FXML ImageView greePawn;
    @FXML ImageView redPawn;
    List<String> opponentNames;
    String fieldOwner, playerName;
    HashMap <String, ImageView> playerPawn;
//this starts from position 0. for each position there's 4 points that is +23 horizontally and +18 vertically
// so if there is one pawn on the position I'll just do x+23 y+0, if two x+0 y+18 and so on
    private final double[][] pawnCoordinates = {
        {923,363},// 0
        {964, 363},//  1
        {1009, 363},// 2
        {1030, 320},// 3
        {988, 320},// 4
        {947, 320},// 5
        {901, 320},// 6
        {901, 282},// 7
        {946, 282},// 8
        {988, 282},// 9
        {1030, 282},// 10
        {1030, 244},// 11
        {988, 244},// 12
        {946, 244},// 13
        {901, 244},// 14
        {901, 206},// 15
        {946, 206},// 16
        {988, 206},// 17
        {1030, 206},// 18
        {1030, 169},// 19
        {964, 149},// 20
        {901, 169},// 21
        {901, 129},// 22
        {901, 89},// 23
        {926, 57},// 24
        {964, 53},// 25
        {1004, 57},// 26
        {1030, 91},// 27
        {1030, 127},// 28
        {964, 100},// 29
    };
    public void displayText(String s, ClientControllerState state){
        if(state.equals(ClientControllerState.REQUESTING_PLACEMENT)){

        }else if(state.equals(ClientControllerState.REQUESTING_DRAW_CARD)){

        }
    }
    public void initialize(String fieldOwner, String playerName, List<String> opponentNames, List<Integer> playerHand,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck,int[] commonObjs,int secrObj) {
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
        if (playerHand.size() == 2) {
            secondHand.visibleProperty().set(false);
            firstHand.setImage(getCardImage(playerHand.getFirst(),true));
            thirdHand.setImage(getCardImage(playerHand.get(1),true));
        } else {
            firstHand.setImage(getCardImage(playerHand.getFirst(),true));
            secondHand.setImage(getCardImage(playerHand.get(1),true));
            thirdHand.setImage(getCardImage(playerHand.get(2),true));
        }
        secretObj.visibleProperty().set(false);
        secretObj.setImage(getCardImage(secrObj,true));
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
    @FXML
    private void toggleSecretObj(){
        if(!secretObj.visibleProperty().get()){
            secretObj.visibleProperty().set(true);
            secretObjButton.setText("Hide Secret Objective");
        }else{
            secretObj.visibleProperty().set(false);
            secretObjButton.setText("Show Secret Objective");
        }
    }
    private Image getCardImage(int cardID,boolean top) {
        if(top) return new Image(GuiApplication.class.getResource("Cards/Front/" + cardID + ".png").toExternalForm());
        else return new Image(GuiApplication.class.getResource("Cards/Back/" + cardID + ".png").toExternalForm());
    }

    public void updatePawnPosition(int playerPoints, String playerName){
        if(playerPoints >= 0 && playerPoints <= 29){
            double[] newPosition = pawnCoordinates[playerPoints];
            ImageView currPawn = playerPawn.get(playerName);
            currPawn.setLayoutX(newPosition[0]);
            currPawn.setLayoutY(newPosition[1]);
        } else System.out.println("Points out of range");
    }
}
