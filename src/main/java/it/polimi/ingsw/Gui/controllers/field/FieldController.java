package it.polimi.ingsw.Gui.controllers.field;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Gui.GuiApplication;
import it.polimi.ingsw.Gui.controllers.GuiController;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller for handling the field view in the GUI.
 */
public class FieldController extends GuiController {

    @FXML
    protected ScrollPane scrollPane;

    @FXML
    protected AnchorPane anchorPane;

    @FXML
    protected ImageView firstHand, secondHand, thirdHand;

    @FXML
    protected ComboBox<String> switchView;

    @FXML
    protected ImageView goldTop, firstGold, secondGold;

    @FXML
    protected ImageView resourceTop, firstResource, secondResource;

    @FXML
    protected ImageView objTop, firstObj, secondObj;

    @FXML
    protected ImageView bluePawn, yellowPawn, greenPawn, redPawn, blackPawn;

    @FXML
    protected Label numFungi, numPlant, numAnimal, numInsect, numInk, numScroll, numQuill;

    protected List<String> opponentNames = new ArrayList<>();

    protected final HashMap<String, ImageView> playerPawn = new HashMap<>();

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

    /**
     * Represents how many pawns are in each position
     */
    private int[] pawnsOnPosition = new int[30];

    protected String fieldOwner, firstPlayerName;

    /**
     * Shows the decks' information on the GUI.
     *
     * @param goldDeck   the gold deck view
     * @param resDeck    the resource deck view
     * @param objectives the common objectives
     */
    protected void showDecks(DeckViewGui goldDeck, DeckViewGui resDeck, int[] objectives) {
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
                    41 : (goldDeck.getTopColour() == CardType.plant ? 51 : (goldDeck.getTopColour() == CardType.animal ? 61 : 71)), false));
        }

        if (goldDeck.getFirstVisible() != null) {
            firstGold.setImage(getCardImage(goldDeck.getFirstVisible(), true));
        }

        if (goldDeck.getSecondVisible() != null) {
            secondGold.setImage(getCardImage(goldDeck.getSecondVisible(), true));
        }

        firstObj.setImage((getCardImage(objectives[0], true)));
        secondObj.setImage((getCardImage(objectives[1], true)));
        objTop.setImage(getCardImage(99, false));
    }

    /**
     * Shows the cards on the field.
     *
     * @param playerField the player's field
     */
    protected void showCards(SimpleField playerField) { //This method by default shows cards that are 210*140, it is used in the opponent's fields
        resizePane(playerField.getCards(), 1.4); //The resizing is 1.4 times that of the player field
        ImageView cardImage;
        double centerX = anchorPane.getPrefWidth() / 2.0 - 105.0, centerY = anchorPane.getPrefHeight() / 2.0 - 70.0; //They are the coordinates to put the center of a card to the center of the pane

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

        showFieldPawns();
    }

    /**
     * Shows the pawns on the field.
     */
    protected void showFieldPawns() {
        if (firstPlayerName.equals(fieldOwner)) {
            blackPawn.setVisible(true);
            AnchorPane.setLeftAnchor(blackPawn, anchorPane.getPrefWidth() / 2.0 - blackPawn.getFitWidth() * 2);
            AnchorPane.setTopAnchor(blackPawn, anchorPane.getPrefHeight() / 2.0 - blackPawn.getFitHeight() / 2.0);

            if (playerPawn.get(fieldOwner) != null) { //If the field is viewed before the pawn choice
                ImageView fieldPawn = new ImageView(playerPawn.get(fieldOwner).getImage());
                fieldPawn.setFitWidth(redPawn.getFitWidth());
                fieldPawn.setFitHeight(redPawn.getFitHeight());
                AnchorPane.setLeftAnchor(fieldPawn, anchorPane.getPrefWidth() / 2.0 + blackPawn.getFitWidth());
                AnchorPane.setTopAnchor(fieldPawn, anchorPane.getPrefHeight() / 2.0 - blackPawn.getFitHeight() / 2.0);
                anchorPane.getChildren().addAll(fieldPawn, blackPawn);
            }
        } else if (playerPawn.get(fieldOwner) != null) {
            ImageView fieldPawn = new ImageView(playerPawn.get(fieldOwner).getImage());
            fieldPawn.setFitWidth(redPawn.getFitWidth());
            fieldPawn.setFitHeight(redPawn.getFitHeight());
            blackPawn.setVisible(false);
            AnchorPane.setLeftAnchor(fieldPawn, anchorPane.getPrefWidth() / 2.0 - fieldPawn.getFitWidth() / 2.0);
            AnchorPane.setTopAnchor(fieldPawn, anchorPane.getPrefHeight() / 2.0 - fieldPawn.getFitHeight() / 2.0);
            anchorPane.getChildren().add(fieldPawn);
        }
    }

    /**
     * Adjusts the field's size to fit every card.
     *
     * @param cards          the cards to be displayed
     * @param sizeMultiplier the multiplier for the size of the cards
     */
    protected void resizePane(List<SimpleCard> cards, double sizeMultiplier) {
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

        if (anchorPane.getPrefWidth() < highestX * 150 * 2 * sizeMultiplier) {
            anchorPane.setPrefWidth(highestX * 150 * 2 * sizeMultiplier);
        }

        if (anchorPane.getPrefHeight() < highestY * 100 * 2 * sizeMultiplier) {
            anchorPane.setPrefHeight(highestY * 100 * 2 * sizeMultiplier);
        }
    }

    /**
     * Retrieves the card image based on the card ID and whether it is the top card.
     *
     * @param cardID the card ID
     * @param top    whether the card is the top card
     * @return the card image
     */
    protected Image getCardImage(int cardID, boolean top) {
        if (top) return new Image(GuiApplication.class.getResource("Cards/Front/" + cardID + ".png").toExternalForm());
        else return new Image(GuiApplication.class.getResource("Cards/Back/" + cardID + ".png").toExternalForm());
    }

    /**
     * Shows the score track on the field.
     *
     * @param scoreTrack the score track
     */
    protected void showScoreTrack(ImmutableScoreTrack scoreTrack) {
        pawnsOnPosition = new int[30];

        for (String name : scoreTrack.getColours().keySet()) {
            int points = scoreTrack.getPlayerPoints().get(name);

            switch (scoreTrack.getColours().get(name)) {

                case ConstantValues.ansiBlue -> {
                    playerPawn.put(name, bluePawn);
                    updatePawnPosition(points, name);
                    bluePawn.setVisible(true);
                }

                case ConstantValues.ansiRed -> {
                    playerPawn.put(name, redPawn);
                    updatePawnPosition(points, name);
                    redPawn.setVisible(true);
                }

                case ConstantValues.ansiGreen -> {
                    playerPawn.put(name, greenPawn);
                    updatePawnPosition(points, name);
                    greenPawn.setVisible(true);
                }

                case ConstantValues.ansiYellow -> {
                    playerPawn.put(name, yellowPawn);
                    updatePawnPosition(points, name);
                    yellowPawn.setVisible(true);
                }
            }
        }
    }

    /**
     * Updates the position of a pawn based on the player's points.
     *
     * @param playerPoints the player's points
     * @param playerName   the player's name
     */
    private void updatePawnPosition(int playerPoints, String playerName) {
        if (playerPoints >= 0 && playerPoints <= 29) {
            pawnsOnPosition[playerPoints]++;
            double[] newPosition = pawnCoordinates[playerPoints];
            ImageView currPawn = playerPawn.get(playerName);
            double xOffset = 0.0, yOffset = 0.0;

            if (pawnsOnPosition[playerPoints] == 4) {
                xOffset = 23.0;
                yOffset = 18.0;
            } else if (pawnsOnPosition[playerPoints] == 3) {
                xOffset = 23.0;
                yOffset = 0.0;
            } else if (pawnsOnPosition[playerPoints] == 2) {
                xOffset = 0.0;
                yOffset = 18.0;
            }
            currPawn.setLayoutX(newPosition[0] + xOffset);
            currPawn.setLayoutY(newPosition[1] + yOffset);
        }
    }

    /**
     * Updates the visible symbols on the field.
     *
     * @param symbols the symbols to be updated
     */
    protected void updateVisibleSymbols(HashMap<TokenType, Integer> symbols) {
        numFungi.setText(symbols.get(TokenType.fungi).toString());
        numPlant.setText(symbols.get(TokenType.plant).toString());
        numAnimal.setText(symbols.get(TokenType.animal).toString());
        numInsect.setText(symbols.get(TokenType.insect).toString());
        numQuill.setText(symbols.get(TokenType.quill).toString());
        numInk.setText(symbols.get(TokenType.ink).toString());
        numScroll.setText(symbols.get(TokenType.scroll).toString());
    }
}
