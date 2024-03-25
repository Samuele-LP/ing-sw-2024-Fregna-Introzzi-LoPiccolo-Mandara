package it.polimi.ingsw.model;

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
}
