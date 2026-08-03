package org.example.phase3.GUI.uicontroller;

import javafx.scene.Scene;

public abstract class GameScreen {
    protected Scene scene;
    
    public abstract void initialize();
    public abstract void show();
    public abstract void hide();
    
    public Scene getScene() {
        return scene;
    }
}
