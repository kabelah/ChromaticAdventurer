package org.example.phase3.phase1algo;

import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import org.example.phase3.phase1algo.algorithms.*;
import org.example.phase3.phase1algo.structures.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ObtainChromaticNumber {
    private static final int BACKTRACKING_TIME = 120000; // 2 minutes in milliseconds

    public ColoringResult findColoring(Graph originalGraph) {
        try {
            // Convert our Graph to the Phase 1 format
            int[][] matrix = new int[originalGraph.getVertexCount()][originalGraph.getVertexCount()];
            int[] edgeList = new int[originalGraph.getEdges().size() * 2];
            int idx = 0;
            for (var edge : originalGraph.getEdges()) {
                // Ensure indices are within bounds
                if (edge.u <= 0 || edge.u > originalGraph.getVertexCount() || 
                    edge.v <= 0 || edge.v > originalGraph.getVertexCount()) {
                    throw new IllegalArgumentException("Invalid vertex indices in edge");
                }
                edgeList[idx++] = edge.u - 1;  // Convert to 0-based indexing
                edgeList[idx++] = edge.v - 1;
                matrix[edge.u - 1][edge.v - 1] = 1;
                matrix[edge.v - 1][edge.u - 1] = 1;
            }
            
            org.example.phase3.phase1algo.structures.Graph graph = 
                new org.example.phase3.phase1algo.structures.Graph(originalGraph.getVertexCount(), matrix, edgeList);

            // Check if graph is complete
            if (isComplete(graph)) {
                return handleCompleteGraph(graph);
            }

            // 1. Get upper bound using greedy algorithm
            Bounds.upperBoundGreedy(graph);
            int upperBoundWP = graph.getUpperBound();

            // 2. Get lower bound using clique detection
            Bounds.LowerBound.lowerBoundLargestClique(graph);
            int lowerBound = graph.getLowerBound();

            // 3. Get DSATUR upper bound
            ChromaticNumber.DSATUR(graph);
            int upperBoundDsatur = graph.getChromaticNumber();

            // Use the lowest upper bound
            int lowestUpperBound = Math.min(upperBoundDsatur, upperBoundWP);
            
            // If bounds match, we have the exact chromatic number
            if (lowerBound == lowestUpperBound) {
                return createColoringResult(graph, lowerBound, 
                    String.format("Exact chromatic number found through bounds (LB = UB = %d)", lowerBound));
            }

            // Run backtracking with the bounds
            TimerCheck timer = new TimerCheck(BACKTRACKING_TIME);
            AtomicInteger bestChromaticNumber = new AtomicInteger(lowestUpperBound);
            Backtracking backtracking = new Backtracking(timer, bestChromaticNumber);
            
            timer.start();
            backtracking.runBacktracking(matrix, lowestUpperBound, lowerBound);

            // Get the final chromatic number and coloring
            int chromaticNumber = bestChromaticNumber.get();
            if (chromaticNumber == Integer.MAX_VALUE) {
                chromaticNumber = lowestUpperBound;  // Use the best upper bound we found
            }

            // Get the coloring from backtracking
            int[] backtrackingColoring = backtracking.getBestColoring();
            if (backtrackingColoring != null && isValidColoring(backtrackingColoring, matrix)) {
                graph.setColoring(backtrackingColoring);
            }

            return createColoringResult(graph, chromaticNumber,
                String.format("Phase 1 Algorithm (LB: %d, UB: %d, Final: %d)", 
                    lowerBound, lowestUpperBound, chromaticNumber));

        } catch (Exception e) {
            // If any error occurs, return a safe fallback using DSATUR
            return fallbackToSafeColoring(originalGraph);
        }
    }

    private boolean isComplete(org.example.phase3.phase1algo.structures.Graph graph) {
        int n = graph.getOrder();
        int expectedEdges = (n * (n - 1)) / 2;
        return graph.getEdgeList().length == expectedEdges * 2;  // *2 because edges are stored twice
    }

    private ColoringResult handleCompleteGraph(org.example.phase3.phase1algo.structures.Graph graph) {
        int n = graph.getOrder();
        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            colorMap.put(i + 1, i + 1);  // Each vertex gets a unique color
        }
        return new ColoringResult(colorMap, n, "Complete graph - chromatic number equals order");
    }

    private ColoringResult createColoringResult(org.example.phase3.phase1algo.structures.Graph graph, 
                                              int chromaticNumber, String algorithmInfo) {
        Map<Integer, Integer> colorMap = new HashMap<>();
        int[] coloring = graph.getColoring();
        for (int i = 0; i < coloring.length; i++) {
            colorMap.put(i + 1, coloring[i] + 1);  // Convert back to 1-based indexing
        }
        return new ColoringResult(colorMap, chromaticNumber, algorithmInfo);
    }

    private ColoringResult fallbackToSafeColoring(Graph originalGraph) {
        // Create a new graph instance for DSATUR
        int[][] matrix = new int[originalGraph.getVertexCount()][originalGraph.getVertexCount()];
        int[] edgeList = new int[originalGraph.getEdges().size() * 2];
        int idx = 0;
        for (var edge : originalGraph.getEdges()) {
            if (edge.u - 1 < matrix.length && edge.v - 1 < matrix.length) {
                edgeList[idx++] = edge.u - 1;
                edgeList[idx++] = edge.v - 1;
                matrix[edge.u - 1][edge.v - 1] = 1;
                matrix[edge.v - 1][edge.u - 1] = 1;
            }
        }
        
        org.example.phase3.phase1algo.structures.Graph graph = 
            new org.example.phase3.phase1algo.structures.Graph(originalGraph.getVertexCount(), matrix, edgeList);
        
        ChromaticNumber.DSATUR(graph);
        return createColoringResult(graph, graph.getChromaticNumber(),
            "Fallback to DSATUR due to error in main algorithm");
    }

    private boolean isValidColoring(int[] coloring, int[][] matrix) {
        for (int i = 0; i < coloring.length; i++) {
            if (coloring[i] == -1) return false;
            for (int j = 0; j < coloring.length; j++) {
                if (matrix[i][j] == 1 && coloring[i] == coloring[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
