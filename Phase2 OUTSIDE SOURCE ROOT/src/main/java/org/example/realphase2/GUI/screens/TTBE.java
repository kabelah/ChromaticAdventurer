package org.example.realphase2.GUI.screens;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.realphase2.Gamemodelogic.FlagClass;
import org.example.realphase2.Generation.ColEdge;
import org.example.realphase2.Generation.GraphDisplayManager;
import org.example.realphase2.GUI.components.graphControls;
import org.example.realphase2.GUI.uicontroller.GameScreen;
import org.example.realphase2.GUI.uicontroller.ScreenManager;
import org.example.realphase2.Gamemodelogic.MainGameLogic;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Arrays;

import static org.example.realphase2.GUI.components.graphControls.showAlert;

public class TTBE implements GameScreen {
    // These fields store the main components of the game screen
    private Scene scene;
    private ScreenManager screenManager;
    private GraphDisplayManager graphDisplayManager;
    private Pane graphPane;
    private graphControls graphControls;
    private MainGameLogic gameLogic;

    public TTBE(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.gameLogic = new MainGameLogic();
        this.graphDisplayManager = new GraphDisplayManager(gameLogic);
        this.graphControls = new graphControls(this::displayGraph);
        initialize();
    }

    @Override
    public void initialize() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5ff;"); // Light blue background

        // Top section with title and timer
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Left section with controls
        VBox leftSection = createLeftSection();
        root.setLeft(leftSection);

        // Center section with graph
        StackPane centerSection = createCenterSection();
        root.setCenter(centerSection);

        // Right section with scoring and hints
        VBox rightSection = createRightSection();
        root.setRight(rightSection);

        // Bottom section with status and messages
        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        scene = new Scene(root, 1440, 1080);
    }

    // This function creates the top section containing the title and timer
    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
        topSection.setStyle("-fx-background-color: #3c4b64;");

        Label title = new Label("To The Bitter End");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: white;");



        Label timer = new Label();
        timer.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timer.setStyle("-fx-text-fill: #ffd700;"); // Gold color
        timer.textProperty().bind(FlagClass.timer);


        topSection.getChildren().addAll(title, timer);
        return topSection;
    }

    // This function creates the left section containing the controls
    private VBox createLeftSection() {
        VBox leftSection = new VBox(15);
        leftSection.setAlignment(Pos.TOP_CENTER);
        leftSection.setPadding(new Insets(20));
        leftSection.setPrefWidth(300);
        leftSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label controlsTitle = new Label("Controls");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        graphControls.setPadding(new Insets(10));
        graphControls.setStyle("-fx-font-size: 14px;");

        Button backButton = new Button("Back to Menu");
        backButton.setPrefWidth(200);
        backButton.setStyle(
            "-fx-background-color: #3c4b64;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;"
        );

        // Add hover effect
        backButton.setOnMouseEntered(e ->
            backButton.setStyle(
                "-fx-background-color: #2a3b54;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 5;"
            )
        );
        backButton.setOnMouseExited(e ->
            backButton.setStyle(
                "-fx-background-color: #3c4b64;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 5;"
            )
        );

        backButton.setOnAction(e -> screenManager.showScreen("mainMenu"));

        leftSection.getChildren().addAll(controlsTitle, graphControls, backButton);
        return leftSection;
    }

    // This function creates the center section containing the graph display
    private StackPane createCenterSection() {
        graphPane = new Pane();
        graphPane.setPrefSize(800, 600);
        graphDisplayManager = new GraphDisplayManager(gameLogic);

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

    // This function creates the right section containing the score and hints
    private VBox createRightSection() {
        VBox rightSection = new VBox(15);
        rightSection.setAlignment(Pos.TOP_CENTER);
        rightSection.setPadding(new Insets(20));
        rightSection.setPrefWidth(300);
        rightSection.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label scoreTitle = new Label("Score");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label timer = new Label();
        timer.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timer.setStyle("-fx-text-fill: #ffd700;"); // Gold color
        timer.textProperty().bind(FlagClass.timer);


        Label scoreValue = new Label();
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        scoreValue.setStyle("-fx-text-fill: #4CAF50;");
        scoreValue.textProperty().bind(FlagClass.score);

        Button hintButton = createHintButton();
        hintButton.setStyle("-fx-font-size: 14px;");

        rightSection.getChildren().addAll(scoreTitle, scoreValue, hintButton);
        return rightSection;
    }

    // This function creates the bottom section containing the status messages
    private HBox createBottomSection() {
        HBox bottomSection = new HBox(10);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(10));
        bottomSection.setStyle("-fx-background-color: #3c4b64;");

        Label statusLabel = new Label("Select a vertex to begin coloring");
        statusLabel.setStyle("-fx-text-fill: white;");

        bottomSection.getChildren().add(statusLabel);
        return bottomSection;
    }

    // This function creates a styled button with the given text and style class
    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    // This function handles the display of the graph
    // It uses either loaded file data or user input to determine vertex count
    private void displayGraph(List<ColEdge> edges) {
        if (graphControls.isFileLoaded) {
            graphDisplayManager.displayGraph(graphPane, edges, graphControls.getFileVertexCount(), 1);
        } else {
            int vertices = graphControls.getNumberOfVertices();
            if (vertices > 0) {
                graphDisplayManager.displayGraph(graphPane, edges, vertices, 1);
            } else {
                showAlert("Input Error", "Please enter a valid number of vertices.");
            }
        }
    }

    private Button createHintButton() {
        Button hintButton = createStyledButton("Get Hint", "hint-button");
        hintButton.setOnAction(e -> handleGetHintButtonClick());
        return hintButton;
    }

    private void handleGetHintButtonClick() {
        String hint = gameLogic.getSelectedGameModeTTBE().getHint();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(hint);
        alert.showAndWait();
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