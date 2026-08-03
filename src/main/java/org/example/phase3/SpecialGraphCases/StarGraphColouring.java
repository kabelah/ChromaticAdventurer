package org.example.phase3.SpecialGraphCases;

import java.util.*;
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;

public class StarGraphColouring {
    
    /**
     * Colors a star graph with two colors.
     * The central node is assigned color 1.
     * All other nodes are assigned color 2.
     * 
     * @param graph The graph to color
     * @return ColoringResult containing the color assignments and chromatic number
     */
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Find the central node (the one with highest degree)
        Map<Integer, Integer> degrees = new HashMap<>();
        
        // Calculate degrees of all vertices
        for (ColEdge edge : edges) {
            degrees.merge(edge.u, 1, Integer::sum);
            degrees.merge(edge.v, 1, Integer::sum);
        }
        
        // Find the vertex with maximum degree (central node)
        int centralNode = degrees.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(1);
        
        System.out.println("[DEBUG] Star Graph - Central node: " + centralNode);
        System.out.println("[DEBUG] Star Graph - Vertex degrees: " + degrees);
        
        // Create color assignments map
        Map<Integer, Integer> colorMap = new HashMap<>();
        
        // Color central node with color 1
        colorMap.put(centralNode, 1);
        
        // Color all other nodes with color 2
        for (int i = 1; i <= vertexCount; i++) {
            if (i != centralNode) {
                colorMap.put(i, 2);
            }
        }
        
        return new ColoringResult(colorMap, 2, "Star Graph Coloring");
    }
}
