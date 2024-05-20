package it.polimi.ingsw.model;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.network.commonData.ConstantValues;
import it.polimi.ingsw.network.messages.clientToServer.DrawCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;
    private final List<String> players = new ArrayList<>();


    @Before
    public void setUp() {
        try {
            players.add("test1");
            players.add("test2");
            players.add("test3");
            players.add("test4");
            game= new Game(1);//"Invalid number of players" will be displayed
            game.startGame(players);
            game= new Game(2);
            game.startGame(players);
            game= new Game(3);
            game.startGame(players);
            game= new Game(4);
            game.startGame(players);
            game.setStartingCard("test1",false);
            game.setStartingCard("test2",true);
            game.setStartingCard("test3",false);
            game.setStartingCard("test4",true);
            for(String s: players){
                game.placeSecretObjective(s,game.dealSecretObjective(s)[0]);
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.goldDeck) ));
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.goldFirstVisible) ));
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.goldSecondVisible) ));
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.resourceDeck) ));
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.resourceFirstVisible) ));
                assertThrows(HandAlreadyFullException.class,
                        ()-> game.drawCard(s,new DrawCardMessage(PlayerDrawChoice.resourceSecondVisible) ));
            }
            assertTrue(game.getFirstCommonObjective()>86&&game.getFirstCommonObjective()<=102);
            assertTrue(game.getSecondCommonObjective()>86&&game.getSecondCommonObjective()<=102);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void setStartingCard() {
        System.out.println("\n\n\n\n\n\n\n\n");
        assertThrows(AlreadyPlacedException.class,
        ()-> game.setStartingCard("test1",false));
        assertThrows(AlreadyPlacedException.class,
        ()-> game.setStartingCard("test1",true));
        System.out.println("\n\n\n\n\n\n\n\n");
    }

    @Test
    public void dealSecretObjective() {
        System.out.println("\n\n\n\n\n\n\n\n");
        assertThrows(ObjectiveAlreadySetException.class,()->
                game.placeSecretObjective("test1", (ObjectiveCard) Creation.getObjectiveCards().getFirst()));
        System.out.println("\n\n\n\n\n\n\n\n");
    }
    @Test
    public void drawCard() {
        System.out.println("\n\n\n\n\n\n\n\n");
        System.out.println("Gold Top: "+ game.getGoldTop()+"Resource Top: "+ game.getResourceTop());
        System.out.println("Visible: "+ game.getVisibleCards().toString());
        try {
            emptyDeck(PlayerDrawChoice.resourceDeck);
            emptyDeck(PlayerDrawChoice.goldDeck);
        }catch (Exception e){
            fail();
        }
        assertTrue(game.isInFinalPhase());
        assertThrows(NoSuchElementException.class,()->
        System.out.println("Gold Top: "+ game.getGoldTop()+"Resource Top: "+ game.getResourceTop()));
        System.out.println("Visible: "+ game.getVisibleCards().toString());
        System.out.println("\n\n\n\n\n\n\n\n");
    }
    private void emptyDeck(PlayerDrawChoice c) throws Exception {
        try {
            while (true) {
                try {
                    game.drawCard(players.getFirst(), new DrawCardMessage(c));
                } catch (HandAlreadyFullException e) {
                    //does nothing, only serves to empty the decks
                }
            }
        } catch (EmptyDeckException e) {
            System.out.println(c+" deck emptied");
        }
    }
    @Test
    public void playCard() {
       List<Integer> hand= game.getPlayerHand(players.getFirst());
        PlaceCardMessage mes = new PlaceCardMessage(1 ,1,false, hand.getFirst());
        try{
            game.playCard(players.getFirst(),mes);
        }catch (Exception e){
            try{
                mes = new PlaceCardMessage(-1 ,1,false, hand.getFirst());
                game.playCard(players.getFirst(),mes);
            }catch (Exception e1){
                System.out.println("Unlucky starting card randomization");
            }
        }
    }

    @Test
    public void getAvailablePoints() {
        for(String s: players){
            System.out.println("\n\n\n\n\n\n");
            try {
                for (Point p : game.getAvailablePoints(s)) {
                    System.out.print(p+"  ");
                }
            }catch (PlayerCantPlaceAnymoreException e){
                System.out.println("Player is softLocked");
            }catch (NotPlacedException e){
                fail();
            }
        }
    }

    @Test
    public void getPlayerVisibleSymbols() {
        System.out.println(game.getPlayerVisibleSymbols(players.getFirst()).toString());
    }



    @Test
    public void getFinalScore() {
        game.gameOver();
        for(String s:game.getScoreTrack().printTable()){
            System.out.println(s);
        }
        System.out.println(game.getWinners().toString());
    }

    @Test
    public void getStartingCardId() {
        for (String s : players) {
            assertTrue(game.getStartingCardId(s) < 87 && game.getStartingCardId(s) > 80);
        }
    }

    @Test
    public void setPawnColour() {
        game.setPawnColour(players.get(1), ConstantValues.ansiBlue);
    }
}