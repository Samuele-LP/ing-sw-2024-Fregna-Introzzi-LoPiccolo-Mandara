package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.view.Deck.DeckViewCli;
import it.polimi.ingsw.view.Field.PlayerFieldViewCli;

import java.util.ArrayList;
import java.util.List;

public class GameViewCli extends GameView{

    public GameViewCli(){
        super();
    }

    /**
     * Prints the requested player's field for the CLI
     *
     * @param name name of the opponent whose field will be shown
     */
    @Override
    public void opponentMadeAMove(String name) {
        printSpacer(100);
        System.out.println("\n" + name + " has made a move!");
        System.out.println("\n\n\n");
        printFieldWithoutHand(name);
    }

    /**
     * Method that prints a message as either CLI or GUI according to how the program was started
     */
    @Override
    public void display(String s) {
        System.out.println(s);
    }

    /**
     * This method memorizes the two possible choices and then shows them
     *
     * @param firstChoice  first objective choice
     * @param secondChoice second objective choice
     */
    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice){
        printSpacer(100);
        System.out.println("Here are your secret objective choices");
        secretObjectiveChoices[0] = firstChoice;
        secretObjectiveChoices[1] = secondChoice;
    }

    /**
     * Prints the secret objective or the secret objective choices depending on whether the objective was already chosen
     */
    @Override
    public void showSecretObjectives() {
        if (secretObjectiveChoices[0] < 1) {
            System.out.println("\n\nThe secret objectives haven't been drawn yet!!");
            return;
        }
        printSpacer(1);
        String[] out = new String[5];
        ObjectiveCard obj = (ObjectiveCard) cards.get(secretObjectiveChoices[0] - 1);
        String[] objective = obj.printCardInfo().split("X");
        out[0] = objective[0];
        out[1] = objective[1];
        out[2] = objective[2];
        out[3] = objective[3];
        out[4] = objective[4];
        if (secretObjectiveChoices.length > 1 && secretObjectiveChoices[1] > 0) {
            System.out.println("Secret objective choices:\n");
            obj = (ObjectiveCard) cards.get(secretObjectiveChoices[1] - 1);
            objective = obj.printCardInfo().split("X");
            System.out.println(out[0] + "        " + objective[0]);
            System.out.println(out[1] + "     " + objective[1]);
            System.out.println(out[2] + "        " + objective[2]);
            System.out.println(out[3] + "        " + objective[3]);
            System.out.println(out[4] + "        " + objective[4]);
            System.out.println("\n(Type 'co' or 'choose_objective' followed by the id of the chosen objective)\n ");
        } else {
            System.out.println("Secret objective:\n");
            System.out.println(out[0]);
            System.out.println(out[1]);
            System.out.println(out[2]);
            System.out.println(out[3]);
            System.out.println(out[4]);
        }
    }

    /**
     * Displays the final leaderboard
     */
    @Override
    public void displayWinners(ImmutableScoreTrack finalPlayerScore, List<String> winners,boolean disconnection) {
        printSpacer(100);

        if (disconnection) {
            System.out.println("\nThe game has ended because of a disconnection here is the final leaderboard:\n");
        } else {
            System.out.println("\nThe game has ended here is the final leaderboard:\n");
        }

        for(String s: finalPlayerScore.printTable()){
            System.out.println(s);
        }

        if (winners.size() == 1) {
            this.display("\nCongratulations to " + winners.getFirst() + " for winning!!\n\n");
        } else {
            this.display("\nThere was a draw!! The winners are:   ");

            for (String s : winners) {
                this.display(s + "   ");
            }
        }
    }

    /**
     * Displays the starting card for the player to see
     */
    @Override
    public void printStartingInfo()  {
        printSpacer(100);
        printCommonField();
        printSpacer(1);
        printHand();
        printSpacer(1);
        StartingCard startingCard= (StartingCard) cards.get(startingCardID-1);
        System.out.println("Here is your starting card:\nID: "+startingCardID);
        System.out.println("Front:          Back:");
        System.out.println("|"+startingCard.asciiArtFront()[0]+"|     |"+startingCard.asciiArtBack()[0]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[1]+"|     |"+startingCard.asciiArtBack()[1]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[2]+"|     |"+startingCard.asciiArtBack()[2]+"|\n");
        System.out.println("""

                Place your starting card.           (Type 's' or 'starting' and 'up' or 'down')
                """);
    }

    /**
     * Displays the necessary interface to choose a name
     */
    @Override
    public void nameChoice() {
        System.out.println("""

                Connected successfully to a game.

                Now choose your name (Type 'n' or 'name' followed by your name)
                """);
    }

    @Override
    public void nameNotAvailable(String clientName) {
        System.out.println("\n" + clientName + " is already taken, choose another name.\n");
    }
    @Override
    public void waitingForStart() {
        System.out.println("\nSuccessfully registered to the game \n");
    }

    @Override
    public void chooseNumPlayers(){
        System.out.println("""

                You were the first to connect!
                Choose the number of players that will play in this game
                (Type 'ps' or 'players' and the number of players that will play)
                """);
    }

    @Override
    public void colourChoice(boolean showNotAvailable) {
        if(showNotAvailable) {
            System.out.println("\nChange your choice! That colour was already chosen!\n");
            return;
        }
        System.out.println("\nNow choose the colour you want (red,green,yellow or blue). Type 'col' followed by the colour you want\n");
    }
    @Override
    public void placingACard() {
        printSpacer(100);
        printOwnerField();
        System.out.println("""

                    It's your turn, now place a card!
                    (Type 'p' 'id' 'up' or 'down' 'x' 'y')
                    """);
    }

    @Override
    public void drawingACard(boolean initialPhase) {
        printSpacer(100);
        printOwnerField();
        printSpacer(2);
        printCommonField();
        if (!initialPhase) {
            System.out.println("""

                        Now draw a card!Type 'd' or 'draw' followed by
                        'g' for the top card of the gold deck
                        'g1' for the first visible gold card
                        'g2' for the second visible gold card
                        'r' for the top card f the gold deck
                        'r1' for the first visible resource card
                        'r2' for the second visible resource card
                                                
                        """);
        }
    }

    @Override
    public void receivedDrawnCard() {
        printSpacer(100);
        printCommonField();
        printHand();
    }

    @Override
    public void sharedFieldUpdate() {
        if (this.commonObjectives[0] <= 0) {
            return;
        }
        printSpacer(100);
        printCommonField();
        printHand();
    }

    @Override
    public void goToOwnerField() {
        printSpacer(100);
        printOwnerField();
    }

    @Override
    public void goToOpponentField(String opponentName) {
        printSpacer(100);
        printCommonField();
        printFieldWithoutHand(opponentName);
    }

    @Override
    public void receivedChat(String s) {
        display(s);
    }

    @Override
    public void displayChat(List<String> chatLogs) {
        this.display("Here's the chat history: ");
        if (chatLogs.isEmpty()) {
            display("The chat is empty!!!");
        }
        for (String s : chatLogs) {
            display("\n" + s + "\n");
        }
        display("\n\n");
    }

    @Override
    public void disconnection() {
        display("A disconnection has occurred!!\nThe game has ended without a winner!!\n\n");
    }

    @Override
    public void connectionRefused() {
        display("\nYour connection was refused by the server!!\n");
    }

    public void showLeaderBoard() {
        printSpacer(3);
        for(String s: scoreTrack.printTable()){
            System.out.println(s);
        }
    }

    /**
     * Method that shows information about the common field
     */
    public void printCommonField() {
        printSpacer(100);

        List<String> leaderboard = scoreTrack.printTable();

        String[] goldTemp = ((DeckViewCli)goldDeck).printDeck();

        String[] resourceTemp = ((DeckViewCli)resourceDeck).printDeck();

        String[] commonObjs = showCommonObjectives();

        System.out.println(commonObjs[0] +
                leaderboard.get(0) + "     " +
                goldTemp[0] + " ".repeat(
                30) + resourceTemp[0]);
        System.out.println(commonObjs[1] +
                leaderboard.get(1) + "     " +
                goldTemp[1] + " ".repeat(
                goldTemp[1].equals("The deck has no more cards in it.") ? 7 : 11) + resourceTemp[1]);
        System.out.println(commonObjs[2] +
                leaderboard.get(2) + "     " +
                goldTemp[2] + " ".repeat(
                goldTemp[2].equals("There is no first visible card") ? 10 : 17) + resourceTemp[2]);
        System.out.println(commonObjs[3] +
                leaderboard.get(3) + "     " +
                goldTemp[3] + " ".repeat(
                29) + resourceTemp[3]);
        System.out.println(commonObjs[4] +
                leaderboard.get(4) + "     " +
                goldTemp[4] + " ".repeat(
                29) + resourceTemp[4]);
        System.out.println(commonObjs[5] +
                leaderboard.get(5) + "     " +
                goldTemp[5] + " ".repeat(
                29) + resourceTemp[5]);
        System.out.println(commonObjs[6] +
                leaderboard.get(6) + "     " +
                goldTemp[6] + " ".repeat(
                goldTemp[6].equals("There is no second visible card") ? 9 : 5) + resourceTemp[6]);
        System.out.println(commonObjs[7] +
                leaderboard.get(7) + "     " +
                goldTemp[7] + " ".repeat(
                29) + resourceTemp[7]);
        System.out.println(commonObjs[8] +
                (leaderboard.size() > 8 ? leaderboard.get(8) + "     " : " ".repeat(leaderboard.getFirst().length() + 5)) +
                goldTemp[8] + " ".repeat(
                29) + resourceTemp[8]);
        System.out.println(commonObjs[9] +
                (leaderboard.size() > 9 ? leaderboard.get(9) + "     " : " ".repeat(leaderboard.getFirst().length() + 5)) +
                goldTemp[9] + " ".repeat(
                29) + resourceTemp[9]);
        System.out.println(commonObjs[10]);
    }

    /**
     * Prints the two common objectives
     */
    private String[] showCommonObjectives(){
        String[] out = new String[11];

        out[0] = "Common objectives:" + "     ";

        int repeat = "Common objectives:".length();

        String[] obj = cards.get(commonObjectives[0] - 1).printCardInfo().split("X");
        out[1] = obj[0] + " ".repeat(repeat - obj[0].length() + 5);
        out[2] = obj[1] + " ".repeat(repeat - obj[1].length() + 5);
        out[3] = obj[2] + " ".repeat(repeat - 6);
        out[4] = obj[3] + " ".repeat(repeat - 6);
        out[5] = obj[4] + " ".repeat(repeat - 6);

        obj = cards.get(commonObjectives[1] - 1).printCardInfo().split("X");
        out[6] = obj[0] + " ".repeat(repeat - obj[0].length() + 5);
        out[7] = obj[1] + " ".repeat(repeat - obj[1].length() + 5);
        out[8] = obj[2] + " ".repeat(repeat - 6);
        out[9] = obj[3] + " ".repeat(repeat - 6);
        out[10] = obj[4] + " ".repeat(repeat - 6);

        return out;
    }

    /**
     * Prints the client's field for the CLI
     */
    private void printOwnerField() {
        ArrayList<String> fieldLines= ((PlayerFieldViewCli)ownerField).printField();

        if (ownerField == null) {
            System.out.println("Your field is currently empty");
        }

        StringBuilder[] hand= buildHand();

        int spaces = (playerHand.size() == 2 ? 28 : 42);

        System.out.println("Your field:");

        for(int i = 0; i < fieldLines.size(); i++){
            if (i <= fieldLines.size() - 5 && i >= fieldLines.size() - 13){
                System.out.println(hand[13 - fieldLines.size() + i] + fieldLines.get(i));
            } else {
                System.out.println(" ".repeat(spaces) + fieldLines.get(i));
            }
        }
    }

    /**
     * Prints the hand of the player.
     */
    public void printHand()  {
        printSpacer(5);

        if(playerHand == null || playerHand.size() < 2){
            return;
        }

        System.out.println("You have these following cards in your hand:\n");

        StringBuilder[] hand = buildHand();

        for (StringBuilder stringBuilder : hand) {
            System.out.println(stringBuilder);
        }
    }

    /**
     * @return the current hand of the player, formatted to be printed
     */
    private StringBuilder[] buildHand() {
        StringBuilder[] hand = new StringBuilder[9];

        for(int i = 0; i < hand.length; i++){
            hand[i]= new StringBuilder();
        }

        for(Integer i: playerHand){
            String[] cardAsciiFront= printCardAsciiFront(i);
            String[] cardAsciiBack= printCardAsciiBack(i);
            hand[0].append("ID: ").append(i).append(i<10?"         ":"        ");
            hand[1].append("Front:").append("        ");
            hand[2].append("|").append(cardAsciiFront[0]).append("|").append("   ");
            hand[3].append("|").append(cardAsciiFront[1]).append("|").append("   ");
            hand[4].append("|").append(cardAsciiFront[2]).append("|").append("   ");
            hand[5].append("Back:").append("         ");
            hand[6].append("|").append(cardAsciiBack[0]).append("|").append("   ");
            hand[7].append("|").append(cardAsciiBack[1]).append("|").append("   ");
            hand[8].append("|").append(cardAsciiBack[2]).append("|").append("   ");
        }

        return hand;
    }

    /**
     * Array of 3 Strings
     * Prints the card with the specified id's front
     */
    public static String[] printCardAsciiFront(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id - 1);
        return pc.asciiArtFront();
    }

    /**
     * Array of 3 Strings
     * Prints the card with the specified id's back
     */
    public static String[] printCardAsciiBack(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id - 1);
        return pc.asciiArtBack();
    }

    /**
     * @param id of the requested card
     */
    public static void printCardDetailed(int id){
        if (id > 86) {
            String[] obj= cards.get(id - 1).printCardInfo().split("X");
            System.out.print("Objective Card:  ");
            System.out.println(obj[0]);
            System.out.println(obj[1]);
            System.out.println(obj[2]);
            System.out.println(obj[3]);
            System.out.println(obj[4]);
        } else {
            System.out.println("\n" + cards.get(id - 1).printCardInfo() + "\n");
        }
    }

    private void printFieldWithoutHand(String opponentName) {
        if (opponentName.equals(playerName)) {
            printOwnerField();
        } else if (!opponentFields.containsKey(opponentName)) {
            this.display("Incorrect player name.");
            return;
        }

        System.out.println(opponentName +"'s field:");

        for (String s:((PlayerFieldViewCli)opponentFields.get(opponentName)).printField()) {
            System.out.println(s);
        }
    }

    private void printSpacer(int n){
        if (n < 0) return;
        System.out.print("\n".repeat(n));
    }
}

