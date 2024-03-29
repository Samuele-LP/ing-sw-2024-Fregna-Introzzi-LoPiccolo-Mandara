package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;


/**
 * This class represents all the cards that can be placed in the player area
 */
public class PlayableCard extends Card {
    private final TokenType topRight;
    private final TokenType topLeft;
    private final TokenType bottomLeft;
    private final TokenType bottomRight;
    private final TokenType backTopRight;
    private final TokenType backTopLeft;
    private final TokenType backBottomLeft;
    private final TokenType backBottomRight;
    private final CardType colour;
    private boolean isFacingUp;
    private int placedInTurn;//attribute used to determine which cards are on top of others, default value is -1
    private Point position;
    public PlayableCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour){
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.backTopRight = backTopRight;
        this.backTopLeft = backTopLeft;
        this.backBottomLeft = backBottomLeft;
        this.backBottomRight = backBottomRight;
        this.colour = colour;
        this.ID=ID;
        placedInTurn=-1;
        isFacingUp=false;
    }

    /**
     * Gets the card's data as a String with this format:
     * CardType: [CardColor||    ]
     * Front of the card: {FrontCorners}
     * Back of the card : {BackCorners}
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        String cardData;
        if(ID<=40){
            cardData= "ResourceCard:" + colour.toString() + "\n";
        }
        else if(ID<=80){
            cardData= "GoldCard:" + colour.toString() + "\n";
        }
        else{
            cardData= "StartingCard:\n";
        }
        return cardData+"Front of the card:\n"+"TopRight:"+topRight.toString()+"    TopLeft:"+topLeft.toString()+"    BottomLeft:"+bottomLeft.toString()+"    BottomRight:"+bottomRight.toString()+"\n"+"Back of the card:\n"+"TopRight:"+backTopRight.toString()+"    TopLeft:"+backTopLeft.toString()+"    BottomLeft:"+backBottomLeft.toString()+"    BottomRight:"+backBottomRight.toString()+"\n";
    }

    /**
     * @return isFacingUp  True if the card's front side is facing upwards, false otherwise
     * @throws NotPlacedException if the card has not been placed
     */
    public boolean isFacingUp() throws NotPlacedException {
        if(placedInTurn==-1){
            throw new NotPlacedException();
        }
        return isFacingUp;
    }

    /**
     * Returns the Card placing turn, -1 if the card hasn't been palyed
     * @return placedInTurn
     */
    public int getPlacementTurn(){
        return placedInTurn;
    }

    /**
     * @return topRight if the card is facing up, backTopRight otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public TokenType getPlacedTopRight()throws NotPlacedException{
        if(isFacingUp()){
    return topRight;
        }
        else{
            return backTopRight;
        }
    }
    /**
     * @return topLeft if the card is facing up, backTopLeft otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public TokenType getPlacedTopLeft()throws NotPlacedException{
        if(isFacingUp()){
            return topLeft;
        }
        else{
            return backTopLeft;
        }
    }
    /**
     * @return bottomLeft if the card is facing up, backBottomLeft otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public TokenType getPlacedBottomLeft()throws NotPlacedException{
        if(isFacingUp()){
            return bottomLeft;
        }
        else{
            return backBottomLeft;
        }
    }
    /**
     * @return bottomRight if the card is facing up, backBottomRight otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public TokenType getPlacedBottomRight()throws NotPlacedException{
        if(isFacingUp()){
            return bottomRight;
        }
        else{
            return backBottomRight;
        }
    }
    /**
     *
     * @return position
     * @throws NotPlacedException if the card hasn't been placed
     */
    public Point getPosition()throws NotPlacedException{
        if(placedInTurn==-1){
            throw new NotPlacedException();
        }
        return position;
    }
    /**
     * This method will always be called after checking the legality of a move
     * @param p is the reference to the Point on which the card has been placed by the player
     * @param turn is the turn in which the card has been placed
     */
    public void placeCard(Point p,int turn,boolean isFacingUp)throws AlreadyPlacedException {
        if(placedInTurn!=-1){
            throw new AlreadyPlacedException();
        }
        position=p;
        placedInTurn=turn;
        this.isFacingUp=isFacingUp;
    }
}