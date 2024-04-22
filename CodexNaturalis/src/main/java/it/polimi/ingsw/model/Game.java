package it.polimi.ingsw.model;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.GameListener;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.clientToServer.DrawCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;

import java.util.*;

public class Game {
    public int numPlayers;
    public int currentPlayerIndex;
    private Deck objectiveDeck;
    private Deck goldDeck;
    private Deck resourceDeck;
    public List<Player> players;
    private ScoreTrack scoreTrack;
    private GameListener gameListener;

    public Game(int num, GameListener gameListener){
        this.numPlayers = num;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameListener = gameListener;
    }

    public void startGame(String username1, String username2, String username3, String username4) throws Exception, IllegalStartingCardException {
        setupScoreTrack(username1, username2, username3, username4);
        setupDecks();
        setupPlayers(username1, username2, username3, username4);
    }

    /**
     * Set-up the ScoreTrack based on the number of player of each game
     * @param username1 first player name
     * @param username2 second player name
     * @param username3 third player name
     * @param username4 fourth player name
     */

    private void setupScoreTrack(String username1, String username2, String username3, String username4){
        switch (numPlayers) {
            case 2:
                scoreTrack = new ScoreTrack(username1, username2, this);
                break;
            case 3:
                scoreTrack = new ScoreTrack(username1, username2, username3, this);
                break;
            case 4:
                scoreTrack = new ScoreTrack(username1, username2, username3, username4, this);
                break;
            default:
                System.out.println("Invalid number of players!");
        }
    }

    /**
     * This method aims to create the decks for the different types of cards
     * Shuffles them and set the two visibleCards.
     * For the objectiveDeck the two visibles are in fact the common objectives
     * @throws Exception
     */

    private void setupDecks() throws Exception {
        this.goldDeck = new Deck(Creation.getGoldCards());
        this.objectiveDeck = new Deck(Creation.getObjectiveCards());
        this.resourceDeck = new Deck(Creation.getResourceCards());
        resourceDeck.shuffle();
        resourceDeck.setFirstVisible();
        resourceDeck.setSecondVisible();
        goldDeck.shuffle();
        goldDeck.setFirstVisible();
        goldDeck.setSecondVisible();
        objectiveDeck.shuffle();
        objectiveDeck.setFirstVisible();
        objectiveDeck.setSecondVisible();
    }

    /**
     * This method is called to add players to the game and to deal them their own secretObjectives
     * @param username1 first player name
     * @param username2 second player name
     * @param username3 third player name
     * @param username4 fourth player name
     * @throws Exception
     * @throws IllegalStartingCardException
     */

    private void setupPlayers(String username1, String username2, String username3, String username4) throws Exception, IllegalStartingCardException {
        /*manages the init phase of the game, players are added, startingCards are dealt
           and the player has to decide between the secretObjective dealt*/
        String[] currentUsername = {username1, username2, username3, username4};
        List<Card> startingCards = Creation.getStartingCards();
        Collections.shuffle(startingCards);
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(currentUsername[i], (PlayableCard) startingCards.get(i),(PlayableCard[])drawInitialCards());
            players.add(player);
            //dealSecretObjective(player);
        }
    }

    /**
     * This method deals the initial hand to each player
     * @return drawnCard that is the startingCard of the player
     * @throws Exception
     */

    private Card[] drawInitialCards() throws Exception {
        Card[] drawnCard = new Card[3];
        drawnCard[0]= resourceDeck.draw(0);
        drawnCard[1]=resourceDeck.draw(0);
        drawnCard[2]=goldDeck.draw(0);
        return drawnCard;
        //player draws 2 resource cards and 1 gold one
    }

    /**
     * Give the player two objectiveCards
     * @param player
     * @return objectiveOptions that are two objectiveCards and the player has to choose one of them
     * @throws Exception
     */
/*TODO: this method must be called in coordination with the controller so that the following sequence happens:
   1)The controller gets two random objectives for each player (and stores them until a valid answer is received)
   2)The objectives are sent to the player
   3)The player chooses the objective
   4)The controller calls the placeSecretObjective method for each player
*/
    public ObjectiveCard[] dealSecretObjective(Player player) throws Exception {
        ObjectiveCard[] objectiveOptions = null;
        for (int i = 0; i < 2; i++) {
            assert objectiveOptions != null;
            objectiveOptions[i] = (ObjectiveCard) objectiveDeck.draw(0);
        }
        return  objectiveOptions;
    }

    /**
     * This method receives the chosen secretObjective and sets it to the player
     * @param username player name
     * @param secretObjective that is the card chosen by the player
     */

    private void placeSecretObjective(String username, ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
        getPlayerFromUser(username).setSecretObjective(secretObjective);
    }

    /**
     *
     * Method to manage the draw phase. The player can draw from resource or gold deck or from one the visibleCards
     * If there aren't any cards left in both decks, the method calls the gameOver method that notify the ending of
     * the game to the gameListener interface
     *
     * @param playerName username of the player
     * @param message gives the infos about the type of card the player wants to draw
     * @throws Exception
     */
    public void drawCard(String playerName, DrawCardMessage message) throws Exception {

        Player player = null;
        Card drawncard=null;

        player = getPlayerFromUser(playerName);
        PlayerDrawChoice choice = message.getChoice();

        if(choice== PlayerDrawChoice.goldDeck) {
            drawncard = goldDeck.draw(0);
        }else if(choice== PlayerDrawChoice.goldFirstVisible){
            drawncard = goldDeck.draw(1);
            goldDeck.setVisibleAfterDraw(resourceDeck);
        } else if(choice== PlayerDrawChoice.goldSecondVisible) {
            drawncard = goldDeck.draw(2);
            goldDeck.setVisibleAfterDraw(resourceDeck);
        }else if(choice== PlayerDrawChoice.resourceDeck) {
            drawncard = resourceDeck.draw(0);
        }else if(choice== PlayerDrawChoice.resourceFirstVisible){
            drawncard = resourceDeck.draw(1);
            resourceDeck.setVisibleAfterDraw(goldDeck);
        } else if(choice== PlayerDrawChoice.resourceSecondVisible) {
            drawncard = resourceDeck.draw(2);
            resourceDeck.setVisibleAfterDraw(goldDeck);
        }else{
            System.out.println("Invalid draw choice");
        }
        player.receiveDrawnCard((PlayableCard) drawncard);
        if(goldDeck.getNumRemaining()==0&&resourceDeck.getNumRemaining()==0){
            gameOver();
        }
    }


    /**
     *  Method to manage the placing the of the card during the game round.
     * @param playerName player username
     * @param message Contains the infos on where the player wants to place the card.
     * @throws Exception
     */
    public void playCard(String playerName, PlaceCardMessage message) throws Exception {

        Player currentplayer = null;
        currentplayer = getPlayerFromUser(playerName);

        int cardX = message.getX();
        int cardY = message.getY();
        boolean cardFace = message.isFacingUp();
        int cardID = message.getID();

        currentplayer.placeCard(cardID,cardX,cardY,cardFace,scoreTrack);

    }

    /**
     * Given the username, this method extract the player object
     * @param username
     * @return currentPlayer
     */

    private Player getPlayerFromUser(String username){
        Player currentPlayer = null;
        for(Player compPlayer: players)
            if(compPlayer.getName().equals(username)) {
                currentPlayer=compPlayer;
                break;
            }
        return currentPlayer;
    }



    /**
     *  Calculate the points in the end phase of the game by adding the points given by private and common objectives
     * @throws IllegalStateException
     */
    public void calculateFinalPoints() throws IllegalStateException {

            Card common1 = objectiveDeck.getFirstVisible();
            Card common2 = objectiveDeck.getSecondVisible();
        for(Player player: players){
            player.calculateSecretObjective();
            player.calculateCommonObjectives((ObjectiveCard) common1, (ObjectiveCard) common2);
            int totalPoints = player.getPoints();
            String playerName = player.getName();
        }
    }



    /**
     * Declare Winner based on the dinamic list of players (that can have 2 or 3 or 4 elements).
     * The only information that is saved of the winner is the name.
     * In case of "multiple winners", they can all be found in the List of names "idWinners".
     */
    public void declare_winner(){
        List<String> idWinners = new ArrayList<>();
        idWinners.addFirst(players.getFirst().getName());

        //NB: "i-1" instead of "i+1" is important so the "OutOfIndex" error should never happen in any circumstance
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getPoints() == players.get(i - 1).getPoints()) { idWinners.add(players.get(i).getName()); }
            else if (players.get(i).getPoints() > players.get(i - 1).getPoints()) {
                idWinners.clear();
                idWinners.add(players.get(i).getName());
            }
        }
    }

    /**
     *
     * @return next_Winner
     */

    public String get_next_winner_name(){
        String next_winner = players.getFirst().getName();
        players.removeFirst();
        return next_winner;
    }

    /**
     * this method is called by the scoreTrack class if one the players has reached 20 points or by this class
     * if there are no cards left to draw.
     */

    public void gameOver(){
        gameListener.startFinalPhase();
    }

    /**
     *
     * @return a copy of the scoreTrack to be sent to the clients
     */
    public ScoreTrack getScoreTrack() {
        return scoreTrack.copyScoreTrack();
    }

    /**
     *
     * @param username the player whose information is returned
     * @return a list containing all possible positions on which a card could be placed
     * @throws NotPlacedException if an error has occurred during the placement of a card
     * @throws PlayerCantPlaceAnymoreException if the player has blocked all possible future moves
     */
    public List<Point> getAvailablePoints(String username) throws NotPlacedException, PlayerCantPlaceAnymoreException {
        return getPlayerFromUser(username).getAvailablePositions();
    }

    /**
     *
     * @param username the player whose information is returned
     * @return a list of the player's hand cards' ids
     */
    public List<Integer> getPlayerHand(String username){
        List<Integer> handID= new ArrayList<>();
        for(PlayableCard card:getPlayerFromUser(username).viewCurrentHand() ){
            handID.add(card.getID());
        }
        return  handID;
    }

    /**
     * @param username the player whose information is returned
     * @param isFacingUp chosen side
     * @throws AlreadyPlacedException if the card has already been initialized elsewhere
     * @throws NotPlacedException if the initialization failed
     */
    public void placeStartingCard(String username,boolean isFacingUp) throws NotPlacedException, AlreadyPlacedException {
        getPlayerFromUser(username).placeStartingCard(isFacingUp);
    }
}