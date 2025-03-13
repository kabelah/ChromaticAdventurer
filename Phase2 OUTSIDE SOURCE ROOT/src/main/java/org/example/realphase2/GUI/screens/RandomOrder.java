package org.example.realphase2.GUI.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

import static org.example.realphase2.GUI.components.graphControls.showAlert;

// The RandomOrder class implements the GameScreen interface and represents a specific screen
// in the application's GUI where the graph randomization gameplay occurs.
public class RandomOrder implements GameScreen {
    private Scene scene; // The JavaFX Scene object representing the screen.
    private ScreenManager screenManager; // A reference to the ScreenManager to manage screen transitions.
    private GraphDisplayManager graphDisplayManager; // Responsible for displaying and managing the graph UI.
    private Pane graphPane; // The pane where the graph is rendered.
    private graphControls graphControls; // UI component for graph-related controls.
    private MainGameLogic gameLogic;

    // Constructor to initialize the RandomOrder screen. It accepts a ScreenManager
    // to allow navigation between screens.
    public RandomOrder(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.gameLogic = new MainGameLogic();
        this.graphDisplayManager = new GraphDisplayManager(gameLogic);
        this.graphControls = new graphControls(this::displayGraph);
        initialize();
    }

    // Initializes the layout, UI components, and styles for the screen.
    @Override
    public void initialize() {
        // Create a BorderPane as the root layout for this screen.
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5ff;"); // Set a light blue background.

        // Top section of the screen (header) with title and timer.
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
        topSection.setStyle("-fx-background-color: #3c4b64;"); // Set dark blue background for the header.

        // Title label configuration.
        Label title = new Label("Random Order");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32)); // Set title font size and style.
        title.setStyle("-fx-text-fill: white;"); // Set text color to white.

        // Timer label configuration.
        Label timer = new Label("00:00");
        timer.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Set timer font size and style.
        timer.setStyle("-fx-text-fill: #ffd700;"); // Set text color to gold.
        timer.textProperty().bind(FlagClass.timer);

        // Add the title and timer to the top section.
        topSection.getChildren().addAll(title, timer);
        root.setTop(topSection); // Add the top section to the root layout.

        // Left section for controls.
        VBox leftSection = new VBox(15);
        leftSection.setAlignment(Pos.TOP_CENTER);
        leftSection.setPadding(new Insets(20));
        leftSection.setPrefWidth(300); // Set a fixed width for the controls section.
        leftSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Add a "Controls" label to the left section.
        Label controlsTitle = new Label("Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Initialize graph controls with padding for spacing.
        graphControls = new graphControls(this::displayGraph);
        graphControls.setPadding(new Insets(10));

        // Create a "Back to Menu" button with styling and action to return to the main menu.
        Button backButton = new Button("Back to Menu");
        backButton.setPrefWidth(200);
        backButton.setStyle(
            "-fx-background-color: #3c4b64;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;"
        );
        backButton.setOnAction(e -> screenManager.showScreen("mainMenu")); // Navigate to the main menu.

        // Add the controls label, graph controls, and back button to the left section.
        leftSection.getChildren().addAll(controlsTitle, graphControls, backButton);
        root.setLeft(leftSection); // Add the left section to the root layout.

        // Create the center section where the graph is displayed.
        StackPane centerSection = createCenterSection();
        root.setCenter(centerSection); // Add the center section to the root layout.
        
        // Right section for displaying score and other options.
        VBox rightSection = new VBox(15);
        rightSection.setAlignment(Pos.TOP_CENTER);
        rightSection.setPadding(new Insets(20));
        rightSection.setPrefWidth(300); // Set a fixed width for the score section.
        rightSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Add a "Score" label and score value to the right section.
        Label scoreTitle = new Label("Score");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label scoreValue = new Label("100"); // Placeholder score value.
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        scoreValue.setStyle("-fx-text-fill: #4CAF50;"); // Set text color to green.
        scoreValue.textProperty().bind(FlagClass.score);

        // Create a "Get Hint" button with styling.
        Button hintButton = new Button("Get Hint");
        hintButton.setPrefWidth(200);
        hintButton.setStyle(
            "-fx-background-color: #3c4b64;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;"
        );
        hintButton.setOnAction(e -> handleGetHintButtonClick());

        // Add the score title, value, and hint button to the right section.
        rightSection.getChildren().addAll(scoreTitle, scoreValue, hintButton);
        root.setRight(rightSection); // Add the right section to the root layout.

        // Bottom section for displaying the status label.
        HBox bottomSection = new HBox(10);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(10));
        bottomSection.setStyle("-fx-background-color: #3c4b64;");

        // Add a status label to the bottom section.
        Label statusLabel = new Label("Select a vertex to begin coloring");
        statusLabel.setStyle("-fx-text-fill: white;"); // Set text color to white.
        bottomSection.getChildren().add(statusLabel);
        root.setBottom(bottomSection); // Add the bottom section to the root layout.

        // Create the scene with the root layout and set its dimensions.
        scene = new Scene(root, 1440, 1080);
    }

    // Creates the center section where the graph is rendered.
    private StackPane createCenterSection() {
        graphPane = new Pane(); // Pane for rendering the graph.
        graphPane.setPrefSize(800, 600); // Set preferred size for the graph pane.

        // Wrap the graph pane in a StackPane for alignment and styling.
        StackPane centeredGraphPane = new StackPane(graphPane);
        centeredGraphPane.setAlignment(Pos.CENTER);
        centeredGraphPane.setPadding(new Insets(20));
        centeredGraphPane.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #3c4b64;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);"
        );

        return centeredGraphPane;
    }

    // Displays the graph based on a list of edges.
    private void displayGraph(List<ColEdge> edges) {
        List<ColEdge> edgesScrambled;
        // Determine the number of vertices based on the controls state.
        int vertices = graphControls.isFileLoaded ? graphControls.getFileVertexCount() : graphControls.getNumberOfVertices();

        // Validate the number of vertices and display the graph if valid.
        if (vertices > 0) {
            edgesScrambled = EdgeScrambler.scrambleEdges(edges, vertices);
            graphDisplayManager.displayGraph(graphPane, edgesScrambled, vertices, 2); // Render the graph.
        } else {
            showAlert("Input Error", "Please enter a valid number of vertices."); // Show an error if invalid.
        }
    }

    @Override
    public void show() {
        // Method called when the screen is shown (no specific implementation needed here).
    }

    @Override
    public void hide() {
        // Method called when the screen is hidden (no specific implementation needed here).
    }

    @Override
    public Scene getScene() {
        return scene; // Return the scene associated with this screen.
    }

    private void handleGetHintButtonClick() {
       
        String hint = gameLogic.getSelectedGameModeRM().getHint();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(hint);
        alert.showAndWait();
    }
}