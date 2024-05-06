package it.polimi.ingsw.view;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.PlayerFieldView.cardList;

/**
 * Class used to represent the state of the Game, it's created after the Game has started
 */
public class GameView {
    private final String playerName;
    private ImmutableScoreTrack scoreTrack;
    private final DeckView goldDeck;
    private final DeckView resourceDeck;
    private List<Integer> playerHand;
    private final PlayerFieldView ownerField;
    private final int startingCardID;
    private final HashMap<String , PlayerFieldView> opponentFields;
    private int[] secretObjectiveChoices= new int[2];
    private final int[] commonObjectives= new int[2];
/**
 * After the constructor the methods to update the decks must be called by the controller with the necessary information
 */
    public GameView( List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective) throws IOException {
        this.playerName = playerName;
        startingCardID=startingCard;
        this.goldDeck = new DeckView("Gold");
        this.resourceDeck = new DeckView("Resource");
        opponentFields=new HashMap<>();
        HashMap<String ,Integer> startingScoreTrack=  new HashMap<>();
        for(String s: otherPlayerNames){
            opponentFields.put(s,new PlayerFieldView());
            startingScoreTrack.put(s,0);
        }
        commonObjectives[0]=firstCommonObjective;
        commonObjectives[1]=secondCommonObjective;
        scoreTrack=new ImmutableScoreTrack(startingScoreTrack);
        ownerField=new PlayerFieldView();
    }
    /**
     * This method updates the information of the scoreTrack
     * @param updated is the updated scoreTrack tha has been received
     */
    public void updateScoreTrack(ImmutableScoreTrack updated){
        if(updated!=null)
            scoreTrack = updated;
    }

    /**
     *
     * @param topGold colour of the top of the deck
     * @param topResource colour of the top of the deck
     * @param visibleCards the four visible cards that can be drawn, in this order resource first visible -> resource second visible -> gold first visible-> gold second visible
     */
    public void updateDecks(CardType topGold, CardType topResource, List<Integer> visibleCards){
        resourceDeck.update(topResource,visibleCards.get(0),visibleCards.get(1));
        goldDeck.update(topGold,visibleCards.get(2),visibleCards.get(3));
    }

    /**
     * Adds a card to the field of the player that has just made a move
     * @param name name of the opponent who made the move
     * @param placeID ID of the placed card
     * @param placedX xPosition
     * @param placedY yPosition
     */
    public void updateOtherPlayerField(String name, int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType,Integer> visibleSymbols){
        opponentFields.get(name).updateField(placeID,placedX,placedY,isFacingUp,visibleSymbols);
    }
    /**Adds the card the owner of the field just placed
     * @param placedID ID of the placed card
     * @param placedX xPosition
     * @param placedY yPosition*/
    public void updateOwnerField(int placedID, int placedX, int placedY,boolean isFacingUp, Map<TokenType,Integer> visibleSymbols){
        ownerField.updateField( placedID,  placedX,  placedY,isFacingUp,visibleSymbols);
    }

    /**
     * Method that prints information about the scoreTrack for the cli
     */
    public void printCommonField(){
        System.out.println("\n\n\n");
        scoreTrack.printTable();
        System.out.println("----------------------------------------------------------------------------------------------------");
        showCommonObjectives();
        System.out.println("----------------------------------------------------------------------------------------------------");
        resourceDeck.printDeck();
        System.out.println("----------------------------------------------------------------------------------------------------");
        goldDeck.printDeck();
    }
    /**
     * Prints the client's field for the CLI
     */
    public void printOwnerField(){
        System.out.println("\n\n\n");
        System.out.println("Your field:");
        ownerField.printField();
    }
    /**
     * Prints the requested player's field for the CLI
     * @param name name of the opponent whose field will be shown
     */
    public void printOpponentField(String name){
        System.out.println("\n\n\n");
        if(!opponentFields.containsKey(name)){
            showText("Incorrect player name.");
            return;
        }
        System.out.println(name +"'s field:");
        opponentFields.get(name).printField();
    }

    /**This method should be called after a player has placed a card, to update for the removal of the card placed,
     *  and  after a player has drawn to update for the addition of the new card
     * @param newPlayerHand the hand after the player has made a move
     */
    public void updatePlayerHand(List<Integer> newPlayerHand){
        playerHand= newPlayerHand;
    }

    /**
     * Removes the card with id lastPlayed from the hand of the player
     * @param lastPlayed the id of the card that has been placed
     */
    public void updatePlayerHand(int lastPlayed){
        if(playerHand.contains(lastPlayed)){
            playerHand.remove((Integer) lastPlayed);
        }
    }

    /**
     * Returns the hand of the player. Used to check if the card they want to place is in their possession
     */
    public List<Integer> getPlayerHand(){
        return new ArrayList<>(playerHand);
    }
    public void updateAvailablePositions(List<Point> availablePositions){
        ownerField.updateAvailablePositions(availablePositions);
    }

    /**
     * Method that prints a message as either CLI or GUI according to how the program was started
     */
    public static void showText(String s) {
        System.out.println(s);
    }

    /**
     * This method memorizes the two possible choices and then shows them
     * @param firstChoice first objective choice
     * @param secondChoice second objective choice
     */
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        System.out.println("\n\n\n\nHere are your secret objective choices");
        secretObjectiveChoices[0]=firstChoice;
        secretObjectiveChoices[1]=secondChoice;
        showSecretObjectives();
    }
    /**
     * Adds information about the secret objective card chosen by the player
     * @param id is the id of the objective card to be chosen
     * @return true if the choice happened successfully
     */
    public boolean setSecretObjective(int id){
        if(secretObjectiveChoices.length==1){
            showText("You have already chosen an objective!");
            return false;
        }else if(id!=secretObjectiveChoices[0]&&id!=secretObjectiveChoices[1]){
            showText("You don't have this card as a choice for an objective!");
            return false;
        }
        else {
            secretObjectiveChoices= new int[1];
            secretObjectiveChoices[0]=id;
            return true;
        }
    }

    /**
     * Prints the two common objectives
     */
    public void showCommonObjectives(){
        List<Card> objectives;
        try {
            objectives=Creation.getObjectiveCards();
        }catch (IOException e){
            throw new RuntimeException();
        }
        System.out.println("Common objectives:");
        ObjectiveCard obj = (ObjectiveCard) objectives.get(commonObjectives[0]-87);
        System.out.println(obj.printCardInfo());
        obj = (ObjectiveCard) objectives.get(commonObjectives[1]-87);
        System.out.println(obj.printCardInfo());
    }

    /**
     * Prints the secret objective or the secret objective choices depending on whether the objective was already chosen
     */
    public void showSecretObjectives(){
        List<Card> objectives;
        try {
            objectives=Creation.getObjectiveCards();
        }catch (IOException e){
            throw new RuntimeException();
        }
        ObjectiveCard obj = (ObjectiveCard) objectives.get(secretObjectiveChoices[0]-87);
        System.out.println(obj.printCardInfo());
        if(secretObjectiveChoices.length>1){
            obj = (ObjectiveCard) objectives.get(secretObjectiveChoices[1]-87);
            System.out.println(obj.printCardInfo());
        }
        System.out.println("\n\n");
    }

    /**
     * Prints the leaderboard as requested by the player
     */
    public void printScoreTrack() {
        scoreTrack.printTable();
    }

    /**
     * Displays the final leaderboard
     * @param finalPlayerScore
     */
    public void displayWinners(HashMap<String, Integer> finalPlayerScore) {
        for(String player: finalPlayerScore.keySet()){
            System.out.println(player+": "+finalPlayerScore.get(player)+" points");
        }
    }
    /**
     * Prints the hand of the player
     */
    public void printHand() {
        System.out.println("You have these following cards in your hand:\n");
        for(Integer i: playerHand){
            PlayableCard card = (PlayableCard) cardList.get(i-1);
            String[] asciiFront= card.asciiArtFront();
            String[] asciiBack= card.asciiArtBack();
            System.out.println("ID: "+i);
            System.out.println("|"+asciiFront[0]+"|     |"+asciiBack[0]+"|");
            System.out.println("|"+asciiFront[1]+"|     |"+asciiBack[1]+"|");
            System.out.println("|"+asciiFront[2]+"|     |"+asciiBack[2]+"|\n");
        }
    }

    /**
     * Displays the starting card for the player to see
     */
    public void printStartingCard() {
        StartingCard startingCard= (StartingCard) cardList.get(startingCardID-1);
        System.out.println("Here is your starting card:\nID: "+startingCardID);
        System.out.println("|"+startingCard.asciiArtFront()[0]+"|     |"+startingCard.asciiArtBack()[0]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[1]+"|     |"+startingCard.asciiArtBack()[1]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[2]+"|     |"+startingCard.asciiArtBack()[2]+"|\n");
    }
}
