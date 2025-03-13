package org.example.realphase2.Run;

import org.example.realphase2.GUI.screens.ICMM;
import org.example.realphase2.GUI.screens.MainMenuScreen;
import org.example.realphase2.GUI.screens.RandomOrder;
import org.example.realphase2.GUI.screens.TTBE;
import org.example.realphase2.GUI.uicontroller.ScreenManager;

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

        // Create instances of screens and add them to the ScreenManager
        MainMenuScreen mainMenu = new MainMenuScreen(screenManager);
        TTBE ttbeScreen = new TTBE(screenManager);
        RandomOrder randomOrderScreen = new RandomOrder(screenManager);
        ICMM icmmScreen = new ICMM(screenManager);

        screenManager.addScreen("mainMenu", mainMenu);
        screenManager.addScreen("TTBE", ttbeScreen);
        screenManager.addScreen("randomOrder", randomOrderScreen);
        screenManager.addScreen("ICMM", icmmScreen);

        screenManager.showScreen("mainMenu");
        primaryStage.setTitle("Chromatic Adventurer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}