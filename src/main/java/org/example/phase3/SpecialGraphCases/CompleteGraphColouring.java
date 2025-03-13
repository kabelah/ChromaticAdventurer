package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.*;

public class CompleteGraphColouring {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // For a complete graph, number of edges should be n(n-1)/2
        int expectedEdges = (vertexCount * (vertexCount - 1)) / 2;
        if (edges.size() != expectedEdges) {
            throw new IllegalArgumentException("Not a complete graph: incorrect number of edges");
        }
        
        // Build adjacency list to verify completeness
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            adjList.add(new ArrayList<>());
        }
        
        for (ColEdge edge : edges) {
            adjList.get(edge.u - 1).add(edge.v - 1);
            adjList.get(edge.v - 1).add(edge.u - 1);
        }
        
        // Verify each vertex is connected to all others
        for (List<Integer> neighbors : adjList) {
            if (neighbors.size() != vertexCount - 1) {
                throw new IllegalArgumentException("Not a complete graph: vertex with incorrect degree");
            }
        }
        
        // For complete graphs, each vertex needs a different color
        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int i = 0; i < vertexCount; i++) {
            colorMap.put(i + 1, i + 1);
        }
        
        return new ColoringResult(colorMap, vertexCount, "Complete Graph Coloring");
    }
}
