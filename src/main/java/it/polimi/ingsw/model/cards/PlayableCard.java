package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;

/**
 * This class represents all the cards that can be placed in the player area.
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
    private int placedInTurn;
    private Point position;

    /**
     * Constructs a PlayableCard with the specified parameters.
     *
     * @param ID              id of the card
     * @param topRight        top right corner symbol
     * @param topLeft         top left corner symbol
     * @param bottomLeft      bottom left corner symbol
     * @param bottomRight     bottom right corner symbol
     * @param backTopRight    back top right corner symbol
     * @param backTopLeft     back top left corner symbol
     * @param backBottomLeft  back bottom left corner symbol
     * @param backBottomRight back bottom right corner symbol
     * @param colour          colour of the card
     */
    public PlayableCard(int ID, TokenType topRight, TokenType topLeft, TokenType bottomLeft, TokenType bottomRight, TokenType backTopRight, TokenType backTopLeft, TokenType backBottomLeft, TokenType backBottomRight, CardType colour) {
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.backTopRight = backTopRight;
        this.backTopLeft = backTopLeft;
        this.backBottomLeft = backBottomLeft;
        this.backBottomRight = backBottomRight;
        this.colour = colour;
        this.ID = ID;
        placedInTurn = -1;
        isFacingUp = false;
    }

    /**
     * Method used in the cli in response of a {@link it.polimi.ingsw.controller.userCommands.CardDetailCommand}
     * Gets the card's data as a String with this format:
     * CardType: [CardColor||    ]
     * Front of the card: {FrontCorners}
     * Back of the card : {BackCorners}
     *
     * @return cardData
     */
    @Override
    public String printCardInfo() {
        String cardData = super.printCardInfo();

        if (ID <= 40) {
            cardData = cardData + "\nResourceCard:" + colour.toString() + "\n";
        } else if (ID <= 80) {
            cardData = cardData + "\nGoldCard:" + colour.toString() + "\n";
        } else {
            cardData = cardData + "\nStartingCard:\n";
        }

        return cardData + "Front of the card:\n" + "TopRight:" + topRight.toString() + "    TopLeft:" + topLeft.toString() + "    BottomLeft:" + bottomLeft.toString() + "    BottomRight:" + bottomRight.toString() + "\n" + "Back of the card:\n" + "TopRight:" + backTopRight.toString() + "    TopLeft:" + backTopLeft.toString() + "    BottomLeft:" + backBottomLeft.toString() + "    BottomRight:" + backBottomRight.toString() + "\n";
    }

    /**
     * @return the card's backside colour
     */
    public CardType getCardColour(){
        return colour;
    }

    /**
     * Checks if the card's front side is facing upwards.
     *
     * @return true if the card's front side is facing upwards, false otherwise
     * @throws NotPlacedException if the card has not been placed
     */
    public synchronized boolean isFacingUp() throws NotPlacedException {
        if (placedInTurn == -1) {
            throw new NotPlacedException();
        }

        return isFacingUp;
    }

    /**
     * Gets the top right symbol of the placed card.
     *
     * @return the top right symbol if the card is facing up, back top right symbol otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized TokenType getPlacedTopRight() throws NotPlacedException {
        if (isFacingUp()) {
            return topRight;
        } else {
            return backTopRight;
        }
    }

    /**
     * Gets the top left symbol of the placed card.
     *
     * @return the top left symbol if the card is facing up, back top left symbol otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized TokenType getPlacedTopLeft() throws NotPlacedException {
        if (isFacingUp()) {
            return topLeft;
        } else {
            return backTopLeft;
        }
    }

    /**
     * Gets the bottom left symbol of the placed card.
     *
     * @return the bottom left symbol if the card is facing up, back bottom left symbol otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized TokenType getPlacedBottomLeft() throws NotPlacedException {
        if (isFacingUp()) {
            return bottomLeft;
        } else {
            return backBottomLeft;
        }
    }

    /**
     * Gets the bottom right symbol of the placed card.
     *
     * @return the bottom right symbol if the card is facing up, back bottom right symbol otherwise
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized TokenType getPlacedBottomRight() throws NotPlacedException {
        if (isFacingUp()) {
            return bottomRight;
        } else {
            return backBottomRight;
        }
    }

    /**
     * Gets the position of the placed card.
     *
     * @return the position of the placed card
     * @throws NotPlacedException if the card hasn't been placed
     */
    public synchronized Point getPosition() throws NotPlacedException {
        if (placedInTurn == -1) {
            throw new NotPlacedException();
        }

        return position;
    }

    /**
     * Places the card on the specified point.
     *
     * @param p          the point on which the card has been placed by the player
     * @param turn       the turn in which the card has been placed
     * @param isFacingUp whether the card is facing up
     * @throws AlreadyPlacedException if the card has already been placed
     */
    public synchronized void placeCard(Point p, int turn, boolean isFacingUp) throws AlreadyPlacedException {
        if (placedInTurn != -1) {
            throw new AlreadyPlacedException();
        }

        position = p;
        placedInTurn = turn;
        this.isFacingUp = isFacingUp;
    }

    /**
     * Returns the ASCII art for the front of the card.
     *
     * @return an array containing two strings: the first is the top line, the second is the bottom line
     */
    public String[] asciiArtFront() {
        String c1 = topLeft.toString();
        String c2 = topRight.toString();
        String c3 = bottomLeft.toString();
        String c4 = bottomRight.toString();

        String[] art= new String[3];

        String backgroundFont = colour == CardType.animal ? "\u001B[44m" :
                colour == CardType.insect?"\u001B[45m" :
                colour == CardType.fungi?"\u001B[41m" : colour == CardType.plant ? "\u001B[42m" : "";
        backgroundFont = "\u001B[49m" + backgroundFont;

        art[0] = c1 + " " + " " + " " + c2;
        art[1] = "\u001B[49m" + "         " + "\u001B[0m";//9 spaces
        art[2] = c3 + backgroundFont + "   \u001B[0m" + c4;

        return art;
    }

    /**
     * Returns the ASCII art for the back of the card.
     *
     * @return an array containing two strings: the first is the top line, the second is the bottom line
     */
    public String[] asciiArtBack() {
        String[] art = new String[3];

        art[0] = TokenType.empty + " " + " " + " " + TokenType.empty;
        art[1] = "\u001B[49m" + "   " + colour + "   ";//9 spaces
        art[2] = TokenType.empty + "   " + TokenType.empty;

        return art;
    }
}
