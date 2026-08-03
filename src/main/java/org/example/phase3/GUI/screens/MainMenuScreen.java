package org.example.phase3.GUI.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.phase3.GUI.uicontroller.GameScreen;
import org.example.phase3.GUI.uicontroller.ScreenManager;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

public class MainMenuScreen extends GameScreen {
    private Scene scene;
    private VBox root;
    private ScreenManager screenManager;

    public MainMenuScreen(ScreenManager manager) {
        this.screenManager = manager;
        initialize();
    }

    @Override
    public void initialize() {
        root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Title
        Label title = new Label("Graph Coloring Visualizer");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(8, Color.web("#3498db")));

        // Buttons Container
        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        // Menu Buttons
        Button startButton = createMenuButton("Start New Graph");
        Button exitButton = createMenuButton("Exit");
        
        buttonContainer.getChildren().addAll(startButton, exitButton);
        root.getChildren().addAll(title, buttonContainer);

        // Setup button actions
        startButton.setOnAction(e -> screenManager.showScreen("GraphDisplayer"));
        exitButton.setOnAction(e -> System.exit(0));

        scene = new Scene(root, 800, 600);
    }

    @Override
    public void show() {
        // Empty implementation
    }

    @Override
    public void hide() {
        // Empty implementation
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-min-width: 250px;" +
            "-fx-min-height: 50px;" +
            "-fx-background-radius: 25px;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10px 20px;" +
            "-fx-effect: dropshadow(gaussian, #2980b9, 4, 0, 0, 0);"
        );

        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: #2980b9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-min-width: 250px;" +
                "-fx-min-height: 50px;" +
                "-fx-background-radius: 25px;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10px 20px;" +
                "-fx-effect: dropshadow(gaussian, #2980b9, 4, 0, 0, 0);" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;"
            )
        );

        button.setOnMouseExited(e ->
            button.setStyle(
                "-fx-background-color: #3498db;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-min-width: 250px;" +
                "-fx-min-height: 50px;" +
                "-fx-background-radius: 25px;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10px 20px;" +
                "-fx-effect: dropshadow(gaussian, #2980b9, 4, 0, 0, 0);"
            )
        );

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}