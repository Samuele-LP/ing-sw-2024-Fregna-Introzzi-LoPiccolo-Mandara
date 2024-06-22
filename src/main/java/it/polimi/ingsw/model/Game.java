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
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import it.polimi.ingsw.view.ImmutableScoreTrack;

import java.util.*;

/**
 * Class that contains all the information about a single game and provides the methods to access and update such information
 */
public class Game {
    private final int numPlayers;
    private Deck objectiveDeck;
    private Deck goldDeck;
    private Deck resourceDeck;
    private final List<Player> players;
    private ScoreTrack scoreTrack;
    private boolean isInFinalPhase;
    private final ArrayList<String> winners= new ArrayList<>();

    /**
     * Game constructor
     *
     * @param num number of players
     */
    public Game(int num){
        this.numPlayers = num;
        this.players = new ArrayList<>();
        this.isInFinalPhase=false;
    }

    /**
     * Initializes the game by creating the deck, the scoreTrack, the Players and dealing the starting cards.
     *
     * @param players the list of players' names
     * @throws Exception if an error occurs during setup
     * @throws IllegalStartingCardException if a non-starting card is being set as a starting card
     */
    public void startGame(List<String> players) throws Exception, IllegalStartingCardException {
        setupScoreTrack(players);
        setupDecks();
        setupPlayers(players);
    }

    /**
     * Sets up the ScoreTrack based on the number of players in the game.
     *
     * @param players the list of players' names
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
     * Creates the decks for different types of cards, shuffles them, and sets the two visible cards.
     * For the objectiveDeck, the two visible cards are the common objectives.
     *
     * @throws Exception if an error occurs during setup
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
     * Adds players to the game and deals them their secret objectives.
     *
     * @param playersList the list of players' names
     * @throws Exception if an error occurs during setup
     * @throws IllegalStartingCardException if a non-starting card is being set as a starting card
     */
    private void setupPlayers(List<String>playersList) throws Exception, IllegalStartingCardException {
        /*manages the init phase of the game, players are added, startingCards are dealt
           and the player has to decide between the secretObjective dealt*/
        List<Card> startingCards = Creation.getInstance().getStartingCards();
        Collections.shuffle(startingCards);
        for (String s : playersList) {
            Player player = new Player(s, (PlayableCard) startingCards.get(playersList.indexOf(s)), drawInitialCards());
            this.players.add(player);
        }
    }

    /**
     * Deals the initial hand to each player.
     *
     * @return drawnCard the starting cards of the player
     * @throws Exception if a fatal error occurs
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
     * Sets the starting card for the player.
     *
     * @param playerName the player's name
     * @param isFacingUp true if the card is facing up, false otherwise
     * @throws NotPlacedException if an error occurs during placement: the starting card has not been correctly initialized
     * @throws AlreadyPlacedException if the starting card has already been initialized
     */
    public void setStartingCard(String playerName, boolean isFacingUp) throws NotPlacedException, AlreadyPlacedException {
        Player player = getPlayerFromUser(playerName);
        player.placeStartingCard(isFacingUp);
    }

    /**
     * Deals two objective cards to the player.
     *
     * @return objectiveOptions the two objective cards for the player to choose from
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
     * Sets the chosen secret objective for the player.
     *
     * @param username the player's name
     * @param secretObjective the chosen secret objective card
     */
    public void placeSecretObjective(String username, ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
        getPlayerFromUser(username).setSecretObjective(secretObjective);
    }

    /**
     * Manages the draw phase. The player can draw from the resource or gold deck, or from one of the visible cards.
     * If there aren't any cards left in both decks, the method calls the gameOver method to notify the end of the game.
     *
     * @param playerName the player's username
     * @param choice the choice of card to draw
     */
    public void drawCard(String playerName, PlayerDrawChoice choice) throws EmptyDeckException, NoVisibleCardException, CardAlreadyPresentException, HandAlreadyFullException {
        Card drawncard = null;
        Player player = getPlayerFromUser(playerName);

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
        } catch (CantReplaceVisibleCardException e) {
            setInFinalPhase();
        }

        if(goldDeck.getNumRemaining() == 0 && resourceDeck.getNumRemaining() == 0){
            setInFinalPhase();
        }

        player.receiveDrawnCard((PlayableCard) drawncard);
    }

    /**
     * Manages the placement of a card during the game round.
     *
     * @param playerName the player's username
     * @param message the message containing the information on where to place the card
     */
    public void playCard(String playerName, PlaceCardMessage message) throws InvalidPositionException, NotPlacedException, AlreadyPlacedException, NotEnoughResourcesException, CardNotInHandException {

        Player currentplayer;
        currentplayer = getPlayerFromUser(playerName);

        int cardX = message.getX();
        int cardY = message.getY();
        boolean cardFace = message.isFacingUp();
        int cardID = message.getID();

        currentplayer.placeCard(cardID, cardX, cardY, cardFace, scoreTrack);

        if (scoreTrack.doesFinalPhaseStart()) {
            setInFinalPhase();
        }
    }

    /**
     * Extracts the player object given the username.
     *
     * @param username the player's name
     * @return currentPlayer the player object
     */
    private Player getPlayerFromUser(String username){
        Player currentPlayer = null;

        for (Player compPlayer : players) {
            if (compPlayer.getName().equals(username)) {
                currentPlayer = compPlayer;
                break;
            }
        }

        return currentPlayer;
    }

    /**
     * Calculates the final points at the end phase of the game by adding the points given by private and common objectives.
     *
     * @throws IllegalStateException if the method is invoked at the incorrect time
     */
    private void calculateFinalPoints() throws IllegalStateException {
            Card common1 = objectiveDeck.getFirstVisible();
            Card common2 = objectiveDeck.getSecondVisible();
        for(Player player: players){
            player.calculateSecretObjective();
            player.calculateCommonObjectives((ObjectiveCard) common1, (ObjectiveCard) common2);
            scoreTrack.updateScoreTrack(player.getName(),player.getPoints());
        }
    }

    /**
     * Return a copy of the scoreTrack to be sent to che clients
     *
     * @return a copy of the scoreTrack to be sent to the clients
     */
    public ImmutableScoreTrack getScoreTrack() {
        return scoreTrack.copyScoreTrack();
    }

    /**
     * Gets the available points where a card could be placed for a specific player.
     *
     * @param username the player's name
     * @return a list of available points
     * @throws NotPlacedException if an error occurs during the placement of a card
     * @throws PlayerCantPlaceAnymoreException if the player has blocked all possible future moves
     */
    public List<Point> getAvailablePoints(String username) throws NotPlacedException, PlayerCantPlaceAnymoreException {
        return getPlayerFromUser(username).getAvailablePositions();
    }

    /**
     * Gets the IDs of the cards in the player's hand.
     *
     * @param username the player's name
     * @return a list of the player's hand card IDs
     */
    public List<Integer> getPlayerHand(String username) {
        List<Integer> handID = new ArrayList<>();

        for (PlayableCard card : getPlayerFromUser(username).viewCurrentHand()) {
            handID.add(card.getID());
        }

        return  handID;
    }

    /**
     * Gets the visible symbols of a player.
     *
     * @param name the player's name
     * @return a map of visible symbols and their counts
     */
    public Map<TokenType,Integer> getPlayerVisibleSymbols(String name) {
        return getPlayerFromUser(name).viewVisibleSymbols();
    }

    /**
     * Sets the game in the final phase.
     */
    private void setInFinalPhase() {
        isInFinalPhase = true;
    }

    /**
     * Checks if the final phase has started.
     *
     * @return true if the game is in the final phase, false otherwise
     */
    public boolean isInFinalPhase() {
        return isInFinalPhase;
    }

    /**
     * Returns the winner(s) of the game
     *
     * @return a list of the winners of the game. It has size=0 before the final phase and size>1 only if
     * two or more players scored both the same number of points and the same number of objectives
     */
    public List<String> getWinners() {
        return new ArrayList<>(winners);
    }

    /**
     * Returns the first common visible objective
     *
     * @return the ID of the first common objective
     */
    public int getFirstCommonObjective() {
        return objectiveDeck.getFirstVisible().getID();
    }

    /**
     * Returns the second common visible objective
     *
     * @return the ID of the second common objective
     */
    public int getSecondCommonObjective() {
        return objectiveDeck.getSecondVisible().getID();
    }

    /**
     * Gets the ID of the starting card of a player.
     *
     * @param playerName the player's name
     * @return the ID of the starting card
     */
    public int getStartingCardId(String playerName) {
        Player player = getPlayerFromUser(playerName);
        return player.getStartingCard().getID();
    }

    /**
     * Gets the top card type of the gold deck.
     *
     * @return the CardType of the top card, or null if the deck is empty
     */
    public CardType getGoldTop() {
        return goldDeck.getTopCardColour();
    }

    /**
     * Gets the top card type of the resource deck.
     *
     * @return the CardType of the top card, or null if the deck is empty
     */
    public CardType getResourceTop() {
        return resourceDeck.getTopCardColour();
    }

    /**
     * Gets the IDs of the visible cards.
     *
     * @return a list of IDs of the visible cards
     */
    public List<Integer> getVisibleCards() {
        List <Integer> visibleCards = new ArrayList<>();
        Card c = resourceDeck.getFirstVisible();
        visibleCards.add(c == null ? null : c.getID());
        c = resourceDeck.getSecondVisible();
        visibleCards.add(c == null ? null : c.getID());
        c = goldDeck.getFirstVisible();
        visibleCards.add(c == null ? null : c.getID());
        c = goldDeck.getSecondVisible();
        visibleCards.add(c == null ? null : c.getID());
        return visibleCards;
    }

    /**
     * Sets the pawn color for a player.
     *
     * @param name the player's name
     * @param colour the player's pawn colour
     */
    public void setPawnColour(String name, String colour) {
        scoreTrack.setPawnColor(name, colour);
    }

    /**
     * Gets the list of winners excluding the disconnected players.
     *
     * @param connectedPlayers the names of players to be included as winners
     * @return the list of winners
     */
    public List<String> getWinnersAfterDisconnection(Collection<String> connectedPlayers) {
        updateWinners(connectedPlayers);
        return winners;
    }

    /**
     * Finalizes the game by updating the list of winners. This method is called
     * when there is a disconnection or when the final round counter reaches 0.
     *
     * @param connectedPlayers the list of names of connected players
     */
    public void gameOver(List<String> connectedPlayers) {
        calculateFinalPoints();
        updateWinners(connectedPlayers);
    }

    /**
     * Updates the list of winners with this policy:<br>
     * If a single player has the maximum number of points he is the winner.<br>
     * If there are multiple players tied for first place then the winner is decided by the number of scored objectives.<br>
     * If the number of scored objectives is also tied then the game will end in a tie.<br>
     *
     * @param toBeIncluded the names of players to be counted as winners
     */
    private void updateWinners(Collection<String> toBeIncluded) {
        int highestScore = -1;

        for (Player p : players) {
            if (toBeIncluded.contains(p.getName())) {
                int points = p.getPoints();
                scoreTrack.updateScoreTrack(p.getName(), points);

                if (points > highestScore) {
                    highestScore = points;
                    winners.clear();
                    winners.add(p.getName());
                } else if (points == highestScore) { // "if" entered only if there is at least one player in "winners"

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