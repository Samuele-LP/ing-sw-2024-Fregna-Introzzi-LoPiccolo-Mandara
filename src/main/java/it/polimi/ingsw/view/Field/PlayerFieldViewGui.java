package it.polimi.ingsw.view.Field;

import it.polimi.ingsw.SimpleField;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerFieldViewGui extends PlayerFieldView{
    public PlayerFieldViewGui(String owner) {
        super(owner);
    }

    public SimpleField getAsSimpleField(){
        return new SimpleField(new ArrayList<>(simpleCards),new HashMap<>(visibleSymbols),owner);
    }
}
