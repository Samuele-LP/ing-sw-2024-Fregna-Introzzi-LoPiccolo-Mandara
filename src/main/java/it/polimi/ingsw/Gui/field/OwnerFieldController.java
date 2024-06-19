package it.polimi.ingsw.Gui.field;

import it.polimi.ingsw.Gui.GuiController;
import it.polimi.ingsw.Gui.field.FieldController;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class OwnerFieldController extends FieldController {
    @FXML private ImageView secretObj;
    @FXML
    private Button secretObjButton;
    private boolean isPlayerTurn, chosenFace;
    private int chosenCard = -1;
    private boolean isDrawPhase;

    //this starts from position 0. for each position there's 4 points that is +23 horizontally and +18 vertically
// so if there is one pawn on the position I'll just do x+23 y+0, if two x+0 y+18 and so on

    public void initialize(List<String> opponentNames, String firstPlayerName, List<Integer> playerHand,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs, int secretObjID, boolean isPlayerTurn) {

        this.isPlayerTurn = isPlayerTurn;

        this.opponentNames = opponentNames;
        this.firstPlayerName = firstPlayerName;
        this.fieldOwner = playerField.getName();

        showScoreTrack(scoreTrack);

        switchView.getItems().addAll(this.opponentNames);
        switchView.getItems().add("Player Chat");

        if (playerHand.size() == 2) {
            secondHand.visibleProperty().set(false);
            firstHand.setImage(getCardImage(playerHand.getFirst(), true));
            thirdHand.setImage(getCardImage(playerHand.get(1), true));
        } else {
            firstHand.setImage(getCardImage(playerHand.getFirst(), true));
            secondHand.setImage(getCardImage(playerHand.get(1), true));
            thirdHand.setImage(getCardImage(playerHand.get(2), true));

            firstHand.setOnMouseClicked(mouseEvent -> {//TODO:mark in some way the chosen card if possible, after cardSideChoice is called
                if (isPlayerTurn && !isDrawPhase) {
                    cardSideChoice(playerHand.getFirst());
                }

            });
            secondHand.setOnMouseClicked(mouseEvent -> {
                if (isPlayerTurn && !isDrawPhase) {
                    cardSideChoice(playerHand.get(1));
                }

            });
            thirdHand.setOnMouseClicked(mouseEvent -> {
                if (isPlayerTurn && !isDrawPhase) {
                    cardSideChoice(playerHand.get(2));
                }

            });
        }
        secretObj.visibleProperty().set(false);
        try {
            secretObj.setImage(getCardImage(secretObjID, true));
        } catch (NullPointerException e) {
            secretObjButton.setVisible(false);//The objective is not yet set so the button will be invisible
        }

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
        if (opponentNames.contains(selected)) {
            ClientController.getInstance().receiveCommand(new ShowOtherFieldCommand(selected));
        } else if (selected.equals("Player Chat")) {
            ClientController.getInstance().receiveCommand(new ChatLogCommand());
        }
    }

    @FXML
    private void toggleSecretObj() {
        if (!secretObj.visibleProperty().get()) {
            secretObj.visibleProperty().set(true);
            secretObjButton.setText("Hide Secret Objective");
        } else {
            secretObj.visibleProperty().set(false);
            secretObjButton.setText("Show Secret Objective");
        }
    }
    @Override
    protected void showCards(SimpleField playerField){
        resizePane(playerField.getCards(),1.0);
        ImageView cardImage;
        double centerX = anchorPane.getPrefWidth() / 2.0 - 75.0, centerY = anchorPane.getPrefHeight() / 2.0 - 50.0;//They are the coordinates to put the center of a card to the center of the pane

        //These four offset represent how the card would move of 1 position in the
        double positiveXOffset = +150 - 36, positiveYOffset = -100 + 43;//Y goes from top to bottom in the pane
        for (SimpleCard card : playerField.getCards()) {
            cardImage = new ImageView(getCardImage(card.getID(), card.isFacingUp()));
            cardImage.setFitWidth(150);
            cardImage.setFitHeight(100);
            double xOffset = positiveXOffset * card.getX();
            double yOffset = positiveYOffset * card.getY();
            AnchorPane.setLeftAnchor(cardImage, centerX + xOffset);
            AnchorPane.setTopAnchor(cardImage, centerY + yOffset);
            anchorPane.getChildren().add(cardImage);
            cardImage.setOnMouseClicked(mouseEvent -> {
                if (isPlayerTurn && !isDrawPhase && chosenCard > 0) {
                    double eventX = mouseEvent.getX(), eventY = mouseEvent.getY();
                    int cornerX, cornerY;
                    cornerX = eventX < 32 ? -1 : (eventX > 118 ? +1 : 0);
                    cornerY = eventY < 40 ? +1 : (eventY > 60 ? -1 : 0);
                    if (cornerX == 0 || cornerY == 0 ||chosenCard<1) {
                        return;//There is a dead zone around the middle of the card
                    }
                    ClientController.getInstance().receiveCommand(new PlaceCardCommand(card.getX() + cornerX, card.getY() + cornerY, chosenFace, chosenCard));
                }
            });
        }
        showFieldPawns();
    }
    private void cardSideChoice(int cardID) {
        Stage overlay = new Stage();
        AnchorPane container = new AnchorPane();
        container.setPrefSize(800.0, 350.0);

        ImageView card = new ImageView(getCardImage(cardID, true));//Face up
        card.setFitHeight(200);
        card.setFitWidth(300);
        AnchorPane.setTopAnchor(card, 75.0);
        AnchorPane.setLeftAnchor(card, 50.0);
        container.getChildren().add(card);

        card = new ImageView(getCardImage(cardID, false));//Face down
        card.setFitHeight(200);
        card.setFitWidth(300);
        AnchorPane.setTopAnchor(card, 75.0);
        AnchorPane.setRightAnchor(card, 50.0);
        container.getChildren().add(card);

        Button chooseTop = new Button();
        chooseTop.setText("Face up");
        chooseTop.setOnMouseClicked(mouseEvent -> {
            chosenCard = cardID;
            chosenFace = true;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });

        AnchorPane.setBottomAnchor(chooseTop, 40.0);
        AnchorPane.setLeftAnchor(chooseTop, 175.0);
        chooseTop.setPrefWidth(80);

        Button chooseBottom = new Button();
        chooseBottom.setText("Face down");
        chooseBottom.setOnMouseClicked(mouseEvent -> {
            chosenCard = cardID;
            chosenFace = false;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });

        AnchorPane.setBottomAnchor(chooseBottom, 40.0);
        AnchorPane.setRightAnchor(chooseBottom, 175.0);
        chooseBottom.setPrefWidth(80);

        Button goBack = new Button();
        goBack.setText("Close");
        goBack.setOnMouseClicked(mouseEvent -> {
            chosenCard = -1;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });
        AnchorPane.setTopAnchor(goBack, 0.0);
        AnchorPane.setLeftAnchor(goBack, 0.0);
        container.getChildren().addAll(chooseTop, chooseBottom, goBack);
        overlay.setScene(new Scene(container));
        //Sets the overlay on top and blocks any interaction with the field until a side is chosen/ the stage is closed
        overlay.setAlwaysOnTop(true);
        overlay.initModality(Modality.WINDOW_MODAL);
        overlay.initOwner((this.anchorPane.getParent().getScene().getWindow()));
        overlay.show();
    }
    @FXML
    public void firstResource() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceDeck));
    }

    @FXML
    public void secondGold() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldFirstVisible));
    }

    @FXML
    public void firstGold() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldDeck));
    }

    @FXML
    public void secondResource() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible));
    }

    @FXML
    public void TopDeckResource() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible));
    }

    @FXML
    public void TopDeckGold() {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldSecondVisible));
    }

    public void drawingPhase() {
        this.isDrawPhase = true;
    }

}
