package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalStartingCardException;
import it.polimi.ingsw.model.enums.GameState;

import java.util.ArrayList;
import java.util.List;
public class Game {
    /**
     *  Data
     */
    private GameState currentState=GameState.SETUP;
    public int numPlayers;
    public int currentPlayerIndex;
    private Deck objectiveDeck;
    private Deck goldDeck;
    private Deck resourceDeck;
    private List<StartingCard> startingCards;
    public List<Player> players;
    private ScoreTrack scoreTrack;

    public Game(int num){
        this.numPlayers = num;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    };

    public void startGame(Deck goldDeck, Deck objectiveDeck, Deck resourceDeck) throws Exception, IllegalStartingCardException {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.objectiveDeck = objectiveDeck;
        this.startingCards = new ArrayList<>();
        currentState = GameState.SETUP;
        setupScoreTrack();
        setupDecks();
        setupPlayers();
    }
    /**
     *  Set-up
     */
    private void setupScoreTrack(){
        switch (numPlayers) {
            case 2:
                scoreTrack = new ScoreTrack("Player 1", "Player 2");
                break;
            case 3:
                scoreTrack = new ScoreTrack("Player 1", "Player 2", "Player 3");
                break;
            case 4:
                scoreTrack = new ScoreTrack("Player 1", "Player 2", "Player 3", "Player 4");
                break;
            default:
                System.out.println("Invalid number of players!");
        }
    }

    private void setupDecks() throws Exception {
        resourceDeck.shuffle();
        resourceDeck.setFirstVisible();
        resourceDeck.setSecondVisible();
        goldDeck.shuffle();
        goldDeck.setFirstVisible();
        goldDeck.setSecondVisible();
        objectiveDeck.shuffle();
    }


    private void setupPlayers() throws IllegalStartingCardException {

        /*manages the init phase of the game, players are added, startingCards are dealt
            and the player has to decide between the secretObjective dealt*/

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player("Player " + (i + 1));
            players.add(player);
            dealStartingCards(player);
            drawPlayableCards();
            dealSecretObjective(player);
        }
        currentState = GameState.GAME_ROUND;
    }

    private void drawPlayableCards() {
        //player draws 2 resource cards and 1 gold one
    }


    private void dealStartingCards(Player player) {
    }

    private void dealSecretObjective(Player player) {
    }
 /*
                        VITALIANO

 public void start_game(*//* Deck: GoldenDeck1, Deck: ResourceDeck1, Deck: ObjectiveDeck1*//*){
        *//* ----------------------------------------------------------( 1 )-----------------------------------*//*
        switch(numPlayers) {
            *//*------------------------- to fix the setting of the keys --------------------*//*
            case 2:
                *//* TODO *//*
                ScoreTrack Table2 = new ScoreTrack("1","2");
                break;
            case 3:
                *//* TODO *//*
                ScoreTrack Table3 = new ScoreTrack("1","2","3");
                break;
            case 4:
                *//* TODO *//*
                ScoreTrack Table4 = new ScoreTrack("1","2","3","4");
                break;
            default:
                System.out.println("Il numero non è valido");
        }*/
        /**
         *   phase of game
         */
        /*
         *  Random rand = new Random();
         *  int randomNumber = rand.nextInt(2) + 2;
         *  int turn;
         *  turn = randomNumber;
        /*
         *  SETUP GAME
         *
         * -----------------------------------------------------------( 2 )--------------------------------------
         *  GoldenDeck1.shuffle();               (?)
         *  GoldenDeck1.getFirstVisible();
         *  GoldenDeck1.getSecondVisible();
         *  ResourceDeck1.shuffle();             (?)
         *  ResourceDeck1.getFirstVisible();
         *  ResourceDeck1.getSecondVisible();
         * -----------------------------------------------------------( 3 )---------------------------------------
         *
         * switch(numPlayers) {
         *   case 2:
         *       --- DRAW + PLACING
         *  Random rand = new Random();
         *  int randomNumber = rand.nextInt(2) + 2;
         *  int turn;
         *  turn = randomNumber;
         *
         *
         *   case 3:
         *       --- DRAW + PLACING
         *       break;
         *   case 4:
         *       --- DRAW + PLACING
         *       break;
         *   default:
         *       System.out.println("Il numero non è valido");
         *  }
         *
         *
         *
         *
         *  ObjectiveCard1.shuffle();
         *  ObjectiveCard1.getFirstVisible();
         *  ObjectiveCard1.getSecondVisible();
         *  if(numPlayers == 2)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 2 PLAYERS
         * }
         *  if(numPlayers == 3)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 3 PLAYERS
         * }
         *  if(numPlayers == 4)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 4 PLAYERS
         * }
         *
         *
         *  --- put the pawn on the table ---
         */

        /**
         * This class aim to manages the game rounds and the overall flow of the game
         *
         */


        public void gameRound() {
            if (currentState != GameState.GAME_ROUND) {
                return;
            }
            int phaseCounter=0;
            Player currentPlayer = players.get(currentPlayerIndex);

            // PlayerTurn TODO
            //play card
            //draw card
            //i can use a counter to increase in each phase, if =2
            //round is finished and go to the next round, setting counter back to 0


            if (isRoundOver(phaseCounter)) {
                //increase the currentPlayer index, %numPlayers guarantees that
                // the index never overtake the numbers of players
                currentPlayerIndex = (currentPlayerIndex + 1) % numPlayers;
                if (isGameOver()) {
                    currentState = GameState.FINAL_PHASE;
                    calculateFinalPoints();
                    declare_winner();
                    currentState = GameState.FINISHED;
                } else {
                    currentState = GameState.GAME_ROUND;
                }
            }
        }

    /**
     * getter implemented to always know the state of the game
     * @return currentState
     */
    public GameState getGameState() {
        return currentState;
    }

    /**
     * this method checks if the player round is ended or not
     * @return true if the round is ended, false otherwise
     */
    private boolean isRoundOver(int phaseCounter) {
        //counter logic?
        if(phaseCounter==2)
            return true;
        return false;
    }

    /**
     * this method checks if the game is still on or the final phase is reached
     * @return true if the final Phase is reached, false otherwise
     */

    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getPoints() >= 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method aims to calculate the objective points in the final phase
     */
    private void calculateFinalPoints() {
            if(currentState != GameState.FINAL_PHASE)
                return;
        //TODO
        // After reaching 20 points, the finalphase starts and the
        //points given by the objective are calculated ib this method
    }


    /**
     * Declare Winner based on the dinamic list of players (that can have 2 or 3 or 4 elements).
     * The only information that is saved of the winner is the name.
     * In case of "multiple winners", they can all be found in the List of names "idWinners".
     */
    private void declare_winner(){
        List<String> idWinners = new List<>(String);
        idWinners.addFirst(players.getFirst().getName());

        //NB: "i-1" instead of "i+1" is important so the "OutOfIndex" error should never happen in any circumstance
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getPoints() == players.get(i - 1).getPoints()) { idWinners.add(players.get(i).getName()); }
            else if (players.get(i).getPoints() > players.get(i - 1).getPoints()) {
                idWinners.clear();
                idWinners.add(players.get(i).getName());
            }
        }

        /*
         *  VITALIANO MANDARA:
         *
         * -- 2 --
         * if(num_Players==2){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints();
         * winner = player2.id;
         *   }
         * }
         *
         * -- 3 --
         *
         * if(num_Players==3){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints()
         * winner = player2.id
         *   }
         * else if (player3.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * }
         *
         * -- 4 --
         *
         * if(num_Players==4){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints()
         * winner = player2.id
         *   }
         * if (player3.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * if (player4.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * }
         *
         *
         */

    }

    public String get_next_winner_name(){
        String next_winner = players.getFirst().getName();
        players.removeFirst();
        return next_winner;
    }
}