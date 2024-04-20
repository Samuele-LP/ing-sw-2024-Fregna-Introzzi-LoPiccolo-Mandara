package it.polimi.ingsw.model.player;

import it.polimi.ingsw.Creation;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ScoreTrack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.TokenType;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player blockedTest;
    ScoreTrack stub = new ScoreTrack("","");
    List<Card> resource= Creation.getResourceCards();
    List<Card> gold=Creation.getGoldCards();
    List<Card> starter=Creation.getStartingCards();
    List<Card> objectives=Creation.getObjectiveCards();
    boolean blockTest=true;
    PlayerTest() throws IOException {
    }
    @BeforeEach
    void setUp() throws IllegalStartingCardException, NotPlacedException, AlreadyPlacedException {
        //This test refers to a field condition that blocks every possible move
        PlayableCard[] startingHand= new PlayableCard[3];
        startingHand[0]=(PlayableCard) resource.get(16);
        startingHand[1]=(PlayableCard) resource.get(0);
        startingHand[2]=(PlayableCard) gold.get(0);
        blockedTest= new Player("blocked",(PlayableCard) starter.get(4),startingHand );
        blockedTest.placeStartingCard(true);
        assertDoesNotThrow(()->
                blockedTest.placeCard(1,-1,1,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(16)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(17,1,1,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(7)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(8,2,0,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(22)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(23,3,1,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(33)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(34,-2,0,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(35)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(36,-2,2,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(12)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(13,0,2,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) gold.get(16)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(57,4,0,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(2)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(3,-1,3,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) resource.get(38)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(39,-2,4,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) gold.get(18)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(59,-3,5,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) gold.get(26)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(67,-1,5,true,stub)
        );
        assertDoesNotThrow(()->
                blockedTest.receiveDrawnCard((PlayableCard) gold.get(38)));
        assertDoesNotThrow(()->
                blockedTest.placeCard(79,-2,6,true,stub)
        );
        assertThrows(PlayerCantPlaceAnymoreException.class,()->{
            blockedTest.getAvailablePositions();
        });
    }
    @Test
    void getAvailablePositions() {
        if(blockTest){
            assertThrows(PlayerCantPlaceAnymoreException.class,()->{
                blockedTest.getAvailablePositions();
            });
        }
    }
    @Test
    void placeCard(){

    }

    @Test
    void placeStartingCard() {
        assertThrows(AlreadyPlacedException.class,()->{
            blockedTest.placeStartingCard(false);
        });
    }

    @Test
    void calculateSecretObjective() {
        if(blockTest)
            assertEquals(14, blockedTest.getPoints());
        for(Card o: objectives){
            if(blockTest){
                blockedTest.calculateCommonObjectives((ObjectiveCard) o,null);
            }
        }
        if(blockTest)
            assertEquals(26, blockedTest.getPoints());
    }

    @Test
    void quantityOfCards(){
        assertTrue(blockedTest.quantityOfCards()<=3);
    }
}