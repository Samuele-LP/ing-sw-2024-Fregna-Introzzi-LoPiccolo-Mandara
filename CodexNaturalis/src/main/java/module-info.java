module PSP13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires java.rmi;
    requires javafx.graphics;

    exports it.polimi.ingsw.controller.userCommands;
    exports it.polimi.ingsw.Gui;

    opens it.polimi.ingsw.Gui to javafx.fxml, javafx.graphics;

}