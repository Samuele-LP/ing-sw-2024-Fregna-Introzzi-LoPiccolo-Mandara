package it.polimi.ingsw.Gui.controllers;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalScreenController extends GuiController{
    @FXML Label firstPoints, secondPoints, thirdPoints, fourthPoints,firstName,secondName,thirdName,fourthName;
    @FXML
    ImageView firstImg, secondImg,thirdImg,fourthImg;
    @FXML Label disconnectionLabel;

    public void initialize(ImmutableScoreTrack finalPlayerScore, List<String> winners, boolean disconnection) {
        Image crown = firstImg.getImage();
        Image loss = secondImg.getImage();

        if (disconnection) {
           disconnectionLabel.setVisible(true);
        }

        List<Map.Entry<String, Integer>> orderedPlayers = finalPlayerScore.getPlayerPoints().entrySet().stream().sorted((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            } else if (o1.getValue() < o2.getValue()) {
                return 1;
            } else {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey(), o2.getKey());
            }
        }).toList();

        HashMap<String,String> colours = finalPlayerScore.getColours();

        firstName.setText(orderedPlayers.getFirst().getKey());
        firstName.setTextFill(ansiToPaint(colours.get(orderedPlayers.getFirst().getKey())));
        firstPoints.setText(orderedPlayers.getFirst().getValue().toString() + (orderedPlayers.get(0).getValue() == 1 ? " POINT" : " POINTS"));

        if (!winners.contains(orderedPlayers.getFirst().getKey())) { //only happens whenever the player with most points disconnects
            firstImg.setImage(loss);
        }

        secondName.setText(orderedPlayers.get(1).getKey());
        secondName.setTextFill(ansiToPaint(colours.get(orderedPlayers.get(1).getKey())));
        secondPoints.setText(orderedPlayers.get(1).getValue().toString() + (orderedPlayers.get(1).getValue() == 1 ? " POINT" : " POINTS"));

        if (winners.contains(orderedPlayers.get(1).getKey())) {
            secondImg.setImage(crown);
        }

        switch(finalPlayerScore.getPlayerPoints().keySet().size()){
            case 4 -> {

                thirdName.setText(orderedPlayers.get(2).getKey());
                thirdName.setTextFill(ansiToPaint(colours.get(orderedPlayers.get(2).getKey())));
                thirdPoints.setText(orderedPlayers.get(2).getValue().toString() + (orderedPlayers.get(2).getValue() == 1 ? " POINT" : " POINTS"));

                fourthName.setText(orderedPlayers.get(3).getKey());
                fourthName.setTextFill(ansiToPaint(colours.get(orderedPlayers.get(3).getKey())));
                fourthPoints.setText(orderedPlayers.get(3).getValue().toString() + (orderedPlayers.get(3).getValue() == 1 ? " POINT" : " POINTS"));

                if(winners.contains(orderedPlayers.get(2).getKey())){
                    thirdImg.setImage(crown);
                }
                if(winners.contains(orderedPlayers.get(3).getKey())){
                    fourthImg.setImage(crown);
                }

            }
            case 3 -> {
                fourthImg.setVisible(false);
                fourthName.setVisible(false);
                fourthPoints.setVisible(false);

                thirdName.setText(orderedPlayers.get(2).getKey());
                thirdName.setTextFill(ansiToPaint(colours.get(orderedPlayers.get(2).getKey())));
                thirdPoints.setText(orderedPlayers.get(2).getValue().toString() + (orderedPlayers.get(2).getValue() == 1 ? " POINT" : " POINTS"));
                if(winners.contains(orderedPlayers.get(2).getKey())){
                    thirdImg.setImage(crown);
                }
            }
            case 2 -> {
                fourthImg.setVisible(false);
                fourthName.setVisible(false);
                fourthPoints.setVisible(false);

                thirdImg.setVisible(false);
                thirdPoints.setVisible(false);
                thirdName.setVisible(false);
            }
        }
    }

    private Paint ansiToPaint(String col){
        switch (col){
            case ConstantValues.ansiRed -> {
                return Color.RED;
            }
            case ConstantValues.ansiBlue -> {
                return Color.BLUE;
            }
            case ConstantValues.ansiGreen -> {
                return Color.GREEN;
            }
            case ConstantValues.ansiYellow -> {
                return Color.YELLOW;
            }
        }
        return Color.WHITE;
    }
}
