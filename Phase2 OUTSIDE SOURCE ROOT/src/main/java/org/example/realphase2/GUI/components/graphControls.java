package org.example.realphase2.GUI.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.example.realphase2.Generation.ColEdge;
import org.example.realphase2.Generation.RandomGraphGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

// This class provides UI components and functionality for controlling graph-related operations.
public class graphControls extends VBox {

    // Input fields for specifying the number of vertices and edges.
    private TextField vertexField;
    private TextField edgeField;

    // Buttons for generating a graph or loading one from a file.
    private Button generateButton;
    private Button loadFileButton;

    // Tracks the current graph configuration.
    private int numberOfVertices;
    private int numberOfEdges;
    public boolean isFileLoaded; // Indicates if a graph was loaded from a file.
    private int fileVertexCount; // Stores the vertex count of the loaded file graph.

    // Callback function for notifying when a graph is generated.
    private Consumer<List<ColEdge>> onGraphGenerated;

    // Constructor to initialize the UI components and set the callback function.
    public graphControls(Consumer<List<ColEdge>> onGraphGenerated) {
        this.onGraphGenerated = onGraphGenerated;
        initialize();
    }

    /**
     * Initializes the layout and user interface components for graph controls.
     */
    private void initialize() {
        setSpacing(10); // Set spacing between elements.
        setPadding(new Insets(10)); // Add padding around the VBox.
        setAlignment(Pos.TOP_CENTER); // Center the elements at the top.

        // Label and input field for the number of vertices.
        Label vertexLabel = new Label("Number of Vertices:");
        vertexField = new TextField();
        vertexField.setPromptText("Enter number of vertices");

        // Label and input field for the number of edges.
        Label edgeLabel = new Label("Number of Edges:");
        edgeField = new TextField();
        edgeField.setPromptText("Enter number of edges");

        // Button for generating a random graph.
        generateButton = new Button("Generate Random Graph");
        generateButton.setOnAction(e -> generateRandomGraph());

        // Button for loading a graph from a file.
        loadFileButton = new Button("Load Graph from File");
        loadFileButton.setOnAction(e -> selectGraphFile());

        // Add all UI elements to the VBox.
        getChildren().addAll(
            vertexLabel, vertexField,
            edgeLabel, edgeField,
            generateButton, loadFileButton
        );
    }

    /** 
     * Generates a random graph based on the user's input.
     */
    private void generateRandomGraph() {
        try {
            isFileLoaded = false; // Reset file-loaded status.
            numberOfVertices = getNumberOfVertices();
            numberOfEdges = getNumberOfEdges();

            // Validate user input.
            if (numberOfVertices <= 0 || numberOfEdges < 0) {
                showAlert("Input Error", "Please enter valid numbers for vertices and edges.");
                return;
            }

            // Generate edges using the RandomGraphGenerator.
            List<ColEdge> edges = RandomGraphGenerator.generateEdges(numberOfVertices, numberOfEdges);

            // Notify the callback function with the generated graph.
            if (onGraphGenerated != null) {
                onGraphGenerated.accept(edges);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers.");
        }
    }

    /**
     * Opens a file chooser to select and load a graph file.
     */
    private void selectGraphFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph File");
        File file = fileChooser.showOpenDialog(null); // Show the file chooser dialog.

        if (file != null) {
            try {
                // Parse edges from the selected file.
                List<ColEdge> edges = loadEdgesFromFile(file);

                // Determine the number of vertices in the graph.
                numberOfVertices = determineVertexCount(edges);
                fileVertexCount = numberOfVertices;

                // If the graph is valid, notify the callback function.
                if (numberOfVertices > 0) {
                    isFileLoaded = true;
                    onGraphGenerated.accept(edges);
                } else {
                    showAlert("Error", "No valid vertices found in the file.");
                }
            } catch (IOException | NumberFormatException e) {
                showAlert("Error", "Failed to load graph from file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads graph edges from a specified file.
     * The file is expected to contain edge definitions in a specific format, such as:
     * - Lines with two integers representing edge endpoints (e.g., "1 2").
     *
     * @param file The file to load edges from.
     * @return A list of ColEdge objects representing the graph edges.
     * @throws IOException If an error occurs while reading the file.
 */
    private List<ColEdge> loadEdgesFromFile(File file) throws IOException {
        List<ColEdge> edges = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        boolean foundEdgeSection = false;

        // Read the file line by line.
        while ((line = br.readLine()) != null) {
            line = line.trim();

            // Skip empty lines and comments.
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Start parsing edges after the "edges:" section.
            if (line.equalsIgnoreCase("edges:")) {
                foundEdgeSection = true;
                continue;
            }

            // Parse edges if inside the edge section.
            if (foundEdgeSection) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int u = Integer.parseInt(parts[0]);
                        int v = Integer.parseInt(parts[1]);
                        edges.add(new ColEdge(u, v));
                    } catch (NumberFormatException e) {
                        // Skip invalid lines.
                    }
                }
            }
        }
        br.close();

        // If no edges were found, attempt to parse the file as an edge list.
        if (edges.isEmpty()) {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 2) {
                        try {
                            int u = Integer.parseInt(parts[0]);
                            int v = Integer.parseInt(parts[1]);
                            edges.add(new ColEdge(u, v));
                        } catch (NumberFormatException e) {
                            // Skip invalid lines.
                        }
                    }
                }
            }
            br.close();
        }

        return edges;
    }

    /**
 * Determines the number of unique vertices in a list of graph edges.
 *
 * @param edges The list of ColEdge objects representing the graph edges.
 * @return The number of unique vertices.
 */
    private int determineVertexCount(List<ColEdge> edges) {
        Set<Integer> vertices = new HashSet<>();
        for (ColEdge edge : edges) {
            vertices.add(edge.u);
            vertices.add(edge.v);
        }
        return vertices.size();
    }

    /**
     * Retrieves the number of vertices specified by the user or loaded from a file.
     *
     * @return The number of vertices, or -1 if the input is invalid.
     */
    public int getNumberOfVertices() {
        if (isFileLoaded) {
            return fileVertexCount;
        }
        try {
            return Integer.parseInt(vertexField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Retrieves the number of edges specified by the user.
     *
     * @return The number of edges, or -1 if the input is invalid.
     */
    public int getNumberOfEdges() {
        try {
            return Integer.parseInt(edgeField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Retrieves the number of vertices in the graph loaded from a file.
     *
     * @return The number of vertices from the file.
     */
    public int getFileVertexCount() {
        return fileVertexCount;
    }

    /**
     * Displays an alert dialog with a specified title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to be shown in the alert dialog.
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}