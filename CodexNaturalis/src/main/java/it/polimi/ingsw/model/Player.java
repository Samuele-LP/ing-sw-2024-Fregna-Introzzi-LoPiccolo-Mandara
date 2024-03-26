package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player
 * */
public class Player extends Game{
    private final String name;  //Attribute that can't be changed (or should we add this option?), reffering to the declared name of the player
    private int currentPoints; //Attribute used to keep track of current points
    private int currentTurn;   //Attribute used to keep track of the current turn
    private List<ID> personalHandCards; //Attribute for listing actual cards in a players hand

    public Player(String name){
        name = this.name;
        currentPoints=0;
        currentTurn=0;
    }

    public void receiveDrawnCard(PlayableCard card){
        //Check if # of card is valid, otherwise something went wrong somewhere in the code before
        if checkQuantityOfCards(){
            // In attesa dei metodi presenti in Game per decidere come implementare questo metodo
        }

    }

    public void printAvailableCornerCoords() throws NotPlacedException {
        // Dubbio: come mostrare le carte (e quindi gli angoli disponibili) senza la GUI ?
    }

    private void placeCard(PlayableCard card, int xCoordinate, int yCoordinate){
        // Assignment of the coordinates of a playableCard
    }

    private boolean isPlacingPointValid(Point point, int chosenCorner){
        // Check if in a point (X,Y) there is an empty available corner of a card

        return false; // return false if position is invalid
    }

    private int getPoints(PlayableCard card){
        // Check, and add, how many points the player get based on the conditions of the card selected and the position of placement (and so, based on other cards connected, if the condition requires so)

        return (-1);
    }

    // Select one of the two possible personale secret objective
    private int chooseSecretObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        private int choosenID;

        throws IOException
        {
            // Print on console the ID of two choices
            System.out.println("Chooce your Secret Objective between: " + obj1.getID() + " " + obj2.getID());

            // Choose one of the two
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int choosenID = reader.readLine();
        }

        return choosenID;
    }

    // Calculate points for current player given from the common objective
    private int calculateCommonObjectiveGivenPoint(ObjectiveCard objective){

        return (-1);
    }

    private int calculatePointsOnPlace(PlayableCard card, Point placedIn) throws NotPlacedException {
        //Check if card conditions are easy or hard type

        //Add points on easy condition (the one for number of TokenType)

        //Add points on hard condition (the one for objective of card combinations)

        return (-1);
    }

    // Check if the number of cards in the current hand is valid (in order to check if other parts of the code made some mistakes)
    private boolean checkQuantityOfCards(){
        List<ID> personalHandCards = new ArrayList<>(3);    //DA CONFERMARE: LA LUNGHEZZA E' SEMPRE 3? VERSO FINE GIOCO SI HANNO ANCORA 3 CARTE O SI GIOCA FINCHE' TUTTI NON HANNO 0 CARTE IN MANO?
        personalHandCards = this.personalHandCards;
        boolean result = true;

        //DA AGGIUNGERE, SE AGLI ULTIMI TURNI SI POSSONO AVERE MENO DI 3 CARTE, CONTROLLO SU NUMERO TURNO O SU QUANTITA' DI CARTE RIMANENTI NEI VARI MAZZI
        result = personalHandCards.size()==3 ? true : false;
        return result;
    }
}
