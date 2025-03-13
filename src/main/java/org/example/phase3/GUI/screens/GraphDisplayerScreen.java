package org.example.phase3.GUI.screens;

import java.util.List;

import org.example.phase3.GUI.components.graphControls;
import static org.example.phase3.GUI.components.graphControls.showAlert;
import org.example.phase3.GUI.uicontroller.GameScreen;
import org.example.phase3.GUI.uicontroller.ScreenManager;
import org.example.phase3.Generation.ChromaticNumberSolver;
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Generation.GraphDisplayManager;
import org.example.phase3.SpecialGraphCases.PlanarGraphSolver;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GraphDisplayerScreen extends GameScreen {
    // These fields store the main components of the game screen
    private Scene scene;
    private ScreenManager screenManager;
    private GraphDisplayManager graphDisplayManager;
    private Pane graphPane;
    private graphControls controls;
    public static Button colorGraph;

    public GraphDisplayerScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.graphDisplayManager = new GraphDisplayManager();
        this.graphPane = new Pane();
        colorGraph = new Button("Color Graph");
        colorGraph.setVisible(false);
        this.controls = new graphControls(edges -> {
            if (controls.isFileLoaded) {
                graphDisplayManager.displayGraph(graphPane, edges, controls.getFileVertexCount(), 2);
            } else {
                graphDisplayManager.displayGraph(graphPane, edges, controls.getNumberOfVertices(), 1);
            }
        });
        initialize();
    }

    @Override
    public void initialize() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Create left control panel
        VBox controlPanel = new VBox(15);
        controlPanel.setStyle(
                "-fx-background-color: #2c3e50;" +
                        "-fx-padding: 20px;" +
                        "-fx-spacing: 10px;" +
                        "-fx-effect: dropshadow(gaussian, #1a1a1a, 10, 0, 0, 0);"
        );
        controlPanel.setPadding(new Insets(20));
        controlPanel.setMinWidth(250);
        controlPanel.setAlignment(Pos.TOP_CENTER);

        // Add zoom functionality directly to graphPane
        graphPane.setOnScroll(event -> {
            if (event.isShiftDown()) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }

                graphPane.setScaleX(graphPane.getScaleX() * zoomFactor);
                graphPane.setScaleY(graphPane.getScaleY() * zoomFactor);

                event.consume();
            }
        });

        // Add back button
        Button backButton = screenManager.createBackButton();
        setButtonSize(backButton, 200, 50);
        backButton.setStyle(
                "-fx-background-color: #2c3e50;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-cursor: hand;"
        );
        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-background-color: #34495e;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );
        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-background-color: #2980b9;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );

        // Initialize color graph button
        colorGraph = new Button("Color Graph");
        setButtonSize(colorGraph, 200, 50);
        colorGraph.setVisible(false);
        colorGraph.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        colorGraph.setOnMouseEntered(e ->
                colorGraph.setStyle(
                        "-fx-background-color: #2980b9;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-padding: 10 20;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );
        colorGraph.setOnMouseExited(e ->
                colorGraph.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-padding: 10 20;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );
        // Algorithms that find the chromatic number need to be started by pressing this button.
        // Please go to ChromaticNumberSolver.java. Do not edit this button unless you know 100%
        // what you are changing.
        colorGraph.setOnAction(e -> ChromaticNumberSolver.solveChromaticNumber(controls.getCurrentEdges()));

        // Make all controls in graphControls the same width
        controls.setPrefWidth(200);
        controls.setAlignment(Pos.CENTER);

        // Apply styles to controls
        controls.getChildren().forEach(child -> {
            if (child instanceof Label) {
                child.setStyle(
                        "-fx-text-fill: white;" +
                                "-fx-font-family: 'Segoe UI';"
                );
            } else if (child instanceof TextField) {
                child.setStyle(
                        "-fx-background-color: #34495e;" +
                                "-fx-text-fill: white;" +
                                "-fx-prompt-text-fill: #b2bec3;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 10 15;"
                );
            } else if (child instanceof Button) {
                setButtonSize((Button) child, 200, 50);
                child.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-padding: 10 20;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                );
                child.setOnMouseEntered(e ->
                        child.setStyle(
                                "-fx-background-color: #2980b9;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-padding: 10 20;" +
                                        "-fx-background-radius: 5;" +
                                        "-fx-cursor: hand;"
                        )
                );
                child.setOnMouseExited(e ->
                        child.setStyle(
                                "-fx-background-color: #3498db;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-padding: 10 20;" +
                                        "-fx-background-radius: 5;" +
                                        "-fx-cursor: hand;"
                        )
                );
            }
        });

        // Add components to control panel with consistent spacing
        controlPanel.getChildren().addAll(backButton, controls, colorGraph);
        VBox.setMargin(backButton, new Insets(0, 0, 20, 0));
        VBox.setMargin(colorGraph, new Insets(20, 0, 0, 0));

        root.setLeft(controlPanel);
        root.setCenter(graphPane);

        scene = new Scene(root, 1600, 900);
    }

    // This function creates the top section containing the title and timer
    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
        topSection.setStyle("-fx-background-color: #ffffff;");

        Label title = new Label("Graph Displayer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #333333;");

        topSection.getChildren().addAll(title);
        return topSection;
    }

    // This function creates the left section containing the controls
    private VBox createLeftSection() {
        VBox leftSection = new VBox(15);
        leftSection.setAlignment(Pos.TOP_CENTER);
        leftSection.setPadding(new Insets(20));
        leftSection.setPrefWidth(300);
        leftSection.setStyle("-fx-background-color: #ffffff; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label controlsTitle = new Label("Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        controlsTitle.setStyle("-fx-text-fill: #333333;");

        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        Button backButton = new Button("Back to Menu");
        backButton.setPrefWidth(200);
        backButton.setStyle(
                "-fx-background-color: #2980b9" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 5;"
        );

        // Add hover effect
        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-font-weight: bold;" +
                                "-fx-padding: 10;" +
                                "-fx-background-radius: 5;"
                )
        );
        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-font-weight: bold;" +
                                "-fx-padding: 10;" +
                                "-fx-background-radius: 5;"
                )
        );

        backButton.setOnAction(e -> screenManager.showScreen("mainMenu"));

        leftSection.getChildren().addAll(controlsTitle, controls, backButton);
        return leftSection;
    }

    // This function creates the center section containing the graph display
    private StackPane createCenterSection() {
        graphPane = new Pane();
        graphPane.setPrefSize(800, 600);
        graphDisplayManager = new GraphDisplayManager();

        StackPane centeredGraphPane = new StackPane(graphPane);
        centeredGraphPane.setAlignment(Pos.CENTER);
        centeredGraphPane.setPadding(new Insets(20));
        centeredGraphPane.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);"
        );

        return centeredGraphPane;
    }

    // This function creates the right section containing the score and hints
    private VBox createRightSection() {
        VBox rightSection = new VBox(15);
        rightSection.setAlignment(Pos.TOP_CENTER);
        rightSection.setPadding(new Insets(20));
        rightSection.setPrefWidth(300);
        rightSection.setStyle("-fx-background-color: #ffffff; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        colorGraph = new Button("Color in Graph");
        double buttonWidth = 300;
        double buttonHeight = 60;
        setButtonSize(colorGraph, buttonWidth, buttonHeight);

        rightSection.getChildren().addAll(colorGraph);
        colorGraph.setVisible(false);

        // Set up the color graph button action
        colorGraph.setOnAction(e -> {
            if (controls.getCurrentEdges() != null && !controls.getCurrentEdges().isEmpty()) {
                ChromaticNumberSolver.solveChromaticNumber(controls.getCurrentEdges());
            } else {
                showAlert("Error", "No graph to color. Please generate or load a graph first.");
            }
        });

        return rightSection;
    }

    // This function creates the bottom section containing the status messages
    private HBox createBottomSection() {
        HBox bottomSection = new HBox(10);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(10));
        bottomSection.setStyle("-fx-background-color: #ffffff;");

        //bottomSection.getChildren().add();
        return bottomSection;
    }

    // This function creates a styled button with the given text and style class
//    private Button createStyledButton(String text, String styleClass) {
//        Button button = new Button(text);
//        button.getStyleClass().add(styleClass);
//        return button;
//    }

    // This function handles the display of the graph
    // It uses either loaded file data or user input to determine vertex count
    private void displayGraph(List<ColEdge> edges) {
        if (controls.isFileLoaded) {
            graphDisplayManager.displayGraph(graphPane, edges, controls.getFileVertexCount(), 2);
        } else {
            int vertices = controls.getNumberOfVertices();
            if (vertices > 0) {
                graphDisplayManager.displayGraph(graphPane, edges, vertices, 1);
            } else {
                showAlert("Input Error", "Please enter a valid number of vertices.");
            }
        }
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
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public Scene getScene() {
        return scene;
    }
}