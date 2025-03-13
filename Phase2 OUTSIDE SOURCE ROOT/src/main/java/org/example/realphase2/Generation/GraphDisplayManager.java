package org.example.realphase2.Generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.example.realphase2.Gamemodelogic.FlagClass;
import org.example.realphase2.Gamemodelogic.MainGameLogic;
import org.example.realphase2.scoring.GameTimer;
import org.example.realphase2.scoring.GetChromaticNumber;
import org.example.realphase2.scoring.Score;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GraphDisplayManager {
    // Map for storing vertices and their Circle representations
    private final Map<Integer, Circle> vertexCircles = new HashMap<>();

    // Store vertex positions
    private final Map<Integer, Point2D> vertexPositions = new HashMap<>();

    // Pane where the graph will be displayed
    private Pane graphPane;

    // Constants for force-directed layout
    private static final double REPULSION = 2000.0;
    private static final double ATTRACTION = 0.05;
    private static final int ITERATIONS = 100;
    private static final double MIN_DISTANCE = 150.0;

    private GameTimer gameTimer;

    private Score scoreLogic;

    // Initiates the game logic
    private MainGameLogic gameLogic;

    // Color picker for selecting colors
    private ColorPicker colorPicker;

    // Initiates the chromatic number getter
    private GetChromaticNumber chromaticNumber;

    // Button for setting the color picker to the uncolor color
    private Button uncolorButton = new Button("Uncolor Vertex");

    // Counter for checking how many colors are used
    private Label colorCounterLabel;
    private Map<Color, Integer> colorMap = new HashMap<>();
    private SimpleStringProperty colorCounter = new SimpleStringProperty("Colors used: 0");

    private boolean moveMode = false;
    private Button moveButton = new Button("Move Vertices");

    private double zoomFactor = 1.0;
    private double zoomIncrement = 0.1;

    private final Map<Integer, Color> vertexColors = new HashMap<>();

    /**
     *  Empty constructor - initialization happens in displayGraph
     */ 
    public GraphDisplayManager(MainGameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.colorPicker = new ColorPicker(Color.RED);
        this.colorCounterLabel = new Label("Colors used: 0");
        this.colorMap = new HashMap<>();
    }

    /**
     * Displays the graph on the provided pane.
     * Clears previous content and sets up vertices, edges, and interactive controls.
     *
     * @param graphPane   The pane where the graph will be displayed.
     * @param edges       List of edges in the graph.
     * @param numVertices The number of vertices in the graph.
     * @param gameMode    The game mode to be initialized.
     */

     public void displayGraph(Pane graphPane, List<ColEdge> edges, int numVertices, int gameMode) {
        this.graphPane = graphPane;
        graphPane.getChildren().clear();
        vertexCircles.clear();
        vertexPositions.clear();
        colorMap.clear();
        vertexColors.clear(); 
        setCounter();
        graphPane.getChildren().removeAll(colorPicker, colorCounterLabel, uncolorButton);
        String scoreString = Double.toString(0.0);
        FlagClass.score.set(scoreString);
        if (FlagClass.isTimerStarted) {
            gameTimer.stop();
            FlagClass.isTimerStarted = false;
        }
        // Draws the color picker
        colorPicker = createColorPicker();
        graphPane.getChildren().add(colorPicker);

        // Draws button that sets the color picker to the uncolor color when pressed
        uncolorButton.setOnAction(e -> colorPicker.setValue(Color.LIGHTBLUE));
        if (gameMode != 2) {
            graphPane.getChildren().add(uncolorButton);
        }
        uncolorButton.setLayoutX(300.0);

        // Draws the color counter
        colorCounterLabel = new Label();
        colorCounterLabel.textProperty().bind(colorCounter);
        colorCounterLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        colorCounterLabel.setLayoutX(150.0);
        graphPane.getChildren().add(colorCounterLabel);

        gameTimer = new GameTimer();
        gameTimer.start();
        FlagClass.isTimerStarted = true;

        // Passes important values needed for the game logic to run
        gameLogic.setValues(edges, numVertices, gameMode);

        // Passes important values needed for the chromatic number to be found
        chromaticNumber = new GetChromaticNumber();
        chromaticNumber.start(edges, numVertices);

        calculateAndRedrawGraph(edges, numVertices);

        moveButton.setOnAction(e -> {
            moveMode = !moveMode;
            moveButton.setText(moveMode ? "Resume Game" : "Move Vertices");
            if (!moveMode) {
                redrawGraph(edges, numVertices);
            }
        });
        graphPane.getChildren().add(moveButton);
        moveButton.setLayoutX(400.0);

        graphPane.setOnScroll(event -> {
            if (event.isControlDown()) {
                double delta = event.getDeltaY();
                if (delta > 0) {
                    zoomIn();
                } else if (delta < 0) {
                    zoomOut();
                }
                event.consume();
            }
        });
    }


    /**
     * Calculates the initial positions of vertices and draws the entire graph.
     *
     * @param edges       List of edges in the graph.
     * @param numVertices The number of vertices in the graph.
     */
    private void calculateAndRedrawGraph(List<ColEdge> edges, int numVertices) {
        initializeVertexPositions(numVertices);
        applyForceDirectedLayout(edges, numVertices);
        drawEdges(edges);
        drawVertices(numVertices);
    }

    /**
     * Creates and initializes a ColorPicker component for selecting colors.
     *
     * @return A fully initialized ColorPicker component.
     */

    private ColorPicker createColorPicker() {
        ColorPicker colorPicker = new ColorPicker();
        // Sets the starting color to red
        colorPicker.setValue(Color.RED);
        return colorPicker;
    }

    /**
     * Initializes vertex positions in a circular layout.
     *
     * @param numVertices The number of vertices in the graph.
     */
    private void initializeVertexPositions(int numVertices) {
        double centerX = graphPane.getWidth() / 2;
        double centerY = graphPane.getHeight() / 2;
        double radius = Math.min(graphPane.getWidth(), graphPane.getHeight()) * 0.4;

        vertexPositions.clear();
        for (int i = 1; i <= numVertices; i++) {
            double angle = 2 * Math.PI * (i - 1) / numVertices;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            vertexPositions.put(i, new Point2D(x, y));
        }
    }

    /**
     * Draws the edges in the graph.
     *
     * @param edges List of edges to be drawn.
     */
    private void drawEdges(List<ColEdge> edges) {
        for (ColEdge edge : edges) {
            Point2D start = vertexPositions.get(edge.u);
            Point2D end = vertexPositions.get(edge.v);
            if (start != null && end != null) {
                Line line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
                line.setStroke(Color.DARKGRAY);
                line.setStrokeWidth(2.5);

                // Add hover effects for edges and connected vertices
                line.setOnMouseEntered(e -> {
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(4.0);
                    vertexCircles.get(edge.u).setStroke(Color.RED);
                    vertexCircles.get(edge.v).setStroke(Color.RED);
                });
                line.setOnMouseExited(e -> {
                    line.setStroke(Color.DARKGRAY);
                    line.setStrokeWidth(2.5);
                    vertexCircles.get(edge.u).setStroke(Color.DARKBLUE);
                    vertexCircles.get(edge.v).setStroke(Color.DARKBLUE);
                });

                graphPane.getChildren().add(line);
            }
        }
    }

    // Scrambles the integer associated with each vertex so when the vertices are displayed,
    // their numbers will be randomly scrambled from what the generated graph states.
    /*
    private int[] scrambleVertices(int[] unscrambledVertices) {
        Random random = new Random();
        for (int i = 0; i == unscrambledVertices.length-1; i++) {
            int j = random.nextInt(unscrambledVertices.length);
            // Swap array[i] with array[j]
            int temp = unscrambledVertices[i];
            unscrambledVertices[i] = unscrambledVertices[j];
            unscrambledVertices[j] = temp;
        }
        return unscrambledVertices;
    }
    */


    /**
     * Draws the vertices of the graph and attaches interactive event handlers.
     *
     * @param numVertices The number of vertices to be drawn.
     */
    private void drawVertices(int numVertices) {
        /*
        int[] unscrambledVertices = new int[numVertices];
        int[] vertexIndexArray;
        for (int j = 0; j == numVertices; j++){
            unscrambledVertices[j] = j;
        }
        */
        //vertexIndexArray = scrambleVertices(unscrambledVertices);
        for (int i = 1; i <= numVertices; i++) {
            final int vertexIndex = i; // Declare final variable
            Point2D pos = vertexPositions.get(vertexIndex);
            if (pos != null) {
                Color color = vertexColors.getOrDefault(vertexIndex, Color.LIGHTBLUE);
                Circle circle = new Circle(pos.getX(), pos.getY(), 25, color);
                circle.setStroke(Color.DARKBLUE);
                circle.setStrokeWidth(3);

                Text label = new Text(String.valueOf(vertexIndex));
                label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                label.setFill(Color.BLACK);
                label.setX(pos.getX() - (vertexIndex < 10 ? 5 : 9));
                label.setY(pos.getY() + 6);

                // Add hover effect for vertices
                circle.setOnMouseEntered(e -> {
                    circle.setStroke(Color.RED);
                    circle.setStrokeWidth(4);
                });
                circle.setOnMouseExited(e -> {
                    circle.setStroke(Color.DARKBLUE);
                    circle.setStrokeWidth(3);
                });

                // Allows the color of the vertex circle to be changed when clicked on
                circle.setOnMouseClicked(event -> updateVertexColor(circle, String.valueOf(vertexIndex)));

                // Add event handlers for vertex movement
                circle.setOnMousePressed(event -> {
                    if (moveMode) {
                        circle.setStroke(Color.RED);
                        circle.setStrokeWidth(4);
                    }
                });
                circle.setOnMouseDragged(event -> {
                    if (moveMode) {
                        Point2D newPos = new Point2D(event.getX(), event.getY());
                        circle.setCenterX(newPos.getX());
                        circle.setCenterY(newPos.getY());
                        label.setX(newPos.getX() - (vertexIndex < 10 ? 5 : 9));
                        label.setY(newPos.getY() + 6);
                        vertexPositions.put(vertexIndex, newPos);
                    }
                });
                circle.setOnMouseReleased(event -> {
                    if (moveMode) {
                        circle.setStroke(Color.DARKBLUE);
                        circle.setStrokeWidth(3);
                    }
                });

                graphPane.getChildren().addAll(circle, label);
                vertexCircles.put(vertexIndex, circle);
            }
        }
    }

    /**
     * Updates the color of a vertex based on user selection from the color picker.
     *
     * @param circle    The Circle representing the vertex to be colored.
     * @param vertexNum The number of the vertex to be updated.
     */
    private void updateVertexColor(Circle circle, String vertexNum) {
        if (!moveMode) {
            Color newColor = Color.web(colorPicker.getValue().toString());
            Color oldColor = (Color) circle.getFill();
            int vertexIndex = Integer.parseInt(vertexNum);
    
            // If the color is set to the uncolor color (LIGHTBLUE), then the uncolor vertex game logic will run
            if (newColor.equals(Color.LIGHTBLUE)) {
                System.out.println("uncolor");
                gameLogic.unColor(vertexNum);

                if (FlagClass.illegalUncoloring) {
                    Alert uncoloringAlert = new Alert(Alert.AlertType.WARNING);
                    uncoloringAlert.setTitle("Illegal uncoloring");
                    uncoloringAlert.setHeaderText(null);
                    uncoloringAlert.setContentText("You must uncolor the vertices in order!");
                    uncoloringAlert.showAndWait();
                    FlagClass.illegalUncoloring = false;
                    return;
                }

                // Remove the old color from the color map for the counter
                if (colorMap.containsKey(oldColor)) {
                    colorMap.put(oldColor, colorMap.get(oldColor) - 1);
                    if (colorMap.get(oldColor) == 0) {
                        colorMap.remove(oldColor);
                    }
                }
    
                // Remove the color from the vertexColors map to ensure it doesn't revert back on redraw
                vertexColors.remove(vertexIndex);
    
                // Updates the counter with any changes (additions or removals)
                circle.setFill(newColor);
                setCounter();
                return;
            } else {
                gameLogic.tryToColor(vertexNum, newColor);
            }

            if (FlagClass.illegalVertex) {
                Alert vertexAlert = new Alert(Alert.AlertType.WARNING);
                vertexAlert.setTitle("Illegal Vertex");
                vertexAlert.setHeaderText(null);
                vertexAlert.setContentText("You must color the vertices in order!");
                vertexAlert.showAndWait();
                FlagClass.illegalVertex = false;
                return;
            }



            // If an illegal color was selected, this flag will be true.
            // If the flag is true, then the program will prevent the user
            // from coloring the vertex and display a window about this
            if (FlagClass.illegalColor) {
                Alert colorAlert = new Alert(Alert.AlertType.WARNING);
                colorAlert.setTitle("Illegal Color");
                colorAlert.setHeaderText(null);
                colorAlert.setContentText("You cannot color this vertex with the selected color.");
                colorAlert.showAndWait();
                FlagClass.illegalColor = false;
                return;
            }
    
            // Remove the old color from the color map for the counter
            if (colorMap.containsKey(oldColor)) {
                colorMap.put(oldColor, colorMap.get(oldColor) - 1);
                if (colorMap.get(oldColor) == 0) {
                    colorMap.remove(oldColor);
                }
            }
    
            // Updates the counter with any changes (additions or removals)
            colorMap.put(newColor, colorMap.getOrDefault(newColor, 0) + 1);
            circle.setFill(newColor);
            setCounter();
    
            // Asks the game logic if the graph has been fully colored
            // If it has, the graph will be deleted and the score
            // should be added and the timer will reset
            if (gameLogic.isGraphDone()) {
                int chromatic = chromaticNumber.getChromaticNumber();
                int colorsUsed = colorMap.size();
                graphPane.getChildren().clear();
                vertexCircles.clear();
                vertexPositions.clear();
                colorMap.clear();
                setCounter();
                graphPane.getChildren().removeAll(colorPicker, colorCounterLabel, uncolorButton);
                gameTimer.stopComplete();
    
                System.out.println(colorMap.size());
                int extraColorsUsed = calculateExtraColorsUsed(colorsUsed, chromatic);
                scoreLogic = new Score(extraColorsUsed, FlagClass.timeTaken);
                String scoreString = Double.toString(scoreLogic.calculateScore());
                FlagClass.score.set(scoreString);
                int scoreMessage = scoreLogic.scoreMessage();
    
                // Display score and message based on performance
                Alert scorePopup = new Alert(Alert.AlertType.INFORMATION);
                scorePopup.setTitle("Your score");
                switch (scoreMessage) {
                    case 1:
                        scorePopup.setHeaderText("You did everything perfect! Maybe try a more challenging graph.");
                        break;
                    case 2:
                        scorePopup.setHeaderText("You did great! But there is still some room for improvement.");
                        break;
                    case 3:
                        scorePopup.setHeaderText("Okay, not bad! But there is still plenty of room for improvement.");
                        break;
                    default:
                        scorePopup.setHeaderText("Nice try, but I know you can do better! Try again if you like.");
                        break;
                }
                scorePopup.setContentText("Your score: " + scoreString + "\nTime taken: " + scoreLogic.timeTakenConverter()
                        + "\nExtra colours used: " + extraColorsUsed);
                scorePopup.showAndWait();
                FlagClass.isTimerStarted = false;
            }
    
            // Store the new color in the vertexColors map
            vertexColors.put(vertexIndex, newColor);
        }
    }

    /**
     * Calculates the number of extra colors used beyond the chromatic number.
     *
     * @param colorsUsed     The total number of colours the player used to colour the graph.
     * @param chromaticNumber The minimum number of colours required to colour the graph (chromatic number).
     * @return The number of extra colours used. Returns 0 if the player used fewer or equal colours than the chromatic number.
     */
    private int calculateExtraColorsUsed(int colorsUsed, int chromaticNumber) {
        int extraColorsUsed = colorsUsed - chromaticNumber;
        // If somehow fewer colors were used than the size of the chromatic number, then there should be no extra colors used
        if (extraColorsUsed < 0) {
            return 0;
        }
        return extraColorsUsed;
    }

    /** 
     * This function sets the makes the counter text change depending on how many colors are maped/being used
     */ 
    private void setCounter() {
        colorCounter.set("Colors used: " + colorMap.size());
    }

    /**
     * Applies the force-directed layout algorithm to position vertices dynamically.
     *
     * @param edges       List of edges in the graph.
     * @param numVertices The number of vertices in the graph.
     */
    private void applyForceDirectedLayout(List<ColEdge> edges, int numVertices) {
        Map<Integer, Point2D> forces = new HashMap<>();

        for (int iter = 0; iter < ITERATIONS; iter++) {
            // Initialize forces for this iteration
            for (int i = 1; i <= numVertices; i++) {
                forces.put(i, new Point2D(0, 0));
            }

            // Calculate repulsion forces between all vertices
            for (int i = 1; i <= numVertices; i++) {
                for (int j = i + 1; j <= numVertices; j++) {
                    Point2D pos1 = vertexPositions.get(i);
                    Point2D pos2 = vertexPositions.get(j);
                    double dx = pos2.getX() - pos1.getX();
                    double dy = pos2.getY() - pos1.getY();
                    double distance = Math.max(0.1, Math.sqrt(dx * dx + dy * dy));

                    double force = REPULSION / (distance * distance);
                    double fx = (dx / distance) * force;
                    double fy = (dy / distance) * force;

                    forces.put(i, forces.get(i).add(-fx, -fy));
                    forces.put(j, forces.get(j).add(fx, fy));
                }
            }

            // Calculate attraction forces for edges
            for (ColEdge edge : edges) {
                Point2D pos1 = vertexPositions.get(edge.u);
                Point2D pos2 = vertexPositions.get(edge.v);
                double dx = pos2.getX() - pos1.getX();
                double dy = pos2.getY() - pos1.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 0) {
                    double force = ATTRACTION * distance;
                    double fx = (dx / distance) * force;
                    double fy = (dy / distance) * force;

                    forces.put(edge.u, forces.get(edge.u).add(fx, fy));
                    forces.put(edge.v, forces.get(edge.v).add(-fx, -fy));
                }
            }

            // Update positions based on forces
            for (int i = 1; i <= numVertices; i++) {
                Point2D force = forces.get(i);
                Point2D pos = vertexPositions.get(i);
                Point2D newPos = pos.add(force.getX(), force.getY());

                // Keep vertices within bounds with padding
                newPos = new Point2D(
                        Math.max(50, Math.min(graphPane.getWidth() - 50, newPos.getX())),
                        Math.max(50, Math.min(graphPane.getHeight() - 50, newPos.getY()))
                );

                vertexPositions.put(i, newPos);
            }

            // Apply minimum distance between vertices
            for (int i = 1; i <= numVertices; i++) {
                for (int j = i + 1; j <= numVertices; j++) {
                    Point2D pos1 = vertexPositions.get(i);
                    Point2D pos2 = vertexPositions.get(j);
                    double dx = pos2.getX() - pos1.getX();
                    double dy = pos2.getY() - pos1.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance < MIN_DISTANCE) {
                        double angle = Math.atan2(dy, dx);
                        double moveX = (MIN_DISTANCE - distance) * Math.cos(angle) / 2;
                        double moveY = (MIN_DISTANCE - distance) * Math.sin(angle) / 2;

                        vertexPositions.put(i, pos1.add(-moveX, -moveY));
                        vertexPositions.put(j, pos2.add(moveX, moveY));
                    }
                }
            }
        }
    }

    /**
     * Redraws the graph, preserving vertex colors and interactive controls.
     *
     * @param edges       List of edges in the graph.
     * @param numVertices The number of vertices in the graph.
     */
    private void redrawGraph(List<ColEdge> edges, int numVertices) {
        graphPane.getChildren().clear();
        // vertexColors.clear(); // Removed this line to preserve colors
        drawEdges(edges);
        drawVertices(numVertices);
        graphPane.getChildren().addAll(colorPicker, colorCounterLabel, uncolorButton, moveButton);
    }

    /** 
     * This function zooms in the graph
     */
    private void zoomIn() {
        zoomFactor += zoomIncrement;
        applyZoom();
    }

    /** 
     * This function zooms out the graph
     */
    private void zoomOut() {
        zoomFactor = Math.max(0.1, zoomFactor - zoomIncrement);
        applyZoom();
    }

    /** 
     * This function applies the zoom factor to the graph pane
     */
    private void applyZoom() {
        graphPane.setScaleX(zoomFactor);
        graphPane.setScaleY(zoomFactor);
    }
}
