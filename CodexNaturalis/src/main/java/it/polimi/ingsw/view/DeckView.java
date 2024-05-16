package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;

/**
 * Used to memorize the necessary information about the decks to be shown to the player
 */
public class DeckView {
    private final String type;
    private CardType topCard;
    private Integer firstVisible;
    private Integer secondVisible;

    public DeckView(String type) throws IOException {
        this.type=type;
    }

    /**
     * @param top colour of the current top of the deck
     * @param firstVisibleID id of the current first visible card
     * @param secondVisibleID id of the current second visible card
     */
    public void update(CardType top, Integer firstVisibleID, Integer secondVisibleID){
        topCard=top;
        firstVisible=firstVisibleID;
        secondVisible=secondVisibleID;
    }
    /**
     * Prints the deck's information for the cli.
     * Array of 10 Strings.
     */
    public String[] printDeck(){
        String[] lines= new String[10];
        lines[0]=type+" deck.";
        if(topCard!=null){
            lines[1]="The top card's type is "+topCard.fullString();
            //26
        }else{
            lines[1]="The deck has no more cards in it.";
            //33
        }
        if(firstVisible!=null){
            lines[2]="First visible id is: "+firstVisible;//23
            String[]card= GameView.printCardAsciiFront(firstVisible);
            lines[3]="|"+card[0]+"|";
            lines[4]="|"+card[1]+"|";
            lines[5]="|"+card[2]+"|";
        }else{
            lines[2]="There is no first visible card";//30
            lines[3]="           ";
            lines[4]="           ";
            lines[5]="           ";
        }
        if(secondVisible!=null){
            lines[6]="The second visible card's id is: "+secondVisible;//35
            String[]card= GameView.printCardAsciiFront(secondVisible);
            lines[7]="|"+card[0]+"|";
            lines[8]="|"+card[1]+"|";
            lines[9]="|"+card[2]+"|";
        }else{
            lines[6]="There is no second visible card";//31
            lines[7]="           ";
            lines[8]="           ";
            lines[9]="           ";
        }
        return lines;
    }
}
