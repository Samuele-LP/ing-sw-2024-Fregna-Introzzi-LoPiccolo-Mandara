package it.polimi.ingsw.view;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

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
    private final DeckView objectiveDeck;
    private PlayerFieldView ownerField;
    private List<Integer> playerHand;
    private final int startingCardID;
    private final HashMap<String , PlayerFieldView> opponentFields;
    private String currentPlayer;
    private int secretObjective;
/**
 * After the constructor the methods to update the decks must be called by the controller with the necessary information
 */
    public GameView(List<Integer> playerHand, List<String> otherPlayerNames, String playerName, int startingCard) {
        this.playerName = playerName;
        startingCardID=startingCard;
        this.goldDeck = new DeckView("Gold");
        this.resourceDeck = new DeckView("Resource");
        this.objectiveDeck = new DeckView("Objective");
        this.playerHand = playerHand;
        opponentFields=new HashMap<>();
        HashMap<String ,Integer> startingScoreTrack=  new HashMap<>();
        for(String s: otherPlayerNames){
            opponentFields.put(s,new PlayerFieldView());
            startingScoreTrack.put(s,0);
        }
        scoreTrack=new ImmutableScoreTrack(startingScoreTrack);
        ownerField=new PlayerFieldView();
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
        opponentFields.get(name).update(placeID,placedX,placedY,isFacingUp,visibleSymbols);
    }
    /**Adds the card the owner of the field just placed
     * @param placedID ID of the placed card
     * @param placedX xPosition
     * @param placedY yPosition*/
    public void updateOwnerField(int placedID, int placedX, int placedY,boolean isFacingUp, Map<TokenType,Integer> visibleSymbols){
        ownerField.update( placedID,  placedX,  placedY,isFacingUp,visibleSymbols);
    }

    /**
     * Method that prints information about the scoreTrack for the cli
     */
    public void printCommonField(){
        scoreTrack.printTable();
        System.out.println("----------------------------------------------------------------------------------------------------");
        objectiveDeck.printDeck();
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
        System.out.println(name +"'s field:");
        opponentFields.get(name).printField();
    }

    /**This method should be called after a player has placed a card, to update for the removal of the card placed,
     *  and  after a player has drawn to update for the addition of the new card
     * @param playerHand the hand after the player has made a move
     */
    public void updatePlayerHand(List<Integer> playerHand){
        this.playerHand= new ArrayList<>(playerHand);
    }

    /**
     * This method prints the hand of a player.
     */
    public void printHand(){
        System.out.print("In your hand you have the following cards with ID: ");
        for(Integer ID: playerHand){
            System.out.print("-"+ID+"-");
        }
    }

    /**
     * Adds information about the secret objective card chosen by the player
     */
    public void setSecretObjective(int id){
        secretObjective=id;
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

    public void secretObjectiveChoice(int firstChoice, int secondChoice) {
        System.out.println("The first objective is: ");
        System.out.println("The second objective is: ");
    }
}
