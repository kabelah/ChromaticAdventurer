module org.example.realphase2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires jdk.compiler;

    exports org.example.realphase2.Generation; // This allows the package to be accessible for JavaFX to launch MainGUI
    exports org.example.realphase2.GUI.screens;
    exports org.example.realphase2.GUI.uicontroller;
    exports org.example.realphase2.Run;

}