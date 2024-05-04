package it.polimi.ingsw.view;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to represent the state of the Game, it's created after the Game has started
 */
public class GameView {
    private final String playerName;
    private ImmutableScoreTrack scoreTrack;
    private final DeckView goldDeck;
    private final DeckView resourceDeck;

    private PlayerFieldView ownerField;
    private final int startingCardID;
    private final HashMap<String , PlayerFieldView> opponentFields;
    private String currentPlayer;
    private int[] secretObjectiveChoices= new int[2];
    private int[] commonObjectives= new int[2];
/**
 * After the constructor the methods to update the decks must be called by the controller with the necessary information
 */
    public GameView(List<Integer> playerHand, List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective) throws IOException {
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
        ownerField.updateHand(new ArrayList<>(playerHand));
    }
    /**
     * Updates the name of the current player to show whose turn it is
     * @param currentPlayer is the new current player
     */
    public void updateCurrentPlayer(String currentPlayer){
        if(!opponentFields.containsKey(currentPlayer)&&!currentPlayer.equals(playerName)){
            return;
        }
        this.currentPlayer=currentPlayer;
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
        scoreTrack.printTable();
        System.out.println("----------------------------------------------------------------------------------------------------");
        showCommonObjectives();
        System.out.println("----------------------------------------------------------------------------------------------------");
        resourceDeck.printDeck();
        System.out.println("----------------------------------------------------------------------------------------------------");
        goldDeck.printDeck();
        System.out.println("It's "+currentPlayer+"'s turn");
    }
    /**
     * Prints the client's field for the CLI
     */
    public void printOwnerField(){
        System.out.println("Your field:");
        ownerField.printField();
    }
    /**
     * Prints the requested player's field for the CLI
     * @param name name of the opponent whose field will be shown
     */
    public void printOpponentField(String name){
        if(!opponentFields.containsKey(name)){
            showText("Incorrect player name.");
            return;
        }
        System.out.println(name +"'s field:");
        opponentFields.get(name).printField();
    }

    /**This method should be called after a player has placed a card, to update for the removal of the card placed,
     *  and  after a player has drawn to update for the addition of the new card
     * @param playerHand the hand after the player has made a move
     */
    public void updatePlayerHand(List<Integer> playerHand){
        ownerField.updateHand(new ArrayList<>(playerHand));
    }

    /**
     * Removes the card with id lastPlayed from the hand of the player
     * @param lastPlayed the id of the card that has been placed
     */
    public void updatePlayerHand(int lastPlayed){
        ownerField.updateHand(lastPlayed);
    }

    /**
     * Returns the hand of the player. Used to check if the card they want to place is in their possession
     */
    public List<Integer> getPlayerHand(){
        return new ArrayList<>(ownerField.getPlayerHand());
    }
    /**
     * This method prints the hand of a player.
     */
    public void printHand(){
        ownerField.printHand();
    }
    public void updateAvailablePositions(List<Point> availablePositions){
        ownerField.updateAvailablePositions(availablePositions);
    }

    /**
     * Method that prints a message as either CLI or GUI according to how the program was started
     */
    public void showText(String s) {
        System.out.println(s);
    }

    /**
     * This method memorizes the two possible choices and then shows them
     * @param firstChoice first objective choice
     * @param secondChoice second objective choice
     */
    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
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
        obj.printCardInfo();
        obj = (ObjectiveCard) objectives.get(commonObjectives[1]-87);
        obj.printCardInfo();
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
        obj.printCardInfo();
        if(secretObjectiveChoices.length>1){
            obj = (ObjectiveCard) objectives.get(secretObjectiveChoices[1]-87);
            obj.printCardInfo();
        }
    }

    /**
     * Prints the leaderboard as requested by the player
     */
    public void printScoreTrack() {
        scoreTrack.printTable();
    }
}
