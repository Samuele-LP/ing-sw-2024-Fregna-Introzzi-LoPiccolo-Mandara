package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreationTest {
    @Test
    void getGoldCards() throws IOException {
        List<Card> gold=Creation.getGoldCards();
        List<Integer> IDList= new ArrayList<>();
        for(Card c: gold){
            if(!(c instanceof GoldCard)){
                fail();
            }
            if(IDList.contains(c.getID())){
                System.out.println(c.getID());
                fail();
            }
            if(c.getID()>80||c.getID()<41){
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    void getStartingCards() throws IOException {
        List<Card> starter=Creation.getStartingCards();
        List<Integer> IDList= new ArrayList<>();
        for(Card c: starter){
            if(!(c instanceof StartingCard)){
                fail();
            }
            if(IDList.contains(c.getID())){
                System.out.println(c.getID());
                fail();
            }
            if(c.getID()<81||c.getID()>86){
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    void getResourceCards() throws IOException {
        List<Card> resourceCards=Creation.getResourceCards();
        List<Integer> IDList= new ArrayList<>();
        for(Card c: resourceCards){
            if(!(c instanceof ResourceCard)){
                fail();
            }
            if(IDList.contains(c.getID())){
                System.out.println(c.getID());
                fail();
            }
            if(c.getID()<0||c.getID()>40){
                fail();
            }
            IDList.add(c.getID());
        }
    }

    @Test
    void getObjectiveCards() throws IOException {
        List<Card> gold=Creation.getObjectiveCards();
        List<Integer> IDList= new ArrayList<>();
        for(Card c: gold){
            if(!(c instanceof ObjectiveCard)){
                fail();
            }
            if(IDList.contains(c.getID())){
                System.out.println(c.getID());
                fail();
            }
            if(c.getID()<87||c.getID()>102){
                fail();
            }
            IDList.add(c.getID());
        }
    }
}