module PSP13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires java.rmi;

    exports it.polimi.ingsw.controller.userCommands;
    exports it.polimi.ingsw.Gui;

    opens it.polimi.ingsw.controller.userCommands to javafx.fxml, javafx.graphics;

}