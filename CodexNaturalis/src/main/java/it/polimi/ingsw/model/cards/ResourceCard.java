package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

/**
 * Class that adds the information necessary to handle a ResourceCard
 */
public class ResourceCard extends PlayableCard {
    private final int pointsOnPlacement;

    public ResourceCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour, int points) {
        super(ID, topRight, topLeft, bottomLeft, bottomRight, backTopRight, backTopLeft, backBottomLeft, backBottomRight, colour);
        pointsOnPlacement=points;
    }

    /**
     * returns how many points the player gets by placing this card
     * @return pointsOnPlacement
     */
    public int getPointsOnPlacement(){
        return pointsOnPlacement;
    }

    /**
     * Gets the card's data as a String with this format:
     * CardType: [CardColor||    ]
     * Front of the card: {FrontCorners}
     * Back of the card : {BackCorners}
     * [Points given by playing this card {pointsOnPlacement}||]
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        if(pointsOnPlacement==0){
            return super.printCardInfo();
        }
        else{
        return super.printCardInfo()+"Points given by playing this card: "+pointsOnPlacement+"\n";
        }
    }
}
