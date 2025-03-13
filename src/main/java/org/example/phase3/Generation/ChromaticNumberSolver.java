package org.example.phase3.Generation;

import org.example.phase3.Algorithms.AlgorithmSelector;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Evaluation.GraphScorer;
import org.example.phase3.Evaluation.GraphType;
import java.util.List;

/**
 * Handles the process of solving the chromatic number for a given graph.
 * Acts as a coordinator between graph creation, type evaluation, and algorithm selection.
 */
public class ChromaticNumberSolver {
    /**
     * Solves the chromatic number for a given set of edges.
     * Creates a graph, evaluates its type, and executes the appropriate coloring algorithm.
     *
     * @param edges List of edges representing the graph structure
     */
    public static void solveChromaticNumber(List<ColEdge> edges) {
        // Create Graph object from edges
        Graph graph = new Graph(edges);
        
        // Use GraphScorer to determine graph type
        GraphScorer scorer = new GraphScorer();
        scorer.setCurrentGraph(graph);
        GraphType type = scorer.evaluateGraphType();
        
        // Use AlgorithmSelector to execute the appropriate algorithm and display results
        AlgorithmSelector.selectAndExecute(graph, type);
    }
}
