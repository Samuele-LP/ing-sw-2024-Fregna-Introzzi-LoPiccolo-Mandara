package it.polimi.ingsw.Gui;

import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.ClientControllerState;
import it.polimi.ingsw.controller.userCommands.ChatLogCommand;
import it.polimi.ingsw.controller.userCommands.PlaceCardCommand;
import it.polimi.ingsw.controller.userCommands.ShowFieldCommand;
import it.polimi.ingsw.controller.userCommands.ShowOtherFieldCommand;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;

public class OpponentFieldController implements GuiController{
    @FXML private ImageView bluePawn,redPawn,greenPawn,yellowPawn;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane anchorPane;
    @FXML private ComboBox<String> switchView;
    @FXML private ImageView goldTop;
    @FXML private ImageView firstGold;
    @FXML private ImageView secondGold;
    @FXML private ImageView resourceTop;
    @FXML private ImageView firstResource;
    @FXML private ImageView secondResource;
    @FXML private ImageView objTop;
    @FXML private ImageView firstObj;
    @FXML private ImageView secondObj;
    private List<String> opponentNames;
    private String fieldOwner;
    public void initialize(List<String> opponentNames,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs) {
        objTop.setImage(getCardImage(99,false));
        this.opponentNames = opponentNames;
        this.fieldOwner = playerField.getName();

        switchView.getItems().addAll(this.opponentNames);
        switchView.getItems().remove(playerField.getName());
        switchView.getItems().add("Player Chat");
        switchView.getItems().add("Yor Field");

        showDecks(goldDeck,resDeck);
        firstObj.setImage((getCardImage(commonObjs[0],true)));
        secondObj.setImage((getCardImage(commonObjs[1],true)));

        showCards(playerField);
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
    }
    private void showCards(SimpleField playerField) {
        resizePane(playerField.getCards());
        ImageView cardImage;
        double centerX = anchorPane.getPrefWidth() / 2.0 - 105.0, centerY = anchorPane.getPrefHeight() / 2.0 - 70.0;//They are the coordinates to put the center of a card to the center of the pane

        //These four offset represent how the card would move of 1 position in the
        double positiveXOffset = +210 - 50.4, positiveYOffset = -140 + 60.2;//Y goes from top to bottom in the pane
        for (SimpleCard card : playerField.getCards()) {
            cardImage = new ImageView(getCardImage(card.getID(), card.isFacingUp()));
            cardImage.setFitWidth(210);
            cardImage.setFitHeight(140);
            double xOffset = positiveXOffset * card.getX();
            double yOffset = positiveYOffset * card.getY();
            AnchorPane.setLeftAnchor(cardImage, centerX + xOffset);
            AnchorPane.setTopAnchor(cardImage, centerY + yOffset);
            anchorPane.getChildren().add(cardImage);
        }
    }

    /**
     * Adjusts the field's size to fit every card
     */
    private void resizePane(List<SimpleCard> cards) {
        int highestX = 0;
        int highestY = 0;
        for (SimpleCard sc : cards) {
            if (Math.abs(sc.getX()) > highestX) {
                highestX = Math.abs(sc.getX());
            }
            if (Math.abs(sc.getY()) > highestY) {
                highestY = Math.abs(sc.getY());
            }
        }
        if (anchorPane.getPrefWidth() < highestX * 210 * 2) {
            anchorPane.setPrefWidth(highestX * 210 * 2);
        }
        if (anchorPane.getPrefHeight() < highestY * 140 * 2) {
            anchorPane.setPrefHeight(highestY * 140 * 2);
        }
    }

    private void showScoreTrack(HashMap<String, Integer> playerPoints) {

    }

    private void showDecks(DeckViewGui goldDeck, DeckViewGui resDeck) {
        if (resDeck.getTopColour() != null) {
            resourceTop.setImage(getCardImage(resDeck.getTopColour() == CardType.fungi ? 1 : (resDeck.getTopColour() == CardType.plant ? 11
                    : (resDeck.getTopColour() == CardType.animal ? 21 : 31)), false));
        }
        if (resDeck.getFirstVisible() != null) {
            firstResource.setImage(getCardImage(resDeck.getFirstVisible(), true));
        }
        if (resDeck.getSecondVisible() != null) {
            secondResource.setImage(getCardImage(resDeck.getSecondVisible(), true));
        }
        if (goldDeck.getTopColour() != null) {
            goldTop.setImage(getCardImage(goldDeck.getTopColour() == CardType.fungi ?
                    41 : (goldDeck.getTopColour() == CardType.plant ? 51 : (goldDeck.getTopColour() == CardType.animal ? 61 : 7 - 1)), false));
        }
        if (goldDeck.getFirstVisible() != null) {
            firstGold.setImage(getCardImage(goldDeck.getFirstVisible(), true));
        }
        if (goldDeck.getFirstVisible() != null) {
            secondGold.setImage(getCardImage(goldDeck.getSecondVisible(), true));
        }
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

    public void reload(String name) {//If the currently loaded field received an update then it's reloaded
        if(name==null||name.equals(fieldOwner)){
            ClientController.getInstance().receiveCommand(new ShowOtherFieldCommand(name));
        }
    }
}

