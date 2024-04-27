package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

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
    private PlayerFieldView ownerField=null;
    private final List<Integer> playerHand;
    private final int startingCardID;
    private HashMap<String , PlayerFieldView> opponentFields;
    private String currentPlayer;
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
    }
}
