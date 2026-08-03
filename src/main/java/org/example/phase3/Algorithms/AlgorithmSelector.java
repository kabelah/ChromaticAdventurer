package org.example.phase3.Algorithms;

import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Evaluation.GraphType;
import org.example.phase3.DefaultGraphCases.DSATUR;
import org.example.phase3.phase1algo.ObtainChromaticNumber;
import org.example.phase3.SpecialGraphCases.*;
import org.example.phase3.GUI.components.GraphPainter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Map;
import java.util.HashMap;

public class AlgorithmSelector {
    /**
     * Selects and executes the appropriate coloring algorithm based on graph type.
     * Each algorithm returns a Map<Integer, Integer> of vertex-to-color assignments.
     */
    public static void selectAndExecute(Graph graph, GraphType type) {
        System.out.println("Starting coloring algorithm for graph type: " + type);
        Map<Integer, Integer> colorMap;
        int chromaticNumber;
        String algorithmName;
        ColoringResult result;

        try {
            // Select algorithm based on graph type
            switch (type) {
                case TREE:
                    result = TreeGraphColouring.colour(graph);
                    break;
                case BIPARTITE:
                    result = BipartiteGraphColouring.colour(graph);
                    break;
                case CYCLE:
                    result = CycleGraphColouring.colour(graph);
                    break;
                case COMPLETE:
                    result = CompleteGraphColouring.colour(graph);
                    break;
                case STAR:
                    result = StarGraphColouring.colour(graph);
                    break;
                case WHEEL:
                    result = WheelGraphSolver.colour(graph);
                    break;
                case FOREST:
                    result = ForestGraphColouring.colour(graph);
                    break;
                case CHORDAL:
                    result = ChordalGraphColouring.colour(graph);
                    break;
                case SPLIT:
                    result = SplitGraphAlgorithm.colour(graph);
                    if (result == null) {
                        // If not a split graph, fall back to Phase 1 Algorithm
                        ObtainChromaticNumber phase1 = new ObtainChromaticNumber();
                        result = phase1.findColoring(graph);
                    }
                    break;
                case PLANAR:
                    result = PlanarGraphSolver.colour(graph);
                    break;
                default:
                    // Use Phase 1 Algorithm
                    ObtainChromaticNumber phase1 = new ObtainChromaticNumber();
                    result = phase1.findColoring(graph);
                    break;
            }

            if (result == null) {
                throw new RuntimeException("Failed to color graph - algorithm returned null result");
            }

            colorMap = result.getColorAssignments();
            chromaticNumber = result.getChromaticNumber();
            algorithmName = result.getAlgorithmName();

            // Convert the result to format needed by GraphPainter
            int[] colorArray = new int[graph.getVertexCount()];
            for (int i = 0; i < colorArray.length; i++) {
                colorArray[i] = colorMap.getOrDefault(i + 1, 0);
            }

            System.out.println("Algorithm used: " + algorithmName);
            System.out.println("Chromatic number: " + chromaticNumber);
            System.out.println("Number of vertices colored: " + graph.getVertexCount());

            // Display the result using GraphPainter
            GraphPainter.graphPainter(colorArray, chromaticNumber, 
                "Algorithm(s) used: " + algorithmName);
        } catch (Exception e) {
            System.err.println("Error during graph coloring: " + e.getMessage());
        }
    }
}