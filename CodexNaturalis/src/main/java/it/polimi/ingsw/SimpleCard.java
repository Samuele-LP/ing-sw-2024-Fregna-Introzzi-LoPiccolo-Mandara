package it.polimi.ingsw;

/**
 * Class used to memorize how a card has been placed in the view
 */
public class SimpleCard{
    private final int x;
    private final int y;
    private final boolean isFacingUp;
    private final int ID;

    public SimpleCard(int x, int y, boolean isFacingUp, int id) {
        this.x = x;
        this.y = y;
        this.isFacingUp = isFacingUp;
        ID = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFacingUp() {
        return isFacingUp;
    }

    public int getID() {
        return ID;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof SimpleCard other){
            return this.ID==other.ID;
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode(){
        return ID;
    }
}
