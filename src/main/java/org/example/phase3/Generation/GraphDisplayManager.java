package org.example.phase3.Generation;

import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.example.phase3.GUI.screens.GraphDisplayerScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for generating and displaying a graph (vertices + edges) 
 * on a JavaFX Pane. Supports force-directed layout, random/loaded layouts,
 * and vertex-dragging. Also supports a displayMessage(...) method for popups.
 */
public class GraphDisplayManager {

    // --------------------------
    //       DATA STRUCTURES
    // --------------------------
    // Maps vertex -> Circle (the visual node)
    private final Map<Integer, Circle> vertexCircles = new HashMap<>();

    // Maps vertex -> Label (so we can bring labels to front)
    private final Map<Integer, Text> vertexLabels = new HashMap<>();

    // Maps vertex -> Position on the graphPane
    private final Map<Integer, Point2D> vertexPositions = new HashMap<>();

    // Vertex coloring 
    private final Map<Integer, Color> vertexColors = new HashMap<>();

    // The edges of the graph
    private List<ColEdge> currentEdges;

    // The Pane used to display the graph
    private Pane graphPane;

    // Constants for force-directed layout
    private static final double REPULSION    = 2000.0;
    private static final double ATTRACTION   = 0.05;
    private static final int    ITERATIONS   = 100;
    private static final double MIN_DISTANCE = 150.0;

    // UI / game variables
    public static Circle[] circle;
    private static boolean moveMode = false;
    private final Button moveButton = new Button("Move Vertices");

    // --------------------------
    //         CONSTRUCTOR
    // --------------------------
    public GraphDisplayManager() {
        // Empty constructor
    }

    // --------------------------
    //         MAIN ENTRY
    // --------------------------
    /**
     * Displays the graph on the provided Pane.
     * 
     * @param graphPane   The Pane where the graph is displayed
     * @param edges       All edges of the graph
     * @param numVertices Number of vertices
     * @param gameMode    1 => Random, 2 => Loaded
     */
    public void displayGraph(Pane graphPane, List<ColEdge> edges, int numVertices, int gameMode) {
        // Initialize references
        this.graphPane = graphPane;
        this.currentEdges = edges;

        // Clear previous items
        graphPane.getChildren().clear();
        vertexCircles.clear();
        vertexLabels.clear();
        vertexPositions.clear();

        // Show color-graph UI button
        GraphDisplayerScreen.colorGraph.setVisible(true);

        // Choose layout / generation based on gameMode
        if (gameMode == 1) {
            // RANDOM graphs => circular layout + force-directed
            initializeVertexPositions(numVertices);
            applyForceDirectedLayout(edges, numVertices);
            drawEdges(edges);
            drawVertices(numVertices);
        } else if (gameMode == 2) {
            // LOADED graphs => circle arrangement
            renderLoadedGraph(edges, numVertices);
        }

        // Enable SHIFT+Scroll zooming
        graphPane.setOnScroll(event -> {
            if (event.isShiftDown()) {
                double zoomFactor = (event.getDeltaY() < 0) ? 0.95 : 1.05;
                graphPane.setScaleX(graphPane.getScaleX() * zoomFactor);
                graphPane.setScaleY(graphPane.getScaleY() * zoomFactor);
                event.consume();
            }
        });
    }

    // --------------------------
    //      MESSAGE POPUP
    // --------------------------
    /**
     * Displays an informational message in a popup Alert.
     */
    public static void displayMessage(int chromaticNumber, String algorithmsUsed) {
        Alert scorePopup = new Alert(Alert.AlertType.INFORMATION);
        scorePopup.setTitle("Graph Information");
        scorePopup.setHeaderText("Chromatic number: " + chromaticNumber + "\n" + algorithmsUsed);
        scorePopup.showAndWait();
    }

    // --------------------------
    //        ERROR POPUP
    // --------------------------
    /**
     * Displays an informational message in a popup Alert.
     */
    public static void displayError(String errorMessage) {
        Alert scorePopup = new Alert(Alert.AlertType.ERROR);
        scorePopup.setTitle("Graph Creation Error");
        scorePopup.setHeaderText(errorMessage);
        scorePopup.showAndWait();
    }

    // --------------------------
    //       LAYOUT METHODS
    // --------------------------
    /**
     * Places vertices in a (roughly) circular layout around the Pane center.
     * 
     * @param numVertices number of vertices
     */
    private void initializeVertexPositions(int numVertices) {
        double centerX = graphPane.getWidth() / 2;
        double centerY = graphPane.getHeight() / 2;
        // Increase base radius and scale with number of vertices
        double radius = Math.min(graphPane.getWidth(), graphPane.getHeight()) 
                       * 0.85 * (1 + Math.log10(numVertices) * 0.2);

        for (int i = 1; i <= numVertices; i++) {
            double angle = 2 * Math.PI * (i - 1) / numVertices;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            vertexPositions.put(i, new Point2D(x, y));
        }
    }

    /**
     * Force-directed layout to spread vertices.
     * Uses repulsion among all vertices + attraction on edges.
     */
    private void applyForceDirectedLayout(List<ColEdge> edges, int numVertices) {
        // We'll store net forces in this map each iteration
        Map<Integer, Point2D> forces = new HashMap<>();

        for (int iter = 0; iter < ITERATIONS; iter++) {
            // Reset forces to zero each iteration
            for (int i = 1; i <= numVertices; i++) {
                forces.put(i, new Point2D(0, 0));
            }

            // (1) Repulsion among all vertex pairs
            for (int i = 1; i <= numVertices; i++) {
                for (int j = i + 1; j <= numVertices; j++) {
                    Point2D pos1 = vertexPositions.get(i);
                    Point2D pos2 = vertexPositions.get(j);
                    if (pos1 == null || pos2 == null) {
                        continue;
                    }

                    double dx = pos2.getX() - pos1.getX();
                    double dy = pos2.getY() - pos1.getY();
                    double dist = Math.max(0.1, Math.sqrt(dx * dx + dy * dy));

                    double force = REPULSION / (dist * dist);
                    double fx = (dx / dist) * force;
                    double fy = (dy / dist) * force;

                    forces.put(i, forces.get(i).add(-fx, -fy));
                    forces.put(j, forces.get(j).add(fx, fy));
                }
            }

            // (2) Attraction along edges
            for (ColEdge edge : edges) {
                Point2D pos1 = vertexPositions.get(edge.u);
                Point2D pos2 = vertexPositions.get(edge.v);
                if (pos1 == null || pos2 == null) {
                    continue;
                }

                double dx = pos2.getX() - pos1.getX();
                double dy = pos2.getY() - pos1.getY();
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist > 0) {
                    double force = ATTRACTION * dist;
                    double fx = (dx / dist) * force;
                    double fy = (dy / dist) * force;

                    forces.put(edge.u, forces.get(edge.u).add(fx, fy));
                    forces.put(edge.v, forces.get(edge.v).add(-fx, -fy));
                }
            }

            // (3) Update positions from the forces
            for (int i = 1; i <= numVertices; i++) {
                if (!vertexPositions.containsKey(i)) {
                    continue;
                }
                Point2D force = forces.get(i);
                Point2D oldPos = vertexPositions.get(i);
                Point2D newPos = oldPos.add(force);

                // Keep vertices within bounds (padding = 50)
                double boundedX = Math.max(50, Math.min(graphPane.getWidth() - 50, newPos.getX()));
                double boundedY = Math.max(50, Math.min(graphPane.getHeight() - 50, newPos.getY()));
                vertexPositions.put(i, new Point2D(boundedX, boundedY));
            }

            // (4) Enforce a minimum distance to avoid overlapping
            for (int i = 1; i <= numVertices; i++) {
                for (int j = i + 1; j <= numVertices; j++) {
                    Point2D p1 = vertexPositions.get(i);
                    Point2D p2 = vertexPositions.get(j);
                    if (p1 == null || p2 == null) {
                        continue;
                    }

                    double dx = p2.getX() - p1.getX();
                    double dy = p2.getY() - p1.getY();
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist < MIN_DISTANCE) {
                        double angle = Math.atan2(dy, dx);
                        double move = (MIN_DISTANCE - dist) / 2.0;
                        double offsetX = move * Math.cos(angle);
                        double offsetY = move * Math.sin(angle);

                        // Push them away equally
                        vertexPositions.put(i, p1.subtract(offsetX, offsetY));
                        vertexPositions.put(j, p2.add(offsetX, offsetY));
                    }
                }
            }
        }
    }

    // --------------------------
    //       DRAW METHODS
    // --------------------------
    /**
     * Draws edges as Lines between vertex positions.
     */
    private void drawEdges(List<ColEdge> edges) {
        for (ColEdge edge : edges) {
            Point2D start = vertexPositions.get(edge.u);
            Point2D end   = vertexPositions.get(edge.v);

            if (start != null && end != null) {
                double dx = end.getX() - start.getX();
                double dy = end.getY() - start.getY();
                double length = Math.sqrt(dx*dx + dy*dy);

                // Normalize direction
                dx /= length;
                dy /= length;

                // "25" => radius of the circles
                Point2D adjustedStart = new Point2D(start.getX() + dx * 25,
                                                    start.getY() + dy * 25);
                Point2D adjustedEnd   = new Point2D(end.getX() - dx * 25,
                                                    end.getY() - dy * 25);

                // Create the edge (Line)
                Line line = new Line(adjustedStart.getX(), adjustedStart.getY(),
                                     adjustedEnd.getX(),   adjustedEnd.getY());
                line.setStroke(Color.DARKGRAY);
                line.setStrokeWidth(2.5);

                // Hover highlight: highlight the line + connected vertices
                line.setOnMouseEntered(e -> {
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(4.0);
                    if (vertexCircles.get(edge.u) != null) {
                        vertexCircles.get(edge.u).setStroke(Color.RED);
                    }
                    if (vertexCircles.get(edge.v) != null) {
                        vertexCircles.get(edge.v).setStroke(Color.RED);
                    }
                });
                line.setOnMouseExited(e -> {
                    line.setStroke(Color.DARKGRAY);
                    line.setStrokeWidth(2.5);
                    if (vertexCircles.get(edge.u) != null) {
                        vertexCircles.get(edge.u).setStroke(Color.BLACK);
                    }
                    if (vertexCircles.get(edge.v) != null) {
                        vertexCircles.get(edge.v).setStroke(Color.BLACK);
                    }
                });

                graphPane.getChildren().add(line);
            }
        }
    }

    /**
     * Draws vertices (as circles + labels) and attaches event handlers for dragging.
     */
    private void drawVertices(int numVertices) {
        circle = new Circle[numVertices + 1]; // store circles in an array if needed externally

        for (int i = 1; i <= numVertices; i++) {
            final int vertexIndex = i;
            Point2D pos = vertexPositions.get(vertexIndex);

            if (pos != null) {
                // Grab color if previously known; else default to LIGHTBLUE
                Color color = vertexColors.getOrDefault(vertexIndex, Color.LIGHTBLUE);

                Circle vertexCircle = new Circle(pos.getX(), pos.getY(), 25, color);
                vertexCircle.setStroke(Color.BLACK);
                vertexCircle.setStrokeWidth(3);
                circle[i] = vertexCircle; // for external usage

                // Label: shows the vertex index
                Text label = new Text(String.valueOf(vertexIndex));
                label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                label.setFill(Color.BLACK);
                label.setX(pos.getX() - (vertexIndex < 10 ? 5 : 9));
                label.setY(pos.getY() + 6);
                vertexLabels.put(vertexIndex, label);

                // Hover effect: highlight the circle
                vertexCircle.setOnMouseEntered(e -> {
                    vertexCircle.setStroke(Color.RED);
                    vertexCircle.setStrokeWidth(4);
                });
                vertexCircle.setOnMouseExited(e -> {
                    vertexCircle.setStroke(Color.BLACK);
                    vertexCircle.setStrokeWidth(3);
                });

                // Mouse press => highlight
                vertexCircle.setOnMousePressed(e -> {
                    vertexCircle.setStroke(Color.RED);
                    vertexCircle.setStrokeWidth(4);
                });

                // Dragging => move circle + label, redraw edges, bring everything forward
                vertexCircle.setOnMouseDragged(e -> {
                    // 1) Move the circle and label directly
                    vertexCircle.setCenterX(e.getX());
                    vertexCircle.setCenterY(e.getY());
                    label.setX(e.getX() - (vertexIndex < 10 ? 5 : 9));
                    label.setY(e.getY() + 6);
                
                    // 2) Update internal position map for future layout logic
                    vertexPositions.put(vertexIndex, new Point2D(e.getX(), e.getY()));
                
                    // 3) Remove all existing edges (only) in a safe way
                    graphPane.getChildren().removeIf(node -> node instanceof Line);
                
                    // 4) Redraw edges at the new positions
                    drawEdges(currentEdges);
                
                    // 5) Bring all circles and labels to front so they stay above edges
                    for (Circle c : vertexCircles.values()) {
                        c.toFront();
                    }
                    for (Text t : vertexLabels.values()) {
                        t.toFront();
                    }
                });

                // Mouse release => revert highlight
                vertexCircle.setOnMouseReleased(e -> {
                    vertexCircle.setStroke(Color.BLACK);
                    vertexCircle.setStrokeWidth(3);
                });

                // Add circle + label to the Pane
                graphPane.getChildren().addAll(vertexCircle, label);
                vertexCircles.put(vertexIndex, vertexCircle);
            }
        }
    }

    // --------------------------
    //       COLORING
    // --------------------------
    /**
     * Called from external code to update a circle's color.
     * (Currently just sets the fill to RED, for demonstration.)
     */
    public static void updateVertexColor(Circle circle, Color vertexColor) {
        if (!moveMode && circle != null) {
            circle.setFill(vertexColor);
        }
    }

    // --------------------------
    //   LOADED GRAPH LAYOUT
    // --------------------------
    /**
     * Renders a "loaded" graph in a circular layout with the last vertex in the center.
     */
    private void renderLoadedGraph(List<ColEdge> edges, int numVertices) {
        if (numVertices <= 0) return;

        double centerX = graphPane.getWidth() / 2;
        double centerY = graphPane.getHeight() / 2;
        // Increase base radius and scale with number of vertices
        double radius = Math.min(centerX, centerY) * 0.85 * (1 + Math.log10(numVertices) * 0.2);

        // Position vertices
        for (int i = 1; i <= numVertices; i++) {
            double x, y;
            if (i == numVertices) {
                x = centerX;
                y = centerY;
            } else {
                double angle = 2 * Math.PI * (i - 1) / (numVertices - 1);
                x = centerX + radius * Math.cos(angle);
                y = centerY + radius * Math.sin(angle);
            }
            vertexPositions.put(i, new Point2D(x, y));
        }

        drawEdges(edges);
        drawVertices(numVertices);
    }
}