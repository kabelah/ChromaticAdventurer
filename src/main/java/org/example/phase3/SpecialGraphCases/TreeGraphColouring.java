package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Specialized algorithm for coloring tree graphs.
 * Trees are bipartite graphs that can always be colored with 2 colors.
 */
public class TreeGraphColouring {
    /**
     * Colors a tree graph using exactly 2 colors.
     * 
     * @param graph The tree graph to be colored
     * @return ColoringResult containing the vertex color assignments and chromatic number
     */
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Handle single vertex case
        if (edges.isEmpty() && vertexCount == 1) {
            Map<Integer, Integer> colorMap = new HashMap<>();
            colorMap.put(1, 1);  // Color the single vertex with color 1
            return new ColoringResult(colorMap, 2, "Tree Coloring");
        }
        
        int[] colours = colourTreeGraph(graph.getEdges());
        Map<Integer, Integer> colourMap = new HashMap<>();
        
        // Debug logging for verification
        System.out.println("\n[DEBUG] Tree Coloring Details:");
        System.out.println("Number of vertices: " + colours.length);
        
        // Convert array-based colors to map-based representation
        for (int i = 0; i < colours.length; i++) {
            colourMap.put(i + 1, colours[i]);  // Using i+1 to match 1-based vertex numbering
            System.out.println("Vertex " + (i + 1) + " -> Color " + colours[i]);
        }
        
        System.out.println("Final color map: " + colourMap);
        
        return new ColoringResult(colourMap, 2, "Tree Coloring");
    }

    public static int[] colourTreeGraph(List<ColEdge> edges) {
        if (edges.isEmpty()) {
            return new int[]{1};  // Single vertex case
        }
        
        int numberOfVertices = 0;
        
        // Find the maximum vertex number to determine array size
        for (ColEdge edge : edges) {
            numberOfVertices = Math.max(numberOfVertices, Math.max(edge.u, edge.v));
        }

        System.out.println("[DEBUG] Tree Graph Details:");
        System.out.println("Number of edges: " + edges.size());
        System.out.println("Max vertex number found: " + numberOfVertices);
        System.out.println("Edges: " + edges);

        // Build adjacency list (add one extra for 1-based indexing)
        List<List<Integer>> adj = new ArrayList<>(numberOfVertices + 1);
        for (int i = 0; i <= numberOfVertices; i++) {
            adj.add(new ArrayList<>());
        }
        for (ColEdge edge : edges) {
            adj.get(edge.u).add(edge.v);
            adj.get(edge.v).add(edge.u);
        }

        // Initialize colors array with -1 (uncolored)
        int[] colors = new int[numberOfVertices];
        Arrays.fill(colors, -1);

        // Start BFS from vertex 1
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);  // Start with vertex 1
        colors[0] = 1;  // Color first vertex with 1

        // Process vertices in BFS order
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentColor = colors[current - 1];  // -1 for 0-based array

            // Color all neighbors with the opposite color
            for (int neighbor : adj.get(current)) {
                if (colors[neighbor - 1] == -1) {  // -1 for 0-based array
                    colors[neighbor - 1] = (currentColor == 1) ? 2 : 1;
                    queue.add(neighbor);
                }
            }
        }

        return colors;
    }
}
