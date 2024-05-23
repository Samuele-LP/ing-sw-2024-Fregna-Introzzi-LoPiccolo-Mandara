package it.polimi.ingsw.exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException(String m){
        super(m);
    }
    public EmptyDeckException(){
        super();
    }
}
