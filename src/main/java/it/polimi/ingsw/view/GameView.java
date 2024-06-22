package it.polimi.ingsw.view;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.view.Deck.DeckView;
import it.polimi.ingsw.view.Deck.DeckViewCli;
import it.polimi.ingsw.view.Deck.DeckViewGui;
import it.polimi.ingsw.view.Field.PlayerFieldView;
import it.polimi.ingsw.view.Field.PlayerFieldViewCli;
import it.polimi.ingsw.view.Field.PlayerFieldViewGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to represent the state of the Game, it's created after the Game has started.
 */
public abstract class GameView {

    /**
     * Contains all the cards, accessed by the other view elements.
     */
    protected static List<Card> cards;

    String playerName;

    ImmutableScoreTrack scoreTrack;

    DeckView goldDeck = null;

    DeckView resourceDeck = null;

    List<Integer> playerHand;

    PlayerFieldView ownerField;

    protected int startingCardID;

    final HashMap<String, PlayerFieldView> opponentFields;

    int[] secretObjectiveChoices;

    final int[] commonObjectives = new int[2];

    /**
     * Gets the list of opponent names.
     *
     * @return the list of opponent names
     */
    public List<String> getOpponentNames(){
        ArrayList<String> nameList = new ArrayList<>(opponentFields.keySet());
        nameList.remove(playerName);
        return nameList;
    }

    /**
     * Attribute used to determine where to show the black pawn in the GUI.
     */
    String firstPlayerName;

    /**
     * Constructor for GameView. Initializes the card list and opponent fields.
     */
    public GameView() {
        secretObjectiveChoices = new int[2];
        secretObjectiveChoices[0] = -1;
        secretObjectiveChoices[1] = -1;

        opponentFields= new HashMap<>();

        try {
            cards = Creation.getInstance().getResourceCards();
            cards.addAll(Creation.getInstance().getGoldCards());
            cards.addAll(Creation.getInstance().getStartingCards());
            cards.addAll(Creation.getInstance().getObjectiveCards());
        } catch (IOException e) {
            System.err.println("Error while getting the cards for the view");
            throw new RuntimeException();
        }
    }

    /**
     * Gets the name of the player that will have the black pawn on their starting card.
     *
     * @return the name of the first player
     */
    public String getFirstPlayerName(){
        return firstPlayerName;
    }

    /**
     * Initializes the game view with the necessary information.
     *
     * @param otherPlayerNames     list of other player names
     * @param playerName           name of the current player
     * @param startingCard         ID of the starting card
     * @param firstCommonObjective ID of the first common objective
     * @param secondCommonObjective ID of the second common objective
     * @param firstPlayerName      name of the first player
     * @throws IOException if an I/O error occurs
     */
    public void gameStarting(List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective, String firstPlayerName) throws IOException {
        this.playerName = playerName;
        this.firstPlayerName = firstPlayerName;
        startingCardID = startingCard;
        HashMap<String, Integer> startingScoreTrack = new HashMap<>();

        if (ConstantValues.usingCLI) {
            this.goldDeck = new DeckViewCli("Gold");
            this.resourceDeck = new DeckViewCli("Resource");

            for (String s : otherPlayerNames) {
                if (!opponentFields.containsKey(s)) {
                    opponentFields.put(s, new PlayerFieldViewCli(s));
                }

                startingScoreTrack.put(s, 0);
            }

            ownerField = new PlayerFieldViewCli(playerName);
        } else {
            this.goldDeck = new DeckViewGui("Gold");
            this.resourceDeck = new DeckViewGui("Resource");

            for (String s : otherPlayerNames) {
                if (!opponentFields.containsKey(s)) {
                    opponentFields.put(s, new PlayerFieldViewGui(s));
                }

                startingScoreTrack.put(s, 0);
            }

            ownerField = new PlayerFieldViewGui(playerName);
        }

        commonObjectives[0]=firstCommonObjective;
        commonObjectives[1]=secondCommonObjective;
        scoreTrack=new ImmutableScoreTrack(startingScoreTrack,new HashMap<>());

    }

    /**
     * Updates the information of the score track.
     *
     * @param updated the updated score track
     */
    public void updateScoreTrack(ImmutableScoreTrack updated) {
        if (updated != null)
            scoreTrack = updated;
    }

    /**
     * Updates the information of the decks.
     *
     * @param topGold     colour of the top of the gold deck
     * @param topResource colour of the top of the resource deck
     * @param visibleCards the four visible cards that can be drawn
     */
    public void updateDecks(CardType topGold, CardType topResource, List<Integer> visibleCards) {
        if (resourceDeck == null || goldDeck == null){
            return;
        }

        resourceDeck.update(topResource, visibleCards.get(0), visibleCards.get(1));
        goldDeck.update(topGold, visibleCards.get(2), visibleCards.get(3));
    }

    /**
     * Adds a card to the field of the player that has just made a move.
     *
     * @param name         name of the opponent who made the move
     * @param placeID      ID of the placed card
     * @param placedX      x position
     * @param placedY      y position
     * @param isFacingUp   whether the card is facing up
     * @param visibleSymbols map of visible symbols
     */
    public void updateOtherPlayerField(String name, int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType, Integer> visibleSymbols) {
        if (!opponentFields.containsKey(name)) {
            if (ConstantValues.usingCLI) {
                opponentFields.put(name, new PlayerFieldViewCli(name));
            } else {
                opponentFields.put(name, new PlayerFieldViewGui(name));
            }
        }

        opponentFields.get(name).updateField(placeID, placedX, placedY, isFacingUp, visibleSymbols);
    }

    /**
     * Adds the card the owner of the field just placed.
     *
     * @param placedID      ID of the placed card
     * @param placedX       x position
     * @param placedY       y position
     * @param isFacingUp    whether the card is facing up
     * @param visibleSymbols map of visible symbols
     */
    public void updateOwnerField(int placedID, int placedX, int placedY, boolean isFacingUp, Map<TokenType, Integer> visibleSymbols) {
        ownerField.updateField(placedID, placedX, placedY, isFacingUp, visibleSymbols);
    }

    /**
     * Prints the requested player's field for the CLI.
     *
     * @param name name of the opponent whose field will be shown
     */
    public abstract void opponentMadeAMove(String name);

    /**
     * Updates the player hand after a move.
     *
     * @param newPlayerHand the updated player hand
     */
    public void updatePlayerHand(List<Integer> newPlayerHand) {
        playerHand = newPlayerHand;
    }

    /**
     * Updates the player hand by removing the placed card.
     *
     * @param lastPlayed the ID of the card that has been placed
     */
    public void updatePlayerHand(int lastPlayed) {
        if (playerHand.contains(lastPlayed)) {
            playerHand.remove((Integer) lastPlayed);
        }
    }

    /**
     * Returns the player's hand.
     *
     * @return the player's hand
     */
    public List<Integer> getPlayerHand() {
        return new ArrayList<>(playerHand);
    }


    /**
     * Displays a message.
     *
     * @param s the message to display
     */
    public abstract void display(String s);

    /**
     * Memorizes and displays the two secret objective choices.
     *
     * @param firstChoice  the first objective choice
     * @param secondChoice the second objective choice
     */
    public abstract void secretObjectiveChoice(int firstChoice, int secondChoice);

    /**
     * Sets the secret objective chosen by the player.
     *
     * @param id the ID of the chosen objective card
     * @return true if the choice was successful
     */
    public boolean setSecretObjective(int id) {
        if (secretObjectiveChoices.length == 1) {
            this.display("You have already chosen an objective!");
            return false;
        } else if (id != secretObjectiveChoices[0] && id != secretObjectiveChoices[1]) {
            this.display("You don't have this card as a choice for an objective!");
            return false;
        } else {
            secretObjectiveChoices = new int[1];
            secretObjectiveChoices[0] = id;
            return true;
        }
    }

    /**
     * Prints the secret objective or the secret objective choices.
     */
    public abstract void showSecretObjectives();

    /**
     * Displays the final leaderboard.
     *
     * @param finalPlayerScore the final score track
     * @param winners          the list of winners
     * @param disconnection    whether there was a disconnection
     */
    public abstract void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection);

    /**
     * Displays the starting card for the player to see.
     */
    public abstract void printStartingInfo();

    /**
     * Displays the necessary interface to choose a name.
     */
    public abstract void nameChoice();

    /**
     * Informs the player that the chosen name is not available.
     *
     * @param clientName the name that is not available
     */
    public abstract void nameNotAvailable(String clientName);

    /**
     * Informs the player to wait for the game to start.
     */
    public abstract void waitingForStart();

    /**
     * Displays the interface to choose the number of players.
     */
    public abstract void chooseNumPlayers();

    /**
     * Displays the interface to choose a colour.
     *
     * @param showNotAvailable whether to show unavailable colours
     */
    public abstract void colourChoice(boolean showNotAvailable);

    /**
     * Informs the player to place a card.
     */
    public abstract void placingACard();

    /**
     * Informs the player to draw a card.
     *
     * @param initialPhase whether it is the initial phase
     */
    public abstract void drawingACard(boolean initialPhase);

    /**
     * Informs the player that a card has been drawn.
     */
    public abstract void receivedDrawnCard();

    /**
     * Updates the shared field view.
     */
    public abstract void sharedFieldUpdate();

    /**
     * Navigates to the owner's field view.
     */
    public abstract void goToOwnerField();

    /**
     * Navigates to an opponent's field view.
     *
     * @param opponentName the name of the opponent
     */
    public abstract void goToOpponentField(String opponentName);

    /**
     * Receives a chat message.
     *
     * @param s the chat message
     */
    public abstract void receivedChat(String s);

    /**
     * Displays the chat logs.
     *
     * @param chatLogs the list of chat logs
     */
    public abstract void displayChat(List<String> chatLogs);

    /**
     * Informs the player of a disconnection.
     */
    public abstract void disconnection();

    /**
     * Informs the player that the connection was refused.
     */
    public abstract void connectionRefused();
}
