module PSP13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires java.rmi;
    requires javafx.graphics;

    exports it.polimi.ingsw.controller.userCommands;
    exports it.polimi.ingsw.Gui;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.view.Deck;
    exports it.polimi.ingsw;
    exports it.polimi.ingsw.model.enums;

    opens it.polimi.ingsw.Gui to javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw to com.google.gson;
    opens it.polimi.ingsw.model.cards to com.google.gson;
    opens it.polimi.ingsw.model.enums to com.google.gson;
    exports it.polimi.ingsw.Gui.controllers.field;
    opens it.polimi.ingsw.Gui.controllers.field to javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.Gui.controllers;
    opens it.polimi.ingsw.Gui.controllers to javafx.fxml, javafx.graphics;
}