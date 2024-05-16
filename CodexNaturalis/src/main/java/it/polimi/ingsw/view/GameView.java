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


/**
 * Class used to represent the state of the Game, it's created after the Game has started
 */
public class GameView {
    /**
     * Contains all the cards, accessed by the other view elements
     */
    protected static List<Card> cards;
    private  String playerName;
    private ImmutableScoreTrack scoreTrack;
    private  DeckView goldDeck;
    private  DeckView resourceDeck;
    private List<Integer> playerHand;
    private PlayerFieldView ownerField;
    private int startingCardID;
    private final HashMap<String , PlayerFieldView> opponentFields= new HashMap<>();
    private int[] secretObjectiveChoices= new int[2];
    private final int[] commonObjectives= new int[2];
    public GameView(){
        try {
            cards=Creation.getResourceCards();
            cards.addAll(Creation.getGoldCards());
            cards.addAll(Creation.getStartingCards());
            cards.addAll(Creation.getObjectiveCards());
        }catch (IOException e){
            System.err.println("Error while getting the cards for the view");
            throw new RuntimeException();
        }
    }
/**
 * After the constructor the methods to update the decks must be called by the controller with the necessary information
 */
    public void gameStarting( List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective) throws IOException {
        this.playerName = playerName;
        startingCardID=startingCard;
        this.goldDeck = new DeckView("Gold");
        this.resourceDeck = new DeckView("Resource");
        HashMap<String ,Integer> startingScoreTrack=  new HashMap<>();
        for(String s: otherPlayerNames){
            if(!opponentFields.containsKey(s)) {
                opponentFields.put(s, new PlayerFieldView());
            }
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
        if(!opponentFields.containsKey(name)){
            try {
                opponentFields.put(name, new PlayerFieldView());
            }catch (IOException e){
                System.err.println("\n\nIOException while creating a field view\n");
                throw new RuntimeException();
            }
        }
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
        scoreTrack.printTable();
        showCommonObjectives();
        String[] goldTemp= goldDeck.printDeck();
        String[] resourceTemp= resourceDeck.printDeck();
        System.out.println(goldTemp[0]+" ".repeat(
                30)+resourceTemp[0]);
        System.out.println(goldTemp[1]+" ".repeat(
                goldTemp[1].equals("The deck has no more cards in it.")?7:11)+resourceTemp[1]);
        System.out.println(goldTemp[2]+" ".repeat(
                goldTemp[2].equals("There is no first visible card")?10:17)+resourceTemp[2]);
        System.out.println(goldTemp[3]+" ".repeat(
                29)+resourceTemp[3]);
        System.out.println(goldTemp[4]+" ".repeat(
                29)+resourceTemp[4]);
        System.out.println(goldTemp[5]+" ".repeat(
                29)+resourceTemp[5]);
        System.out.println(goldTemp[6]+" ".repeat(
                goldTemp[6].equals("There is no second visible card")?9:5)+resourceTemp[6]);
        System.out.println(goldTemp[7]+" ".repeat(
                29)+resourceTemp[7]);
        System.out.println(goldTemp[8]+" ".repeat(
                29)+resourceTemp[8]);
        System.out.println(goldTemp[9]+" ".repeat(
                29)+resourceTemp[9]);
    }
    /**
     * Prints the client's field for the CLI
     */
    public void printOwnerField(){
        if(ownerField==null){
            System.out.println("Your field is currently empty");
        }
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
     * Prints the hand of the player. 
     */
    public void printHand() {
        System.out.println("You have these following cards in your hand:\n");
        StringBuilder[] hand= new StringBuilder[9];
        for(int i=0;i<hand.length;i++){
            hand[i]= new StringBuilder();
        }
        for(Integer i: playerHand){
            String[] cardAsciiFront= printCardAsciiFront(i);
            String[] cardAsciiBack= printCardAsciiBack(i);
            hand[0].append("ID: ").append(i).append("         ");
            hand[1].append("Front:").append("        ");
            hand[2].append("|").append(cardAsciiFront[0]).append("|").append("   ");
            hand[3].append("|").append(cardAsciiFront[1]).append("|").append("   ");
            hand[4].append("|").append(cardAsciiFront[2]).append("|").append("   ");
            hand[5].append("Back:").append("         ");
            hand[6].append("|").append(cardAsciiBack[0]).append("|").append("   ");
            hand[7].append("|").append(cardAsciiBack[1]).append("|").append("   ");
            hand[8].append("|").append(cardAsciiBack[2]).append("|").append("   ");
        }
        for (StringBuilder stringBuilder : hand) {
            System.out.println(stringBuilder);
        }
    }

    /**
     * Displays the starting card for the player to see
     */
    public void printStartingCard() {
        StartingCard startingCard= (StartingCard) cards.get(startingCardID-1);
        System.out.println("Here is your starting card:\nID: "+startingCardID);
        System.out.println("Front:          Back:");
        System.out.println("|"+startingCard.asciiArtFront()[0]+"|     |"+startingCard.asciiArtBack()[0]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[1]+"|     |"+startingCard.asciiArtBack()[1]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[2]+"|     |"+startingCard.asciiArtBack()[2]+"|\n");
    }

    /**
     * Array of 3 Strings
     * Prints the card with the specified id's front
     * */
    static String[] printCardAsciiFront(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id-1);
        return pc.asciiArtFront();
    }
    /**
     * Array of 3 Strings
     * Prints the card with the specified id's back
     * */
    static String[] printCardAsciiBack(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id-1);
        return pc.asciiArtFront();
    }
    /**
     * @param id of the requested card
     * @return the detailed data on the card with the given id
     */
    static String printCardDetailed(int id){
        return GameView.cards.get(id-1).printCardInfo();
    }
}
