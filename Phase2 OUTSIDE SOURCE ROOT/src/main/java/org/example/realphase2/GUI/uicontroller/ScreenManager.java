package org.example.realphase2.GUI.uicontroller;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * The ScreenManager class manages multiple screens in a JavaFX application.
 * It allows for smooth transitions between different screens by storing and
 * handling their lifecycle, such as initialization, display, and hiding.
 */
public class ScreenManager {

    private final Stage primaryStage; // The main stage (window) of the application.
    private final Map<String, GameScreen> screens = new HashMap<>(); // A map to store screen names and their corresponding GameScreen objects.
    private GameScreen currentScreen; // The screen that is currently being displayed.

    /**
     * Constructs a new `ScreenManager` for managing screens within the given stage.
     *
     * @param stage The main stage of the application.
     */
    public ScreenManager(Stage stage) {
        this.primaryStage = stage; // Store the reference to the main application stage.
    }

    /**
     * Adds a screen to the manager.
     * 
     * @param name   The unique name for the screen (used to identify it later).
     * @param screen The GameScreen object representing the screen.
     */
    public void addScreen(String name, GameScreen screen) {
        screens.put(name, screen); // Add the screen to the map with its name as the key.
        screen.initialize(); // Initialize the screen (set up its UI components).
    }

    /**
     * Displays a screen by its name.
     * 
     * @param name The name of the screen to show.
     */
    public void showScreen(String name) {
        // Hide the current screen if one is already displayed.
        if (currentScreen != null) {
            currentScreen.hide(); // Call the hide method of the current screen.
        }

        // Retrieve the new screen by its name.
        currentScreen = screens.get(name);

        if (currentScreen != null) {
            // Show the new screen and set it as the active scene in the primary stage.
            currentScreen.show(); // Call the show method of the new screen.
            primaryStage.setScene(currentScreen.getScene()); // Set the scene of the stage to the new screen's scene.
        } else {
            // Print an error message if the screen with the given name does not exist.
            System.out.println("Screen " + name + " does not exist");
        }
    }

    /**
     * Creates a styled "Back" button for navigating back to the main menu.
     * 
     * @return A Button object styled and configured to navigate to the main menu.
     */
    public Button createBackButton() {
        Button backButton = new Button("Back"); // Create a new button with the label "Back".

        // Set the action to navigate to the "mainMenu" screen when clicked.
        backButton.setOnAction(e -> showScreen("mainMenu"));

        // Style the button with CSS for appearance.
        backButton.setStyle(
            "-fx-background-color: #3c4b64;" + // Dark blue background color.
            "-fx-text-fill: white;" + // White text color.
            "-fx-background-radius: 10;" + // Rounded corners.
            "-fx-padding: 10 20;" // Padding around the text.
        );

        return backButton; // Return the styled and configured button.
    }
}