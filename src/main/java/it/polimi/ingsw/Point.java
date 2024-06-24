package it.polimi.ingsw;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class used to represent the position of the placed cards.
 */
public class Point implements Serializable {

    private final int xCoord;

    private final int yCoord;

    public Point(int x, int y){
        xCoord = x;
        yCoord = y;
    }

    public int getX(){return xCoord;}

    public int getY(){return yCoord;}

    @Override
    public String toString(){
        return ("(" + xCoord + "," + yCoord + ")");
    }

    /**
     * Overridden to guarantee correct Collections' functioning
     * @param obj object to compare
     * @return true if the x and y values are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point)obj;
            return (xCoord == p.xCoord) && (yCoord == p.yCoord);
        }
        return super.equals(obj);
    }

    /**
     * Overridden to maintain the Object.hashCode() contract's validity,
     * but not in a way that ensures that every single Point will have different values. Every Point will still produce the same result
     * @return the Point's hashcode
     */
    @Override
    public int hashCode(){
        return Objects.hash(xCoord,yCoord);
    }
}
