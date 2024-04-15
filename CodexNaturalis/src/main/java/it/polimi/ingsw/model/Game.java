package it.polimi.ingsw.model;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Game {
    private GameState currentState=GameState.SETUP;
    public int numPlayers;
    public int currentPlayerIndex;
    private Deck objectiveDeck;
    private Deck goldDeck;
    private Deck resourceDeck;
    public List<Player> players;
    private ScoreTrack scoreTrack;

    public Game(int num){
        this.numPlayers = num;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public void startGame(String username1, String username2, String username3, String username4) throws Exception, IllegalStartingCardException {
        currentState = GameState.SETUP;
        setupScoreTrack(username1, username2, username3, username4);
        setupDecks();
        setupPlayers(username1, username2, username3, username4);
    }
    /**
     *  Set-up
     */
    private void setupScoreTrack(String username1, String username2, String username3, String username4){
        switch (numPlayers) {
            case 2:
                scoreTrack = new ScoreTrack(username1, username2);
                break;
            case 3:
                scoreTrack = new ScoreTrack(username1, username2, username3);
                break;
            case 4:
                scoreTrack = new ScoreTrack(username1, username2, username3, username4);
                break;
            default:
                System.out.println("Invalid number of players!");
        }
    }

    /**
     *
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
     *
     * @throws IllegalStartingCardException
     * @throws Exception
     */

    private void setupPlayers(String username1, String username2, String username3, String username4) throws Exception, IllegalStartingCardException {

        /*manages the init phase of the game, players are added, startingCards are dealt
           and the player has to decide between the secretObjective dealt*/
        String[] currentUsername = {username1, username2, username3, username4};

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(currentUsername[i]);
            players.add(player);
            drawInitialCards(player);
            dealSecretObjective(player);
        }
    }

    /**
     *
     * @param player
     * @return drawnCard
     * @throws Exception
     */

    private Card[] drawInitialCards(Player player) throws Exception {
        Card[] drawnCard = null;
        int i=0;
        for(i = 0; i<2; i++){
            assert drawnCard != null;
            drawnCard[i]=resourceDeck.draw(0);
        }
        drawnCard[i+1]=goldDeck.draw(0);
        return drawnCard;
        //player draws 2 resource cards and 1 gold one
    }

    /**
     *
     * @param player
     * @return objectiveOptions
     * @throws Exception
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
     *
     * @param player
     * @param secretObjective
     */

    private void placeSecretObjective(Player player, ObjectiveCard secretObjective) throws ObjectiveAlreadySetException {
        player.setSecretObjective(secretObjective);
    }
    public void drawCard(Player player, int drawChoice, String typeOfCard) throws Exception {
        Card drawncard=null;
        switch(typeOfCard){
            case "resource":
                drawncard = resourceDeck.draw(drawChoice);
                player.receiveDrawnCard((PlayableCard) drawncard);
                try{
                    resourceDeck.setVisibleAfterDraw(goldDeck);
                }catch(Exception e){
                    /*
                    TODO: start final phase of the game
                     */
                }
                break;
            case "gold":
                drawncard=goldDeck.draw(drawChoice);
                player.receiveDrawnCard((PlayableCard) drawncard);
                try{
                    goldDeck.setVisibleAfterDraw(resourceDeck);
                }catch(Exception e){
                    /*
                    TODO: start final phase of the game
                     */
                }
                break;
            default:
                System.out.println("Invalid type of playable card");
        }
    }


    /**
     *
     * @param playerName
     * @param message
     * @throws Exception
     */
    public void playCard(String playerName, PlaceCardMessage message) throws Exception {

        Player currentplayer = null;
        currentplayer = getPlayerFromUser(playerName);

        int cardX = message.getX();
        int cardY = message.getY();
        boolean cardFace = message.isFacingUp();
        int cardID = message.getID();

        currentplayer.placeCard(cardID,cardX,cardY,cardFace);

    }

    /**
     *
     * @param username
     * @return
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
     *  and update the scoretrack with them
     * @throws IllegalStateException
     */
    private void calculateFinalPoints() throws IllegalStateException {
            if(currentState != GameState.FINAL_PHASE)
                throw new IllegalStateException("Not in the final phase yet");
            Card common1 = objectiveDeck.getFirstVisible();
            Card common2 = objectiveDeck.getSecondVisible();
        for(Player player: players){
            player.calculateSecretObjective();
            player.calculateCommonObjectives((ObjectiveCard) common1, (ObjectiveCard) common2);
            int totalPoints = player.getPoints();
            String playerName = player.getName();
            scoreTrack.updateScoreTrack(playerName, totalPoints);
        }
    }



    /**
     * Declare Winner based on the dinamic list of players (that can have 2 or 3 or 4 elements).
     * The only information that is saved of the winner is the name.
     * In case of "multiple winners", they can all be found in the List of names "idWinners".
     */
    private void declare_winner(){
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
}