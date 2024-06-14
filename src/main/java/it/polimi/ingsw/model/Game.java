package it.polimi.ingsw.model;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.clientToServer.DrawCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.*;
/**
 * Class that contains all the information about a single game and provides the methods to access and update such information
 */
public class Game {
    public int numPlayers;
    private Deck objectiveDeck;
    private Deck goldDeck;
    private Deck resourceDeck;
    public List<Player> players;
    private ScoreTrack scoreTrack;
    private boolean isInFinalPhase;
    private final ArrayList<String> winners= new ArrayList<>();

    /**
     * Game constructor
     * @param num number of players
     */
    public Game(int num){
        this.numPlayers = num;
        this.players = new ArrayList<>();
        this.isInFinalPhase=false;
    }

    /**
     * Initialization of the game.
     * Creates the deck, the scoreTrack, the Players and deals the starting cards
     * @param players is the list of players' names
     * @throws Exception if an error occurred during setUp
     * @throws IllegalStartingCardException if a non-starting card is being set as a starting card
     */
    public void startGame(List<String> players) throws Exception, IllegalStartingCardException {
        setupScoreTrack(players);
        setupDecks();
        setupPlayers(players);
    }

    /**
     * Sets-up the ScoreTrack based on the number of player of each game
     * @param players is the list of players' names
     */
    private void setupScoreTrack(List<String> players){
        switch (numPlayers) {
            case 2:
                scoreTrack = new ScoreTrack(players.get(0), players.get(1));
                break;
            case 3:
                scoreTrack = new ScoreTrack(players.get(0), players.get(1),players.get(2));
                break;
            case 4:
                scoreTrack = new ScoreTrack(players.get(0), players.get(1),players.get(2),players.get(3));
                break;
            default:
                System.out.println("Invalid number of players!");
        }
    }
    /**
     * This method creates the decks for the different types of cards
     * Shuffles them and set the two visibleCards.
     * For the objectiveDeck the two visible cards are the common objectives
     * @throws Exception if an error occurs during set up
     */
    private void setupDecks() throws Exception {
        this.goldDeck = new Deck(Creation.getInstance().getGoldCards());
        this.objectiveDeck = new Deck(Creation.getInstance().getObjectiveCards());
        this.resourceDeck = new Deck(Creation.getInstance().getResourceCards());
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
     * @param playersList the list of players' names
     * @throws Exception if an error occurred during setUp
     * @throws IllegalStartingCardException if a non-starting card is being set as a starting card
     */
    private void setupPlayers(List<String>playersList) throws Exception, IllegalStartingCardException {
        /*manages the init phase of the game, players are added, startingCards are dealt
           and the player has to decide between the secretObjective dealt*/
        List<Card> startingCards = Creation.getInstance().getStartingCards();
        Collections.shuffle(startingCards);
        for (String s:playersList) {
            Player player = new Player(s, (PlayableCard) startingCards.get(playersList.indexOf(s)), drawInitialCards());
            this.players.add(player);
        }
    }

    /**
     * This method deals the initial hand to each player
     * @return drawnCard that is the startingCard of the player
     * @throws Exception if a fatal error has occurred
     */

    private PlayableCard[] drawInitialCards() throws Exception {
        PlayableCard[] drawnCard = new PlayableCard[3];
        drawnCard[0]= (PlayableCard) resourceDeck.draw(0);
        drawnCard[1]= (PlayableCard) resourceDeck.draw(0);
        drawnCard[2]= (PlayableCard) goldDeck.draw(0);
        return drawnCard;
        //player draws 2 resource cards and 1 gold one
    }

    /**
     * @throws NotPlacedException if an error occurs during placement: the starting has not been correctly initialized
     * @throws AlreadyPlacedException if the starting card has already been initialized
     */
    public void setStartingCard(String playerName, boolean isFacingUp) throws NotPlacedException, AlreadyPlacedException {
        Player player = getPlayerFromUser(playerName);
        player.placeStartingCard(isFacingUp);
    }

    /**
     * Give the player two objectiveCards
     * @return objectiveOptions that are two objectiveCards and the player has to choose one of them
     * @throws Exception if the objective deck is not correctly initialized
     */
    public ObjectiveCard[] dealSecretObjective() throws Exception {
        ObjectiveCard[] objectiveOptions = new ObjectiveCard[2];
        for (int i = 0; i < 2; i++) {
            objectiveOptions[i] = (ObjectiveCard) objectiveDeck.draw(0);
        }
        return  objectiveOptions;
    }

    /**
     * This method receives the chosen secretObjective and sets it to the player
     * @param username player name
     * @param secretObjective that is the card chosen by the player
     */

    public void placeSecretObjective(String username, ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
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
     */
    public void drawCard(String playerName, DrawCardMessage message) throws EmptyDeckException, NoVisibleCardException, CardAlreadyPresentException, HandAlreadyFullException {

        Card drawncard=null;

        Player player = getPlayerFromUser(playerName);
        PlayerDrawChoice choice = message.getChoice();
        try {
            if (choice == PlayerDrawChoice.goldDeck) {
                drawncard = goldDeck.draw(0);
            } else if (choice == PlayerDrawChoice.goldFirstVisible) {
                drawncard = goldDeck.draw(1);
                goldDeck.setVisibleAfterDraw(resourceDeck);
            } else if (choice == PlayerDrawChoice.goldSecondVisible) {
                drawncard = goldDeck.draw(2);
                goldDeck.setVisibleAfterDraw(resourceDeck);
            } else if (choice == PlayerDrawChoice.resourceDeck) {
                drawncard = resourceDeck.draw(0);
            } else if (choice == PlayerDrawChoice.resourceFirstVisible) {
                drawncard = resourceDeck.draw(1);
                resourceDeck.setVisibleAfterDraw(goldDeck);
            } else if (choice == PlayerDrawChoice.resourceSecondVisible) {
                drawncard = resourceDeck.draw(2);
                resourceDeck.setVisibleAfterDraw(goldDeck);
            }
        }catch (CantReplaceVisibleCardException e){
            setInFinalPhase();
        }
        if(goldDeck.getNumRemaining()==0&&resourceDeck.getNumRemaining()==0){
            setInFinalPhase();
        }
        player.receiveDrawnCard((PlayableCard) drawncard);
    }


    /**
     *  Method to manage the placing the of the card during the game round.
     * @param playerName player username
     * @param message Contains the infos on where the player wants to place the card.
     */
    public void playCard(String playerName, PlaceCardMessage message) throws InvalidPositionException, NotPlacedException, AlreadyPlacedException, NotEnoughResourcesException, CardNotInHandException {

        Player currentplayer;
        currentplayer = getPlayerFromUser(playerName);

        int cardX = message.getX();
        int cardY = message.getY();
        boolean cardFace = message.isFacingUp();
        int cardID = message.getID();

        currentplayer.placeCard(cardID,cardX,cardY,cardFace,scoreTrack);
        if(scoreTrack.doesFinalPhaseStart()){
            setInFinalPhase();
        }
    }

    /**
     * Given the username, this method extract the player object
     * @param username player name
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
     * @throws IllegalStateException if the method is invoked at the incorrect time
     */
    private void calculateFinalPoints() throws IllegalStateException {
            Card common1 = objectiveDeck.getFirstVisible();
            Card common2 = objectiveDeck.getSecondVisible();
        for(Player player: players){
            player.calculateSecretObjective();
            player.calculateCommonObjectives((ObjectiveCard) common1, (ObjectiveCard) common2);
        }
    }
    /**
     *
     * @return a copy of the scoreTrack to be sent to the clients
     */
    public ImmutableScoreTrack getScoreTrack() {
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
     * @param name the name of the player whose symbols are requested
     * @return how many of each visible symbols are there
     */
    public Map<TokenType,Integer> getPlayerVisibleSymbols(String name){
            return getPlayerFromUser(name).viewVisibleSymbols();
    }

    /**
     * Setter for finalPhase
     */
    private void setInFinalPhase() {
        isInFinalPhase = true;
    }

    /**
     * Method to check if the finalPhase is started
     * @return isInFinalPhase true if it's in the finalPhase, false otherwise
     */
    public boolean isInFinalPhase() {
        return isInFinalPhase;
    }

    /**
     * @return a list of the winners of the game. It has size=0 before the final phase and size>1 only if
     *two or more players scored both the same number of points and the same number of objectives
     */
    public List<String> getWinners(){
        return new ArrayList<>(winners);
    }

    /**
     * @return first common objective i
     */
    public int getFirstCommonObjective(){
        return objectiveDeck.getFirstVisible().getID();
    }

    /**
     * @return second common objective id
     */
    public int getSecondCommonObjective(){
        return objectiveDeck.getSecondVisible().getID();
    }

    /**
     * @param playerName is the username of the player
     * @return the ID of the startingCard of the player
     */
    public int getStartingCardId(String playerName){
        Player player = getPlayerFromUser(playerName);
        return player.getStartingCard().getID();
    }

    /**
     *Used to get information on the top card of the deck, to be used by the view to determine what to show
     * @return null if the deck is empty the CardType attribute of the top card otherwise
     */
    public CardType getGoldTop(){
        return goldDeck.getTopCardColour();
    }

    /**
     *Used to get information on the top card of the deck, to be used by the view to determine what to show
     * @return null if the deck is empty the CardType attribute of the top card otherwise
     */
    public CardType getResourceTop(){
        return resourceDeck.getTopCardColour();
    }
    public List<Integer> getVisibleCards(){
        List <Integer> visibleCards = new ArrayList<>();
        Card c= resourceDeck.getFirstVisible();
        visibleCards.add(c==null?null:c.getID());
        c= resourceDeck.getSecondVisible();
        visibleCards.add(c==null?null:c.getID());
        c= goldDeck.getFirstVisible();
        visibleCards.add(c==null?null:c.getID());
        c= goldDeck.getSecondVisible();
        visibleCards.add(c==null?null:c.getID());
        return visibleCards;
    }

    /**
     * @param name it's the player's name
     * @param colour is the player's pawn colour
     */
    public void setPawnColour(String name, String colour){
        scoreTrack.setPawnColor(name, colour);
    }

    /**
     *
     * @param connectedPlayers is the list of names that will be included in the winners
     * @return the list of winners excluding the disconnected players
     */
    public List<String> getWinnersAfterDisconnection(Collection<String> connectedPlayers) {
        updateWinners(connectedPlayers);
        return winners;
    }
    /**
     * This method does the final calculations about the game, it updates the list of winners. It's called when there is
     * a disconnection or when the final round counter reaches 0.
     */
    public void gameOver(){
        calculateFinalPoints();
        updateWinners(new ArrayList<>());
    }

    /**
     * Updates the list of winners with this policy:<br>
     * If a single player has the maximum number of points he is the winner.<br>
     * If there are multiple players tied for first place then the winner is decided by the number of scored objectives<br>
     * If the number of scored objectives is also tied then the game will end in a tie<br>
     * @param toBeIncluded contains the names of players that are not disconnected and will be counted as winners
     */
    private void updateWinners(Collection<String> toBeIncluded) {
        int highestScore=-1;
        for (Player p : players) {
            if(toBeIncluded.contains(p.getName())) {
                int points = p.getPoints();
                scoreTrack.updateScoreTrack(p.getName(), points);
                if (points > highestScore) {
                    highestScore = points;
                    winners.clear();
                    winners.add(p.getName());
                } else if (points == highestScore) {// "if" entered only if there is at least one player in "winners"

                    //If p has more objectives than a player in "winners" then it has more objectives than all of them, so the list is reinitialized
                    if (p.getNumberOfScoredObjectives() > getPlayerFromUser(winners.getFirst()).getNumberOfScoredObjectives()) {
                        winners.clear();
                        winners.add(p.getName());
                        //a second/third/fourth person is added in winners only if they have the same number of objectives scored
                    } else if (p.getNumberOfScoredObjectives() == getPlayerFromUser(winners.getFirst()).getNumberOfScoredObjectives()) {
                        winners.add(p.getName());
                    }
                }
            }
        }
    }
}