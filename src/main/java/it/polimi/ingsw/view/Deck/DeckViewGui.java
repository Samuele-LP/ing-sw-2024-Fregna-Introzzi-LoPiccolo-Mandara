package it.polimi.ingsw.view.Deck;

import it.polimi.ingsw.model.enums.CardType;

import java.io.IOException;

public class DeckViewGui extends DeckView{
    public DeckViewGui(String type) throws IOException {
        super(type);
    }
    public Integer getFirstVisible(){
        return firstVisible;
    }
    public Integer getSecondVisible(){
        return secondVisible;
    }
    public CardType getTopColour(){
        return topCard;
    }
}
