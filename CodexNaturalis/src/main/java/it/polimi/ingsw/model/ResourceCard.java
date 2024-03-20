package it.polimi.ingsw.model;

public class ResourceCard extends PlayableCard{
    private final int pointsOnPlacement;

    public ResourceCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour,int points) {
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

    @Override
    public String printCardInfo() {
        return super.printCardInfo()+"Points given by playing this card: "+pointsOnPlacement+"\n";
    }
}
