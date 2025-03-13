package org.example.phase3.GUI.screens;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.phase3.GUI.uicontroller.GameScreen;
import org.example.phase3.GUI.uicontroller.ScreenManager;
import javafx.scene.effect.DropShadow;

public class LoadingScreen extends GameScreen {
    private ScreenManager screenManager;
    
    public LoadingScreen(ScreenManager manager) {
        this.screenManager = manager;
        initialize();
    }

    @Override
    public void initialize() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #2d3436;");

        // Create loading bar container
        Rectangle loadingBarContainer = new Rectangle(300, 5);
        loadingBarContainer.setArcWidth(10);
        loadingBarContainer.setArcHeight(10);
        loadingBarContainer.setFill(Color.web("#636e72"));

        // Create animated loading bar
        Rectangle loadingBar = new Rectangle(0, 5);
        loadingBar.setArcWidth(10);
        loadingBar.setArcHeight(10);
        loadingBar.setFill(Color.web("#81ecec"));

        // Animate loading bar width
        Timeline loadingAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.widthProperty(), 0)),
            new KeyFrame(Duration.seconds(2), new KeyValue(loadingBar.widthProperty(), 300, Interpolator.EASE_BOTH))
        );
        loadingAnimation.play();

        // Create bouncing dots
        HBox dots = new HBox(10);
        dots.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            Circle dot = new Circle(5, Color.web("#81ecec"));
            dots.getChildren().add(dot);

            // Create bouncing animation for each dot
            TranslateTransition bounce = new TranslateTransition(Duration.seconds(0.5), dot);
            bounce.setByY(15);
            bounce.setCycleCount(Timeline.INDEFINITE);
            bounce.setAutoReverse(true);
            bounce.setDelay(Duration.seconds(i * 0.15));
            bounce.play();
        }

        // Loading text with fade animation
        Text loadingText = new Text("LOADING");
        loadingText.setFill(Color.web("#dfe6e9"));
        loadingText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        loadingText.setEffect(new DropShadow(15, Color.web("#81ecec", 0.2)));

        FadeTransition textFade = new FadeTransition(Duration.seconds(1.5), loadingText);
        textFade.setFromValue(0.4);
        textFade.setToValue(1.0);
        textFade.setCycleCount(Animation.INDEFINITE);
        textFade.setAutoReverse(true);
        textFade.play();

        // Create particle effect
        for (int i = 0; i < 20; i++) {
            Circle particle = new Circle(2, Color.web("#81ecec", 0.4));
            root.getChildren().add(particle);

            // Random starting position
            particle.setTranslateX(Math.random() * 800 - 400);
            particle.setTranslateY(Math.random() * 600 - 300);

            // Create floating animation
            Timeline floatAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(particle.translateXProperty(), particle.getTranslateX()),
                    new KeyValue(particle.translateYProperty(), particle.getTranslateY())
                ),
                new KeyFrame(Duration.seconds(3 + Math.random() * 2),
                    new KeyValue(particle.translateXProperty(), particle.getTranslateX() + (Math.random() * 100 - 50)),
                    new KeyValue(particle.translateYProperty(), particle.getTranslateY() + (Math.random() * 100 - 50))
                )
            );
            floatAnimation.setCycleCount(Timeline.INDEFINITE);
            floatAnimation.setAutoReverse(true);
            floatAnimation.play();
        }

        // Arrange elements
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        StackPane barContainer = new StackPane();
        barContainer.getChildren().addAll(loadingBarContainer, loadingBar);
        
        content.getChildren().addAll(loadingText, barContainer, dots);
        root.getChildren().add(content);

        scene = new Scene(root, 800, 600);
    }

    @Override
    public void show() {
        // Nothing special needed for show
    }

    @Override
    public void hide() {
        // Nothing special needed for hide
    }
}