package it.polimi.ingsw.view;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.SimpleField;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.network.messages.serverToClient.PlayerReconnectedMessage;
import it.polimi.ingsw.network.messages.serverToClient.SharedFieldUpdateMessage;
import it.polimi.ingsw.view.Deck.DeckViewCli;
import it.polimi.ingsw.view.Field.PlayerFieldViewCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameViewCli extends GameView{
    public GameViewCli(){
        super();
    }
    public GameViewCli(PlayerReconnectedMessage mes, String name) {
        super();
        this.ownerField= new PlayerFieldViewCli(mes.getOwnerField());
        List<String> opponentNames= mes.getOpponentFields().stream().map(SimpleField::getName).toList();
        this.playerName= name;
        this.playerHand= new ArrayList<>(mes.getPlayerHand());
        try {
            this.goldDeck = new DeckViewCli("Gold");
            this.resourceDeck = new DeckViewCli("Resource");
        }catch (IOException e){
            System.out.println("IOException while creating the decks after a reconnection");
        }
        SharedFieldUpdateMessage sharedField=mes.getSharedField();
        this.scoreTrack= sharedField.getScoreTrack();
        for(String s: opponentNames){
            SimpleField tempField=null;
            for(SimpleField f: mes.getOpponentFields()){
                if(f.getName().equals(s)){
                    tempField=f;
                }
            }
            if(tempField==null){
                throw new RuntimeException("Error while reconnecting");
            }
            opponentFields.put(s,new PlayerFieldViewCli(tempField));
        }
        this.updateDecks(sharedField.getGoldBackside(),sharedField.getResourceBackside(),sharedField.getVisibleCards());
        commonObjectives[0]=mes.getFirstCommonObjective();
        commonObjectives[1]=mes.getSecondCommonObjective();
        ownerField=new PlayerFieldViewCli();
    }


    /**
     * After the constructor the methods to update the decks must be called by the controller with the necessary information
     *
     * @param otherPlayerNames
     * @param playerName
     * @param startingCard
     * @param firstCommonObjective
     * @param secondCommonObjective
     */
    @Override
    public void gameStarting(List<String> otherPlayerNames, String playerName, int startingCard, int firstCommonObjective, int secondCommonObjective) throws IOException {
        this.playerName = playerName;
        startingCardID=startingCard;
        this.goldDeck = new DeckViewCli("Gold");
        this.resourceDeck = new DeckViewCli("Resource");
        HashMap<String ,Integer> startingScoreTrack=  new HashMap<>();
        for(String s: otherPlayerNames){
            if(!opponentFields.containsKey(s)) {
                opponentFields.put(s, new PlayerFieldViewCli());
            }
            startingScoreTrack.put(s,0);
        }
        commonObjectives[0]=firstCommonObjective;
        commonObjectives[1]=secondCommonObjective;
        scoreTrack=new ImmutableScoreTrack(startingScoreTrack,new HashMap<>());
        ownerField=new PlayerFieldViewCli();
    }

    /**
     * Method that shows information about the common field
     */
    @Override
    public void printCommonField() {
        List<String> leaderboard=scoreTrack.printTable();
        String[] goldTemp= goldDeck.printDeck();
        String[] resourceTemp= resourceDeck.printDeck();
        String[] commonObjs= showCommonObjectives();
        System.out.println(commonObjs[0]+
                leaderboard.get(0)+"     "+
                goldTemp[0]+" ".repeat(
                30)+resourceTemp[0]);
        System.out.println(commonObjs[1]+
                leaderboard.get(1)+"     "+
                goldTemp[1]+" ".repeat(
                goldTemp[1].equals("The deck has no more cards in it.")?7:11)+resourceTemp[1]);
        System.out.println(commonObjs[2]+
                leaderboard.get(2)+"     "+
                goldTemp[2]+" ".repeat(
                goldTemp[2].equals("There is no first visible card")?10:17)+resourceTemp[2]);
        System.out.println(commonObjs[3]+
                leaderboard.get(3)+"     "+
                goldTemp[3]+" ".repeat(
                29)+resourceTemp[3]);
        System.out.println(commonObjs[4]+
                leaderboard.get(4)+"     "+
                goldTemp[4]+" ".repeat(
                29)+resourceTemp[4]);
        System.out.println(commonObjs[5]+
                leaderboard.get(5)+"     "+
                goldTemp[5]+" ".repeat(
                29)+resourceTemp[5]);
        System.out.println(commonObjs[6]+
                leaderboard.get(6)+"     "+
                goldTemp[6]+" ".repeat(
                goldTemp[6].equals("There is no second visible card")?9:5)+resourceTemp[6]);
        System.out.println(commonObjs[7]+
                leaderboard.get(7)+ "     "+
                goldTemp[7]+" ".repeat(
                29)+resourceTemp[7]);
        System.out.println(commonObjs[8]+
                (leaderboard.size()>8?leaderboard.get(8)+"     ":" ".repeat(leaderboard.getFirst().length()+5))+
                goldTemp[8]+" ".repeat(
                29)+resourceTemp[8]);
        System.out.println(commonObjs[9]+
                (leaderboard.size()>9?leaderboard.get(9)+"     ":" ".repeat(leaderboard.getFirst().length()+5))+
                goldTemp[9]+" ".repeat(
                29)+resourceTemp[9]);
        System.out.println(commonObjs[10]);
    }
    /**
     * Prints the two common objectives
     */
    private String[] showCommonObjectives(){
        String[] out= new String[11];
        out[0]="Common objectives:"+"     ";
        int repeat="Common objectives:".length();
        String[] obj = cards.get(commonObjectives[0]-1).printCardInfo().split("X");
        out[1]=obj[0]+" ".repeat(repeat-obj[0].length()+5);
        out[2]=obj[1]+" ".repeat(repeat-obj[1].length()+5);
        out[3]=obj[2]+" ".repeat(repeat-6);
        out[4]=obj[3]+" ".repeat(repeat-6);
        out[5]=obj[4]+" ".repeat(repeat-6);
        obj = cards.get(commonObjectives[1]-1).printCardInfo().split("X");
        out[6]=obj[0]+" ".repeat(repeat-obj[0].length()+5);
        out[7]=obj[1]+" ".repeat(repeat-obj[1].length()+5);
        out[8]=obj[2]+" ".repeat(repeat-6);
        out[9]=obj[3]+" ".repeat(repeat-6);
        out[10]=obj[4]+" ".repeat(repeat-6);
        return out;
    }
    /**
     * Prints the client's field for the CLI
     */
    @Override
    public void printOwnerField() {
        ArrayList<String> fieldLines= ownerField.printField();
        if(ownerField==null){
            System.out.println("Your field is currently empty");
        }
        StringBuilder[] hand= buildHand();
        int spaces=(playerHand.size()==2?28:42);
        System.out.println("Your field:");
        for(int i=0;i<fieldLines.size();i++){
            if(i<=fieldLines.size()-5&&i>=fieldLines.size()-13){
                System.out.println(hand[13-fieldLines.size()+i]+fieldLines.get(i));
            }else {
                System.out.println(" ".repeat(spaces)+fieldLines.get(i));
            }
        }
    }

    /**
     * Prints the requested player's field for the CLI
     *
     * @param name name of the opponent whose field will be shown
     */
    @Override
    public void printOpponentField(String name) {
        System.out.println("\n\n\n");
        if(name.equals(playerName)){
            printOwnerField();
        }
        else if(!opponentFields.containsKey(name)){
            showText("Incorrect player name.");
            return;
        }
        System.out.println(name +"'s field:");
        for(String s:opponentFields.get(name).printField()){
            System.out.println(s);
        }
    }

    /**
     * This method memorizes the two possible choices and then shows them
     *
     * @param firstChoice  first objective choice
     * @param secondChoice second objective choice
     */
    @Override
    public void secretObjectiveChoice(int firstChoice, int secondChoice){
        System.out.println("Here are your secret objective choices");
        secretObjectiveChoices[0]=firstChoice;
        secretObjectiveChoices[1]=secondChoice;
        showSecretObjectives();
    }

    /**
     * Prints the secret objective or the secret objective choices depending on whether the objective was already chosen
     */
    @Override
    public void showSecretObjectives() {
        if (secretObjectiveChoices[0] < 1) {
            return;
        }
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
     * Prints the hand of the player.
     */
    @Override
    public void printHand()  {
        System.out.println("You have these following cards in your hand:\n");
        StringBuilder[] hand= buildHand();
        for (StringBuilder stringBuilder : hand) {
            System.out.println(stringBuilder);
        }
    }

    /**
     * Displays the starting card for the player to see
     */
    @Override
    public void printStartingCard()  {
        StartingCard startingCard= (StartingCard) cards.get(startingCardID-1);
        System.out.println("Here is your starting card:\nID: "+startingCardID);
        System.out.println("Front:          Back:");
        System.out.println("|"+startingCard.asciiArtFront()[0]+"|     |"+startingCard.asciiArtBack()[0]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[1]+"|     |"+startingCard.asciiArtBack()[1]+"|");
        System.out.println("|"+startingCard.asciiArtFront()[2]+"|     |"+startingCard.asciiArtBack()[2]+"|\n");
    }
    /**
     * @return the current hand of the player, formatted to be printed
     */
    private StringBuilder[] buildHand() {
        StringBuilder[] hand= new StringBuilder[9];
        for(int i=0;i<hand.length;i++){
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
     * */
    public static String[] printCardAsciiFront(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id-1);
        return pc.asciiArtFront();
    }
    /**
     * Array of 3 Strings
     * Prints the card with the specified id's back
     * */
    public static String[] printCardAsciiBack(int id) {
        PlayableCard pc= (PlayableCard) GameView.cards.get(id-1);
        return pc.asciiArtBack();
    }
    /**
     * @param id of the requested card
     * @return the detailed data on the card with the given id
     */
    public static void printCardDetailed(int id){
        if(id>86){
            String[] obj= cards.get(id-1).printCardInfo().split("X");
            System.out.print("Objective Card:  ");
            System.out.println(obj[0]);
            System.out.println(obj[1]);
            System.out.println(obj[2]);
            System.out.println(obj[3]);
            System.out.println(obj[4]);
        }else {
            GameView.showText("\n" + cards.get(id - 1).printCardInfo() + "\n");
        }
    }
}
