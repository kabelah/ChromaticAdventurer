package org.example.phase3.SpecialGraphCases;

import java.util.*;
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;

/**
 * Specialized algorithm for coloring forest graphs.
 * A forest is a collection of trees, and like trees, can be colored with 2 colors.
 */
public class ForestGraphColouring {
    /**
     * Colors a forest graph using exactly 2 colors.
     * Processes each tree component separately.
     *
     * @param graph The forest graph to be colored
     * @return ColoringResult containing the vertex color assignments and chromatic number
     */
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        Map<Integer, Integer> colorMap = new HashMap<>();
        boolean[] visited = new boolean[vertexCount + 1];
        
        // Process each component (tree) in the forest
        for (int i = 1; i <= vertexCount; i++) {
            if (!visited[i]) {
                colorComponent(i, edges, colorMap, visited);
            }
        }
        
        return new ColoringResult(colorMap, 2, "Forest Coloring");
    }
    
    /**
     * Colors a single connected component (tree) in the forest.
     * Uses breadth-first search to alternate colors between adjacent vertices.
     *
     * @param start Starting vertex for the component
     * @param edges List of edges in the graph
     * @param colorMap Map to store vertex colors
     * @param visited Array to track visited vertices
     */
    private static void colorComponent(int start, List<ColEdge> edges, Map<Integer, Integer> colorMap, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        colorMap.put(start, 1);  // Start with color 1
        visited[start] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentColor = colorMap.get(current);
            int nextColor = (currentColor == 1) ? 2 : 1;  // Alternate between 1 and 2
            
            // Find and color all neighbors
            for (ColEdge edge : edges) {
                int neighbor = -1;
                if (edge.u == current) neighbor = edge.v;
                if (edge.v == current) neighbor = edge.u;
                
                if (neighbor != -1 && !visited[neighbor]) {
                    colorMap.put(neighbor, nextColor);
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }
}
