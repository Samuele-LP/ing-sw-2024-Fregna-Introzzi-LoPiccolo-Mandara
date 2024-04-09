package it.polimi.ingsw;
public class Point {
    private final int xCoord;
    private final int yCoord;
    public Point(int x, int y){
        xCoord=x;
        yCoord=y;
    }
    public int getX(){return xCoord;}
    public int getY(){return yCoord;}
    @Override
    public String toString(){
        return "X:"+xCoord+"  Y:"+yCoord;
    }

    /**
     * Overridden to guarantee correct HashMap functioning
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
     * Overridden to guarantee correct HashMap functioning
     * @return the Point's hashcode, calculated by using String's hashCode implementation
     */
    @Override
    public int hashCode(){
        return (xCoord+" "+yCoord).hashCode();
    }
}
