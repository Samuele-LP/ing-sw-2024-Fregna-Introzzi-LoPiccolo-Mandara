package it.polimi.ingsw.Gui;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OwnerFieldController implements GuiController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView firstHand, secondHand, thirdHand;
    @FXML
    private ComboBox<String> switchView;
    @FXML
    private ImageView goldTop, firstGold, secondGold;
    @FXML
    private ImageView resourceTop, firstResource, secondResource;
    @FXML
    private ImageView objTop, firstObj, secondObj, secretObj;
    @FXML
    private Button secretObjButton;
    @FXML
    private ImageView bluePawn, yellowPawn, greenPawn, redPawn;
    private List<String> opponentNames = new ArrayList<>();
    private final HashMap<String, ImageView> playerPawn = new HashMap<>();
    private boolean isPlayerTurn,chosenFace;
    private int chosenCard;
    private boolean isDrawPhase;

    //this starts from position 0. for each position there's 4 points that is +23 horizontally and +18 vertically
// so if there is one pawn on the position I'll just do x+23 y+0, if two x+0 y+18 and so on
    private final double[][] pawnCoordinates = {
            {923, 363},// 0
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
    public void initialize(List<String> opponentNames, List<Integer> playerHand,
                           SimpleField playerField, ImmutableScoreTrack scoreTrack, DeckViewGui goldDeck,
                           DeckViewGui resDeck, int[] commonObjs, int secretObjID, boolean isPlayerTurn) {

        this.isPlayerTurn = isPlayerTurn;
        objTop.setImage(getCardImage(99, false));
        this.opponentNames = opponentNames;

        for (String name : scoreTrack.getColours().keySet()) {
            if (scoreTrack.getColours().get(name).equals(ConstantValues.ansiBlue)) {
                playerPawn.put(name, bluePawn);
                break;
            }
            if (scoreTrack.getColours().get(name).equals(ConstantValues.ansiRed)) {
                playerPawn.put(name, redPawn);
                break;
            }
            if (scoreTrack.getColours().get(name).equals(ConstantValues.ansiYellow)) {
                playerPawn.put(name, yellowPawn);
                break;
            }
            if (scoreTrack.getColours().get(name).equals(ConstantValues.ansiGreen)) {
                playerPawn.put(name, greenPawn);
                break;
            }
        }
        showScoreTrack(scoreTrack.getPlayerPoints());

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
                if(isPlayerTurn&&!isDrawPhase){
                    cardSideChoice(playerHand.getFirst());
                }

            });
            secondHand.setOnMouseClicked(mouseEvent -> {
                if(isPlayerTurn&&!isDrawPhase){
                    cardSideChoice(playerHand.get(1));
                }

            });
            thirdHand.setOnMouseClicked(mouseEvent -> {
                if(isPlayerTurn&&!isDrawPhase){
                    cardSideChoice(playerHand.get(2));
                }

            });
        }
        secretObj.visibleProperty().set(false);
        try {
            secretObj.setImage(getCardImage(secretObjID, true));
        }catch (NullPointerException e){
            secretObjButton.setVisible(false);//The objective is not yet set so the button will be invisible
        }

        showDecks(goldDeck, resDeck);

        firstObj.setImage((getCardImage(commonObjs[0], true)));
        secondObj.setImage((getCardImage(commonObjs[1], true)));

        showCards(playerField);
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
    }
    private void showCards(SimpleField playerField) {
        resizePane(playerField.getCards());
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
                if(isPlayerTurn&&!isDrawPhase){
                    double eventX = mouseEvent.getX(), eventY = mouseEvent.getY();
                    int cornerX,cornerY;
                    cornerX = eventX<32?-1:(eventX>118?+1:0);
                    cornerY = eventY<40?+1:(eventY>60?-1:0);
                    if(cornerX==0||cornerY==0){
                        return;//There is a dead zone around the middle of the card
                    }
                    ClientController.getInstance().receiveCommand(new PlaceCardCommand(card.getX()+cornerX,card.getY()+cornerY,chosenFace,chosenCard));
                }
            });
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
        if (anchorPane.getPrefWidth() < highestX * 150 * 2) {
            anchorPane.setPrefWidth(highestX * 150 * 2);
        }
        if (anchorPane.getPrefHeight() < highestY * 100 * 2) {
            anchorPane.setPrefHeight(highestY * 100 * 2);
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

    private Image getCardImage(int cardID, boolean top) {
        if (top) return new Image(GuiApplication.class.getResource("Cards/Front/" + cardID + ".png").toExternalForm());
        else return new Image(GuiApplication.class.getResource("Cards/Back/" + cardID + ".png").toExternalForm());
    }

    /*
    private void firstClickForPlacing(ImageView card){
        for(ImageView placedCard : getPlacedCards()){
            placedCard.setOnMouseClicked(event ->{
                double clickedX = event.getX();
                double clickedY = event.getY();
                double[] cornerClicked = determineCorner(card, clickedX, clickedY);
                if (cornerClicked != null)
                    placeCard(card, cornerClicked);
            });
            
        }
    }

    private void placeCard(ImageView card, double[] cornerClicked) {
        //add card to te scene
        //adding a shift to set correctly the card in the correct place
    }

    private ImageView[] getPlacedCards() {
        return new ImageView[] {};
    }

    private double[] determineCorner(ImageView card, double clickX, double clickY) {
        double cardWidth = card.getFitWidth();
        double cardHeight = card.getFitHeight();
        double x = card.getLayoutX();
        double y = card.getLayoutY();

        if (clickX <= cardWidth / 2 && clickY <= cardHeight / 2) {
            return new double[]{x, y}; // right top
        } else if (clickX > cardWidth / 2 && clickY <= cardHeight / 2) {
            return new double[]{x + cardWidth, y}; // left top
        } else if (clickX <= cardWidth / 2 && clickY > cardHeight / 2) {
            return new double[]{x, y + cardHeight}; // right bottom
        } else if (clickX > cardWidth / 2 && clickY > cardHeight / 2) {
            return new double[]{x + cardWidth, y + cardHeight}; // left bottom
        }
        return null;
    }*/
    private void cardSideChoice(int cardID){
        Stage overlay = new Stage();
        overlay.setTitle("CHOOSE THE SIDE OF THE CARD");
        AnchorPane container = new AnchorPane();
        container.setPrefSize(800.0,350.0);
        ImageView card = new ImageView(getCardImage(cardID,true));//Face up
        card.setFitHeight(200);
        card.setFitWidth(300);
        AnchorPane.setTopAnchor(card,75.0);
        AnchorPane.setLeftAnchor(card,50.0);
        container.getChildren().add(card);
        card = new ImageView(getCardImage(cardID,false));//Face down
        card.setFitHeight(200);
        card.setFitWidth(300);
        AnchorPane.setTopAnchor(card,75.0);
        AnchorPane.setRightAnchor(card,50.0);
        container.getChildren().add(card);

        Button chooseTop = new Button();
        chooseTop.setText("Face up");
        chooseTop.setOnMouseClicked(mouseEvent -> {
            chosenCard = cardID;
            chosenFace = true;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });
        AnchorPane.setBottomAnchor(chooseTop,40.0);
        AnchorPane.setLeftAnchor(chooseTop,175.0);
        chooseTop.setPrefWidth(80);
        Button chooseBottom = new Button();
        chooseBottom.setText("Face down");
        chooseBottom.setOnMouseClicked(mouseEvent -> {
            chosenCard = cardID;
            chosenFace = false;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });
        AnchorPane.setBottomAnchor(chooseBottom,40.0);
        AnchorPane.setRightAnchor(chooseBottom,175.0);
        chooseBottom.setPrefWidth(80);
        Button goBack = new Button();
        goBack.setText("Close");
        goBack.setOnMouseClicked(mouseEvent -> {
            chosenCard = -1;
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        });
        AnchorPane.setTopAnchor(goBack,0.0);
        AnchorPane.setLeftAnchor(goBack,0.0);

        container.getChildren().addAll(chooseTop,chooseBottom,goBack);
        overlay.setScene(new Scene(container));
        //Sets the overlay on top and blocks any interaction with the field until a side is chosen/ the stage is closed
        overlay.setAlwaysOnTop(true);
        overlay.initModality(Modality.WINDOW_MODAL);
        overlay.initOwner((this.anchorPane.getParent().getScene().getWindow()));
        overlay.show();
    }
    public void updatePawnPosition(int playerPoints, String playerName) {
        if (playerPoints >= 0 && playerPoints <= 29) {
            double[] newPosition = pawnCoordinates[playerPoints];
            ImageView currPawn = playerPawn.get(playerName);
            currPawn.setLayoutX(newPosition[0]);
            currPawn.setLayoutY(newPosition[1]);
        } else System.out.println("Points out of range");
    }

    public void firstResource(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceDeck));
        GuiApplication.loadWaitingScreen();
    }


    public void secondGold(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldFirstVisible));
        GuiApplication.loadWaitingScreen();
    }

    public void firstGold(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldDeck));
        GuiApplication.loadWaitingScreen();
    }

    public void secondResource(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible));
        GuiApplication.loadWaitingScreen();
    }

    public void TopDeckResource(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible));
        GuiApplication.loadWaitingScreen();
    }

    public void TopDeckGold(MouseEvent mouseEvent) {
        ClientController.getInstance().receiveCommand(new DrawCardCommand(PlayerDrawChoice.goldSecondVisible));
        GuiApplication.loadWaitingScreen();
    }

    public void drawingPhase() {
        this.isDrawPhase=true;
    }
}
