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
 * Class used to represent the state of the Game, it's created after the Game has started
 */
public abstract class GameView {
    /**
     * Contains all the cards, accessed by the other view elements
     */
    protected static List<Card> cards;
    String playerName;
    ImmutableScoreTrack scoreTrack;
    DeckView goldDeck=null;
    DeckView resourceDeck=null;
    List<Integer> playerHand;
    PlayerFieldView ownerField;
    protected int startingCardID;
    final HashMap<String, PlayerFieldView> opponentFields;
    int[] secretObjectiveChoices;
    final int[] commonObjectives = new int[2];
    public List<String> getOpponentNames(){
        ArrayList<String> nameList = new ArrayList<>(opponentFields.keySet());
        nameList.remove(playerName);
        return nameList;
    }
    /**
     * Attribute used to determine where to show the black pawn in the gui.
     */
    String firstPlayerName;
    public GameView() {
        secretObjectiveChoices = new int[2];
        secretObjectiveChoices[0]=-1;
        secretObjectiveChoices[1]=-1;

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
     *
     * @return the name of the player that will have the blackPawn on their starting Card
     */
    public String getFirstPlayerName(){
        return firstPlayerName;
    }
    /**
     * After the constructor the methods to update the decks must be called by the controller with the necessary information
     */
    public void gameStarting(List<String> otherPlayerNames, String playerName,
                                      int startingCard, int firstCommonObjective, int secondCommonObjective,String firstPlayerName) throws IOException{
        this.playerName = playerName;
        this.firstPlayerName=firstPlayerName;
        startingCardID=startingCard;
        HashMap<String, Integer> startingScoreTrack = new HashMap<>();
        if(ConstantValues.usingCLI) {
            this.goldDeck = new DeckViewCli("Gold");
            this.resourceDeck = new DeckViewCli("Resource");
            for (String s : otherPlayerNames) {
                if (!opponentFields.containsKey(s)) {
                    opponentFields.put(s, new PlayerFieldViewCli(s));
                }
                startingScoreTrack.put(s, 0);
            }
            ownerField=new PlayerFieldViewCli(playerName);
        }else{
            this.goldDeck = new DeckViewGui("Gold");
            this.resourceDeck = new DeckViewGui("Resource");
            for (String s : otherPlayerNames) {
                if (!opponentFields.containsKey(s)) {
                    opponentFields.put(s, new PlayerFieldViewGui(s));
                }
                startingScoreTrack.put(s, 0);
            }
            ownerField=new PlayerFieldViewGui(playerName);
        }
        commonObjectives[0]=firstCommonObjective;
        commonObjectives[1]=secondCommonObjective;
        scoreTrack=new ImmutableScoreTrack(startingScoreTrack,new HashMap<>());

    }

    /**
     * This method updates the information of the scoreTrack
     *
     * @param updated is the updated scoreTrack tha has been received
     */
    public void updateScoreTrack(ImmutableScoreTrack updated) {
        if (updated != null)
            scoreTrack = updated;
    }

    /**
     * @param topGold      colour of the top of the deck
     * @param topResource  colour of the top of the deck
     * @param visibleCards the four visible cards that can be drawn, in this order resource first visible -> resource second visible -> gold first visible-> gold second visible
     */
    public void updateDecks(CardType topGold, CardType topResource, List<Integer> visibleCards) {
        if(resourceDeck==null||goldDeck==null){
            return;
        }
        resourceDeck.update(topResource, visibleCards.get(0), visibleCards.get(1));
        goldDeck.update(topGold, visibleCards.get(2), visibleCards.get(3));
    }

    /**
     * Adds a card to the field of the player that has just made a move
     *
     * @param name    name of the opponent who made the move
     * @param placeID ID of the placed card
     * @param placedX xPosition
     * @param placedY yPosition
     */
    public void updateOtherPlayerField(String name, int placeID, int placedX, int placedY, boolean isFacingUp, Map<TokenType, Integer> visibleSymbols) {
        if (!opponentFields.containsKey(name)) {
            if(ConstantValues.usingCLI) {
                opponentFields.put(name, new PlayerFieldViewCli(name));
            }else {
                opponentFields.put(name, new PlayerFieldViewGui(name));
            }
        }
        opponentFields.get(name).updateField(placeID, placedX, placedY, isFacingUp, visibleSymbols);
    }

    /**
     * Adds the card the owner of the field just placed
     *
     * @param placedID ID of the placed card
     * @param placedX  xPosition
     * @param placedY  yPosition
     */
    public void updateOwnerField(int placedID, int placedX, int placedY, boolean isFacingUp, Map<TokenType, Integer> visibleSymbols) {
        ownerField.updateField(placedID, placedX, placedY, isFacingUp, visibleSymbols);
    }


    /**
     * Prints the requested player's field for the CLI
     *
     * @param name name of the opponent whose field will be shown
     */
    public abstract void opponentMadeAMove(String name);

    /**
     * This method should be called after a player has placed a card, to update for the removal of the card placed,
     * and  after a player has drawn to update for the addition of the new card
     *
     * @param newPlayerHand the hand after the player has made a move
     */
    public void updatePlayerHand(List<Integer> newPlayerHand) {
        playerHand = newPlayerHand;
    }

    /**
     * Removes the card with id lastPlayed from the hand of the player
     *
     * @param lastPlayed the id of the card that has been placed
     */
    public void updatePlayerHand(int lastPlayed) {
        if (playerHand.contains(lastPlayed)) {
            playerHand.remove((Integer) lastPlayed);
        }
    }

    /**
     * Returns the hand of the player. Used to check if the card they want to place is in their possession
     */
    public List<Integer> getPlayerHand() {
        return new ArrayList<>(playerHand);
    }

    public void updateAvailablePositions(List<Point> availablePositions) {
        ownerField.updateAvailablePositions(availablePositions);
    }

    /**
     * Method that prints a message as either CLI or GUI according to how the program was started
     */
    public abstract void display(String s);

    /**
     * This method memorizes the two possible choices and then shows them
     *
     * @param firstChoice  first objective choice
     * @param secondChoice second objective choice
     */
    public abstract void secretObjectiveChoice(int firstChoice, int secondChoice);

    /**
     * Adds information about the secret objective card chosen by the player
     *
     * @param id is the id of the objective card to be chosen
     * @return true if the choice happened successfully
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
     * Prints the secret objective or the secret objective choices depending on whether the objective was already chosen
     */
    public abstract void showSecretObjectives();

    /**
     * Displays the final leaderboard
     *
     * @param finalPlayerScore is the score track at the end of the game
     */
    public abstract void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection);



    /**
     * Displays the starting card for the player to see
     */
    public abstract void printStartingInfo();

    /**
     * Displays the necessary interface to choose a name
     */
    public abstract void nameChoice();

    public abstract void nameNotAvailable(String clientName);

    public abstract void waitingForStart();

    public abstract void chooseNumPlayers();

    public abstract void colourChoice(boolean showNotAvailable);

    public abstract void placingACard();

    public abstract void drawingACard(boolean initialPhase);

    public abstract void receivedDrawnCard();

    public abstract void sharedFieldUpdate();

    public abstract void goToOwnerField();

    public abstract void goToOpponentField(String opponentName);

    public abstract void receivedChat(String s);

    public abstract void displayChat(List<String> chatLogs);

    public abstract void disconnection();

    public abstract void connectionRefused();
}
