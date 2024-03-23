package it.polimi.ingsw.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class represents a player
 * */
public class Player extends Game{
    private final String name;  //Attribute that can't be changed (or should we add this option?), reffering to the declared name of the player
    private byte currentPoints; //Attribute used to keep track of current points
    private byte currentTurn;   //Attribute used to keep track of the current turn
    private List<ID> personalHandCards; //Attribute for listing actual cards in a players hand

    public void receiveDrawnCard(PlayableCard card){

    }

    public void printAvailableCornerCoords(){

    }

    private boolean placeCard(PlayableCard card, int xCoordinate, int yCoordinate){

        return false;
    }

    private boolean isPlacingPointValid(Point point, int chosenCorner){

        return false;
    }

    private byte getPoints(){

        return (-1);
    }

    private byte chooseSecretObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        private byte choosenID;

        throws IOException
        {
            // Print on console the ID of two choices
            System.out.println("Chooce your Secret Objective between: " + obj1.getID() + " " + obj2.getID());

            // Choose one of the two
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Byte choosenID = reader.readLine();
        }

        return choosenID;
    }

    // WHAT IS THIS FOR !?!?!?!
    private byte calculateObjective(ObjectiveCard objective){

        return (-1);
    }

    private byte calculatePointsOnPlace(PlayableCard card, Point placedIn){
        //Check if card conditions are easy or hard type

        //Add points on easy condition (the one for number of TokenType)

        //Add points on hard condition (the one for objective of card combinations)

        return (-1);
    }
}
