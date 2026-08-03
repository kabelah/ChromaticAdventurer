package org.example.phase3.GUI.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.example.phase3.Evaluation.GraphScorer;
import org.example.phase3.Evaluation.Graph;       
import org.example.phase3.Evaluation.GraphType;  
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Generation.RandomGraphGenerator;

/**
 * This class provides UI components and functionality 
 * for controlling graph-related operations.
 */
public class graphControls extends VBox {

    private TextField vertexField;
    private TextField edgeField;
    private Button generateButton;
    private Button loadFileButton;

    private int numberOfVertices;
    private int numberOfEdges;

    public boolean isFileLoaded; 
    private int fileVertexCount; 

    private Consumer<List<ColEdge>> onGraphGenerated;
    private List<ColEdge> currentEdges;

    public graphControls(Consumer<List<ColEdge>> onGraphGenerated) {
        this.onGraphGenerated = onGraphGenerated;
        initialize();
    }

    /**
     * Initializes the layout and user interface components for graph controls.
     */
    private void initialize() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);

        // Label and input field for the number of vertices
        Label vertexLabel = new Label("Number of Vertices:");
        vertexField = new TextField();
        vertexField.setPromptText("Enter number of vertices");

        // Label and input field for the number of edges
        Label edgeLabel = new Label("Number of Edges:");
        edgeField = new TextField();
        edgeField.setPromptText("Enter number of edges");

        // Button for generating a random graph
        generateButton = new Button("Generate Random Graph");
        generateButton.setOnAction(e -> generateRandomGraph());

        // Button for loading a graph from a file
        loadFileButton = new Button("Load Graph from File");
        loadFileButton.setOnAction(e -> selectGraphFile());

        // Add UI elements to the VBox
        getChildren().addAll(
            vertexLabel, vertexField,
            edgeLabel, edgeField,
            generateButton, loadFileButton
        );
    }

    /**
     * Generates a random graph based on the user's input, then scores it for debugging.
     */
    private void generateRandomGraph() {
        try {
            isFileLoaded = false; 
            numberOfVertices = getNumberOfVertices();
            numberOfEdges = getNumberOfEdges();

            if (numberOfVertices <= 0 || numberOfEdges < 0) {
                showAlert("Input Error", "Please enter valid numbers for vertices and edges.");
                return;
            }

            // Generate edges using the RandomGraphGenerator
            List<ColEdge> edges = RandomGraphGenerator.generateEdges(numberOfVertices, numberOfEdges);
            currentEdges = edges; // Store in case we need them later

            // (A) Notify any listeners
            if (onGraphGenerated != null) {
                onGraphGenerated.accept(edges);
            }

            // (B) DEBUG: Score this graph immediately
            GraphScorer scorer = new GraphScorer();
            // Convert edges to a Graph object (assuming your Graph constructor takes these edges)
            Graph g = new Graph(edges);
            scorer.setCurrentGraph(g);

            System.out.println("\n[DEBUG] --- Generated Random Graph ---");
            // This call prints the score map and classification
            scorer.evaluateGraphType();  

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers.");
        }
    }

    /**
     * Opens a file chooser to select and load a graph file, then scores it for debugging.
     */
    public void selectGraphFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<ColEdge> edges = loadEdgesFromFile(file);
                currentEdges = edges;
                isFileLoaded = true;
                fileVertexCount = determineVertexCount(edges);

                // (A) Notify any listeners
                if (onGraphGenerated != null) {
                    onGraphGenerated.accept(edges);
                }

                // (B) DEBUG: Score this graph immediately
                GraphScorer scorer = new GraphScorer();
                Graph g = new Graph(edges);
                scorer.setCurrentGraph(g);

                System.out.println("\n[DEBUG] --- Loaded Graph From File: " + file.getName() + " ---");
                // This call prints the score map and classification
                scorer.evaluateGraphType(); 

            } catch (IOException e) {
                showAlert("File Error", "Error reading the file.");
            }
        }
    }

    /**
     * Loads graph edges from a specified file.
     */
    public List<ColEdge> loadEdgesFromFile(File file) throws IOException {
        List<ColEdge> edges = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        boolean foundEdgeSection = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.equalsIgnoreCase("edges:")) {
                foundEdgeSection = true;
                continue;
            }
            if (foundEdgeSection) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int u = Integer.parseInt(parts[0]);
                        int v = Integer.parseInt(parts[1]);
                        edges.add(new ColEdge(u, v));
                    } catch (NumberFormatException e) {
                     
                    }
                }
            }
        }
        br.close();

        // If no "edges:" section found or no edges read, try reading the file line by line as an edge list
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
                            // skip
                        }
                    }
                }
            }
            br.close();
        }
        return edges;
    }

    /**
     * Determines the number of unique vertices in a list of edges.
     */
    public int determineVertexCount(List<ColEdge> edges) {
        Set<Integer> vertices = new HashSet<>();
        for (ColEdge edge : edges) {
            vertices.add(edge.u);
            vertices.add(edge.v);
        }
        return vertices.size();
    }

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

    public int getNumberOfEdges() {
        try {
            return Integer.parseInt(edgeField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int getFileVertexCount() {
        return fileVertexCount;
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public List<ColEdge> getCurrentEdges() {
        return currentEdges;
    }

    public int getVertexCount() {
        return isFileLoaded ? getFileVertexCount() : getNumberOfVertices();
    }
}