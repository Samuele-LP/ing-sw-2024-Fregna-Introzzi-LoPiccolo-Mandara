package it.polimi.ingsw.view.Field;

import it.polimi.ingsw.SimpleField;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that provides a method to help the GUI represent the user field
 */
public class PlayerFieldViewGui extends PlayerFieldView{

    /**
     * Constructor to initialize the player's field view with the owner's name.
     *
     * @param owner the name of the field's owner
     */
    public PlayerFieldViewGui(String owner) {
        super(owner);
    }

    /**
     * Converts the current state of the field to a SimpleField object.
     *
     * @return a SimpleField object representing the current state of the field
     */
    public SimpleField getAsSimpleField(){
        return new SimpleField(new ArrayList<>(simpleCards), new HashMap<>(visibleSymbols), owner);
    }
}
