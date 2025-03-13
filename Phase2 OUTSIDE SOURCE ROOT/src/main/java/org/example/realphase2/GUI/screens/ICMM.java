package org.example.realphase2.GUI.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.realphase2.Gamemodelogic.EdgeScrambler;
import org.example.realphase2.Gamemodelogic.FlagClass;
import org.example.realphase2.Generation.ColEdge;
import org.example.realphase2.Generation.GraphDisplayManager;
import org.example.realphase2.GUI.components.graphControls;
import org.example.realphase2.GUI.uicontroller.GameScreen;
import org.example.realphase2.GUI.uicontroller.ScreenManager;
import org.example.realphase2.Gamemodelogic.MainGameLogic;
import org.example.realphase2.Gamemodelogic.MainGameLogic;

import java.util.ArrayList;
import java.util.List;

import static org.example.realphase2.GUI.components.graphControls.showAlert;

// The ICMM class implements the GameScreen interface and represents the "I Changed My Mind" game screen.
public class ICMM implements GameScreen {
    private Scene scene; // The scene representing this screen.
    private ScreenManager screenManager; // Screen manager for navigating between screens.
    private GraphDisplayManager graphDisplayManager; // Manages graph display functionality.
    private Pane graphPane; // Pane where the graph is rendered.
    private graphControls graphControls; // UI component for graph controls.
    private MainGameLogic gameLogic;

    // Constructor initializes the screen with a ScreenManager and sets up necessary components.
    public ICMM(ScreenManager screenManager) {
        this.screenManager = screenManager; // Save the screen manager reference.
        this.gameLogic = new MainGameLogic();
        this.graphDisplayManager = new GraphDisplayManager(gameLogic);
        this.graphControls = new graphControls(this::displayGraph); // Set up graph controls with a callback.
        initialize(); // Set up the layout and UI.
    }

    // Initializes the screen layout and UI components.
    @Override
    public void initialize() {
        BorderPane root = new BorderPane(); // Root layout for the screen.
        root.setStyle("-fx-background-color: #f0f5ff;"); // Set a light blue background.

        // --- Top Section ---
        VBox topSection = new VBox(10); // Vertical layout for the top section.
        topSection.setAlignment(Pos.CENTER); // Center the content.
        topSection.setPadding(new Insets(20)); // Add padding around the content.
        topSection.setStyle("-fx-background-color: #3c4b64;"); // Dark blue background.

        // Title label configuration.
        Label title = new Label("I Changed My Mind");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32)); // Set title font and size.
        title.setStyle("-fx-text-fill: white;"); // White text color.

        // Timer label configuration.
        Label timer = new Label("00:00");
        timer.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Set timer font and size.
        timer.setStyle("-fx-text-fill: #ffd700;"); // Gold text color.
        timer.textProperty().bind(FlagClass.timer);

        // Add title and timer to the top section.
        topSection.getChildren().addAll(title, timer);
        root.setTop(topSection); // Place the top section at the top of the BorderPane.

        // --- Left Section ---
        VBox leftSection = new VBox(15); // Vertical layout for the left section.
        leftSection.setAlignment(Pos.TOP_CENTER); // Align content at the top and center.
        leftSection.setPadding(new Insets(20)); // Add padding around the content.
        leftSection.setPrefWidth(300); // Set fixed width for the left section.
        leftSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Label for controls section.
        Label controlsTitle = new Label("Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Set font and size for the label.

        // Add graph controls component with padding.
        graphControls = new graphControls(this::displayGraph);
        graphControls.setPadding(new Insets(10)); // Add padding to the graph controls.

        // Back button configuration.
        Button backButton = new Button("Back to Menu");
        backButton.setPrefWidth(200); // Set button width.
        backButton.setStyle(
            "-fx-background-color: #3c4b64;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;"
        );
        backButton.setOnAction(e -> screenManager.showScreen("mainMenu")); // Navigate to the main menu.

        // Add components to the left section.
        leftSection.getChildren().addAll(controlsTitle, graphControls, backButton);
        root.setLeft(leftSection); // Place the left section on the left side of the BorderPane.

        // --- Center Section ---
        StackPane centerSection = createCenterSection(); // Create the center section with the graph display.
        root.setCenter(centerSection); // Place the center section in the middle of the BorderPane.

        // --- Right Section ---
        VBox rightSection = new VBox(15); // Vertical layout for the right section.
        rightSection.setAlignment(Pos.TOP_CENTER); // Align content at the top and center.
        rightSection.setPadding(new Insets(20)); // Add padding around the content.
        rightSection.setPrefWidth(300); // Set fixed width for the right section.
        rightSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Score title label.
        Label scoreTitle = new Label("Score");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Set font and size for the label.

        // Score value label.
        Label scoreValue = new Label("100"); // Placeholder for the score value.
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 32)); // Set font and size for the label.
        scoreValue.setStyle("-fx-text-fill: #4CAF50;"); // Green text color.
        scoreValue.textProperty().bind(FlagClass.score);

        // Hint button configuration.
        Button hintButton = new Button("Get Hint");
        hintButton.setPrefWidth(200); // Set button width.
        hintButton.setStyle(
            "-fx-background-color: #3c4b64;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;"
        );
        hintButton.setOnAction(e -> handleGetHintButtonClick());

        // Add components to the right section.
        rightSection.getChildren().addAll(scoreTitle, scoreValue, hintButton);
        root.setRight(rightSection); // Place the right section on the right side of the BorderPane.

        // --- Bottom Section ---
        HBox bottomSection = new HBox(10); // Horizontal layout for the bottom section.
        bottomSection.setAlignment(Pos.CENTER); // Center the content.
        bottomSection.setPadding(new Insets(10)); // Add padding around the content.
        bottomSection.setStyle("-fx-background-color: #3c4b64;"); // Dark blue background.

        // Status label configuration.
        Label statusLabel = new Label("Select a vertex to begin coloring");
        statusLabel.setStyle("-fx-text-fill: white;"); // White text color.

        // Add the status label to the bottom section.
        bottomSection.getChildren().add(statusLabel);
        root.setBottom(bottomSection); // Place the bottom section at the bottom of the BorderPane.

        // Create the scene and set its size.
        scene = new Scene(root, 1440, 1080);
    }

    // Callback function to display the graph.
    private void displayGraph(List<ColEdge> edges) {
        List<ColEdge> edgesScrambled;
        // Determine the number of vertices from the graph controls or loaded file.
        int vertices = graphControls.isFileLoaded
            ? graphControls.getFileVertexCount()
            : graphControls.getNumberOfVertices();

        // Check if the vertex count is valid.
        if (vertices > 0) {
            edgesScrambled = EdgeScrambler.scrambleEdges(edges, vertices);
            graphDisplayManager.displayGraph(graphPane, edgesScrambled, vertices, 3); // Render the graph.
        } else {
            showAlert("Input Error", "Please enter a valid number of vertices."); // Show an error message.
        }
    }

    // Empty method for showing the screen.
    @Override
    public void show() {}

    // Empty method for hiding the screen.
    @Override
    public void hide() {}

    // Returns the Scene object for this screen.
    @Override
    public Scene getScene() {
        return scene;
    }

    // Creates the center section where the graph is displayed.
    private StackPane createCenterSection() {
        graphPane = new Pane(); // Pane for displaying the graph.
        graphPane.setPrefSize(800, 600); // Set preferred size for the graph pane.

        // Initialize the graph display manager.
        graphDisplayManager = new GraphDisplayManager(gameLogic);

        // Wrap the graph pane in a StackPane for styling and alignment.
        StackPane centeredGraphPane = new StackPane(graphPane);
        centeredGraphPane.setAlignment(Pos.CENTER); // Center the graph pane.
        centeredGraphPane.setPadding(new Insets(20)); // Add padding around the graph pane.
        centeredGraphPane.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #3c4b64;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);"
        );

        return centeredGraphPane; // Return the styled StackPane.
    }

    private void handleGetHintButtonClick() {
        String hint = gameLogic.getSelectedGameModeICMM().getHint();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(hint);
        alert.showAndWait();
    }
}