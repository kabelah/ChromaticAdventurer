package org.example.realphase2.GUI.screens;

import org.example.realphase2.GUI.uicontroller.GameScreen;
import org.example.realphase2.GUI.uicontroller.ScreenManager;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The MainMenuScreen class represents the main menu of the Chromatic Adventurer application.
 * It provides options to navigate to different game modes or quit the application.
 * The menu features a background image, title, and navigation buttons.
 */
public class MainMenuScreen implements GameScreen {
    private Scene scene;
    private ScreenManager screenManager;

    /**
     * Constructs the MainMenuScreen with the specified ScreenManager.
     *
     * @param screenManager The ScreenManager that handles screen transitions.
     */
    public MainMenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        initialize();
    }

    /**
     * Initializes the main menu layout, including the background image,
     * title, and navigation buttons for game modes and quitting the application.
     */
    @Override
    public void initialize() {
        // Create the root layout with StackPane for background image
        StackPane root = new StackPane();

        // Load and set the background image
        ImageView background = new ImageView(new Image("file:src/main/resources/images/background.jpg"));
        background.setFitWidth(1440); // Full HD width
        background.setFitHeight(1080); // Full HD height

        // Title layout (VBox at the top)
        Label title = new Label("Chromatic Adventurer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48)); // Larger font for 1080p
        title.setStyle("-fx-text-fill: #3c4b64;"); // Color of the title text

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setTranslateY(50); // Adjust to move title down from the top if needed

        // Button layout (VBox at the center)
        VBox menuLayout = new VBox(15); // Spacing between buttons
        menuLayout.setAlignment(Pos.CENTER);

        // Create buttons with a uniform size
        Button toTheBitterEnd = new Button("To the bitter end");
        Button randomOrder = new Button("Random order");
        Button changedMind = new Button("I changed my mind");
        Button quit = new Button("Quit");

        // Set uniform button size
        double buttonWidth = 300;  // Adjust the width as needed
        double buttonHeight = 60;  // Adjust the height as needed
        setButtonSize(toTheBitterEnd, buttonWidth, buttonHeight);
        setButtonSize(randomOrder, buttonWidth, buttonHeight);
        setButtonSize(changedMind, buttonWidth, buttonHeight);
        setButtonSize(quit, buttonWidth, buttonHeight);

        // Button actions
        toTheBitterEnd.setOnAction(e -> screenManager.showScreen("TTBE"));
        quit.setOnAction(e -> System.exit(0));

        randomOrder.setOnAction(e -> screenManager.showScreen("randomOrder"));

        changedMind.setOnAction(e -> screenManager.showScreen("ICMM"));

        // Add buttons to the menu layout
        menuLayout.getChildren().addAll(toTheBitterEnd, randomOrder, changedMind, quit);

        // Add VBox layouts to the root StackPane
        root.getChildren().addAll(background, titleBox, menuLayout);

        // Create the scene with 1440x1080 resolution
        scene = new Scene(root, 1440, 1080);
    }

    
    /**
     * Helper method to set the size and style of buttons.
     *
     * @param button The button to be styled.
     * @param width  The width of the button.
     * @param height The height of the button.
     */
    private void setButtonSize(Button button, double width, double height) {
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Larger font for 1080p
        button.setStyle(
                "-fx-background-color: #3c4b64; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;"
        );
    }

    @Override
    public void show() {
        // Any actions to perform when this screen is shown
    }

    @Override
    public void hide() {
        // Any cleanup actions when this screen is hidden
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}