module org.example.phase3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires jdk.compiler;


    //opens org.example.phase3 to javafx.fxml;
    exports org.example.phase3.Generation;
    exports org.example.phase3.GUI.components;
    exports org.example.phase3.GUI.screens;
    exports org.example.phase3.GUI.uicontroller;
    exports org.example.phase3.Run;
}