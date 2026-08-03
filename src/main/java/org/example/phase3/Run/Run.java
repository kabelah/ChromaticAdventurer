package org.example.phase3.Run;

import org.example.phase3.GUI.screens.GraphDisplayerScreen;
import org.example.phase3.GUI.screens.LoadingScreen;
import org.example.phase3.GUI.screens.MainMenuScreen;
import org.example.phase3.GUI.uicontroller.ScreenManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Run class serves as the entry point for the JavaFX application.
 * It sets up the application by initializing screens and managing their transitions
 * through the ScreenManager class.
 */
public class Run extends Application {

    /**
     * This method is the main entry point for the JavaFX application.
     * It initializes the ScreenManager and sets up the primary stage with the main menu screen.
     *
     * @param primaryStage The main window for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        ScreenManager screenManager = new ScreenManager(primaryStage);
        
        LoadingScreen loadingScreen = new LoadingScreen(screenManager);
        MainMenuScreen mainMenu = new MainMenuScreen(screenManager);
        GraphDisplayerScreen GraphDisplayer = new GraphDisplayerScreen(screenManager);

        screenManager.addScreen("loading", loadingScreen);
        screenManager.addScreen("mainMenu", mainMenu);
        screenManager.addScreen("GraphDisplayer", GraphDisplayer);

        primaryStage.setTitle("Graph Coloring Visualizer");
        screenManager.showScreen("loading");
        primaryStage.show();

        // Transition to main menu after delay
        new Thread(() -> {
            try {
                Thread.sleep(2500);
                javafx.application.Platform.runLater(() -> 
                    screenManager.showScreen("mainMenu")
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}