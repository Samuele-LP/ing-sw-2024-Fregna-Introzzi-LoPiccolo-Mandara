package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;


/**
 * This class represents all the cards that can be placed in the player area
 */
public class PlayableCard extends Card {
    protected final TokenType topRight;
    protected final TokenType topLeft;
    protected TokenType bottomLeft;
    protected final TokenType bottomRight;
    protected final TokenType backTopRight;
    protected final TokenType backTopLeft;
    protected final TokenType backBottomLeft;
    protected final TokenType backBottomRight;
    protected final CardType colour;
    private boolean isFacingUp;
    private int placedInTurn;//attribute used to determine which cards are on top of others, default value is -1
    private Point position;

    /**
     * @param ID id of the card
     * @param topRight top right corner
     * @param topLeft top left corner
     * @param bottomLeft bottom left corner
     * @param bottomRight bottom right corner
     * @param backTopRight back top right corner
     * @param backTopLeft back top left corner
     * @param backBottomLeft back bottom left corner
     * @param backBottomRight back bottom right corner
     * @param colour colour of the card
     */
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
        String cardData = super.printCardInfo();
        if(ID<=40){
            cardData=cardData+ "\nResourceCard:" + colour.toString() + "\n";
        }
        else if(ID<=80){
            cardData= cardData+"\nGoldCard:" + colour.toString() + "\n";
        }
        else{
            cardData=cardData+ "\nStartingCard:\n";
        }
        return cardData+"Front of the card:\n"+"TopRight:"+topRight.toString()+"    TopLeft:"+topLeft.toString()+"    BottomLeft:"+bottomLeft.toString()+"    BottomRight:"+bottomRight.toString()+"\n"+"Back of the card:\n"+"TopRight:"+backTopRight.toString()+"    TopLeft:"+backTopLeft.toString()+"    BottomLeft:"+backBottomLeft.toString()+"    BottomRight:"+backBottomRight.toString()+"\n";
    }

    /**
     * @return the card's backside colour
     */
    public CardType getCardColour(){
        return colour;
    }
    /**
     * @return isFacingUp  True if the card's front side is facing upwards, false otherwise
     * @throws NotPlacedException if the card has not been placed
     */
    public synchronized boolean isFacingUp() throws NotPlacedException {
        if(placedInTurn==-1){
            throw new NotPlacedException();
        }
        return isFacingUp;
    }

    /**
     * Returns the Card placing turn, -1 if the card hasn't been palyed
     * @return placedInTurn
     */
    public synchronized int getPlacementTurn(){
        return placedInTurn;
    }

    /**
     * @return topRight if the card is facing up, backTopRight otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized TokenType getPlacedTopRight()throws NotPlacedException{
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
    public synchronized TokenType getPlacedTopLeft()throws NotPlacedException{
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
    public synchronized TokenType getPlacedBottomLeft()throws NotPlacedException{
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
    public synchronized TokenType getPlacedBottomRight()throws NotPlacedException{
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
    public synchronized Point getPosition()throws NotPlacedException{
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
    public synchronized void placeCard(Point p,int turn,boolean isFacingUp)throws AlreadyPlacedException {
        if(placedInTurn!=-1){
            throw new AlreadyPlacedException();
        }
        position=p;
        placedInTurn=turn;
        this.isFacingUp=isFacingUp;
    }
    /**
     * This method returns the ascii art for the front of the card
     * @return an array containing two strings. The first is the top line, the second is the bottom line.
     */
    public String[] asciiArtFront()  {
        String c1 = topLeft.toString();
        String c2 = topRight.toString();
        String c3 = bottomLeft.toString();
        String c4 = bottomRight.toString();
        String[] art= new String[3];
        String backgroundFont=colour==CardType.animal?"\u001B[44m":
                colour==CardType.insect?"\u001B[45m":
                colour==CardType.fungi?"\u001B[41m": "\u001B[42m";
        backgroundFont="\u001B[48;2;255;255;255m"+backgroundFont;
        art[0] =c1 + " " + " "+" "+ c2;
        art[1] ="\u001B[48;2;255;255;255m"+"         "+"\u001B[0m";//9 spaces
        art[2] =c3 +backgroundFont+"   \u001B[0m"+c4;
        return art;
    }

    /**
     * This method returns the ascii art for the back of the card
     * @return an array containing two strings. The first is the top line, the second is the bottom line.
     */
    public String[] asciiArtBack(){
        String[] art= new String[3];
        art[0] =TokenType.empty + " " + " "+" "+" " + TokenType.empty;
        art[1] ="   "+colour+"   ";//9 spaces
        art[2] =TokenType.empty + "   " + TokenType.empty;
        return art;
    }
}
