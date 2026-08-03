package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.*;

public class SplitGraphAlgorithm {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Calculate degree sequence
        Map<Integer, Integer> degrees = new HashMap<>();
        for (ColEdge edge : edges) {
            degrees.merge(edge.u, 1, Integer::sum);
            degrees.merge(edge.v, 1, Integer::sum);
        }
        
        // Sort vertices by degree in descending order
        List<Integer> sortedVertices = new ArrayList<>(degrees.keySet());
        sortedVertices.sort((a, b) -> degrees.get(b).compareTo(degrees.get(a)));
        
        // Find the split point m
        int m = -1;
        for (int i = 0; i < sortedVertices.size(); i++) {
            if (degrees.get(sortedVertices.get(i)) >= i) {
                m = i;
            }
        }
        
        if (m == -1) {
            // Not a split graph
            return null;
        }
        
        // Verify the split graph property:
        // - First m vertices should form a clique
        // - Remaining vertices should form an independent set
        Set<Integer> clique = new HashSet<>(sortedVertices.subList(0, m + 1));
        Set<Integer> independentSet = new HashSet<>(sortedVertices.subList(m + 1, sortedVertices.size()));
        
        // Build adjacency list
        Map<Integer, Set<Integer>> adjList = new HashMap<>();
        for (int v : sortedVertices) {
            adjList.put(v, new HashSet<>());
        }
        for (ColEdge edge : edges) {
            adjList.get(edge.u).add(edge.v);
            adjList.get(edge.v).add(edge.u);
        }
        
        // Check clique property
        for (int u : clique) {
            for (int v : clique) {
                if (u != v && !adjList.get(u).contains(v)) {
                    return null; // Not a clique
                }
            }
        }
        
        // Check independent set property
        for (int u : independentSet) {
            for (int v : independentSet) {
                if (u != v && adjList.get(u).contains(v)) {
                    return null; // Not an independent set
                }
            }
        }
        
        // Assigns unique colors to vertices in the clique
        Map<Integer, Integer> colorMap = new HashMap<>();
        int cliqueSize = m + 1;
        for (int i = 0; i < cliqueSize; i++) {
            colorMap.put(sortedVertices.get(i), i + 1);
        }
        
        // Assigns the same color to all vertices in the independent set
        for (int i = cliqueSize; i < sortedVertices.size(); i++) {
            colorMap.put(sortedVertices.get(i), cliqueSize);
        }
        
        // Returns the result with chromatic number equal to clique size
        return new ColoringResult(colorMap, cliqueSize, "Split Graph Coloring");
    }
}