package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.*;

/**
 * Specialized algorithm for coloring cycle graphs.
 * Even cycles can be colored with 2 colors, while odd cycles require 3 colors.
 */
public class CycleGraphColouring {
    /**
     * Colors a cycle graph using either 2 or 3 colors depending on the cycle length.
     * 
     * @param graph The cycle graph to be colored
     * @return ColoringResult containing the vertex color assignments and chromatic number
     * @throws IllegalArgumentException if the input graph is not a valid cycle
     */
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Validate cycle properties
        if (edges.size() != vertexCount) {
            throw new IllegalArgumentException("Not a cycle graph: number of edges != number of vertices");
        }
        
        // Build adjacency list for validation
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            adjList.add(new ArrayList<>());
        }
        
        // Populate adjacency list
        for (ColEdge edge : edges) {
            adjList.get(edge.u - 1).add(edge.v - 1);
            adjList.get(edge.v - 1).add(edge.u - 1);
        }
        
        // Verify cycle property (each vertex must have exactly 2 neighbors)
        for (List<Integer> neighbors : adjList) {
            if (neighbors.size() != 2) {
                throw new IllegalArgumentException("Not a cycle graph: vertex with degree != 2");
            }
        }
        
        Map<Integer, Integer> colorMap = new HashMap<>();
        int chromaticNumber;
        
        // Color based on whether cycle length is even or odd
        if (vertexCount % 2 == 0) {
            // Even cycle: 2-colorable
            for (int i = 0; i < vertexCount; i++) {
                colorMap.put(i + 1, (i % 2) + 1);
            }
            chromaticNumber = 2;
        } else {
            // Odd cycle: requires 3 colors
            for (int i = 0; i < vertexCount - 1; i++) {
                colorMap.put(i + 1, (i % 2) + 1);
            }
            colorMap.put(vertexCount, 3); // Last vertex needs a third color
            chromaticNumber = 3;
        }
        
        return new ColoringResult(colorMap, chromaticNumber, "Cycle Graph Coloring");
    }
}
