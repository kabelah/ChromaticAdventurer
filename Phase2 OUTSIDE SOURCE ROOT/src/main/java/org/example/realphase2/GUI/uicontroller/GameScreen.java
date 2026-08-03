package org.example.realphase2.GUI.uicontroller;

import javafx.scene.Scene;

/** 
 * This interface forces every single screen to implement the methods seen below.
 * These are simple methods but they are essential for being able to control what the user sees with certainty
 */
public interface GameScreen {
    void initialize();
    void show();
    void hide();
    Scene getScene();
}
