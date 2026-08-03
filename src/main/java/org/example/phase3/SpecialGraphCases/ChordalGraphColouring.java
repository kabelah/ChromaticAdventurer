package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.*;

public class ChordalGraphColouring {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Build adjacency list
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i <= vertexCount; i++) {
            adjList.add(new ArrayList<>());
        }
        
        for (ColEdge edge : edges) {
            adjList.get(edge.u).add(edge.v);
            adjList.get(edge.v).add(edge.u);
        }
        
        // Get perfect elimination ordering
        List<Integer> vertices = getPerfectEliminationOrdering(adjList, vertexCount);
        
        // Color the vertices using the perfect elimination ordering
        Map<Integer, Integer> colorMap = new HashMap<>();
        int maxColor = 0;
        
        for (int vertex : vertices) {
            // Find used colors from neighbors
            boolean[] usedColors = new boolean[vertexCount + 1];
            for (int neighbor : adjList.get(vertex)) {
                if (colorMap.containsKey(neighbor)) {
                    usedColors[colorMap.get(neighbor)] = true;
                }
            }
            
            // Assign the smallest available color
            int color = 1;
            while (usedColors[color]) {
                color++;
            }
            colorMap.put(vertex, color);
            maxColor = Math.max(maxColor, color);
        }
        
        return new ColoringResult(colorMap, maxColor, "Chordal Graph Coloring");
    }
    
    private static List<Integer> getPerfectEliminationOrdering(List<List<Integer>> adjList, int vertexCount) {
        List<Integer> ordering = new ArrayList<>();
        boolean[] visited = new boolean[vertexCount + 1];
        
        // Simple elimination ordering for now - can be improved with maximum cardinality search
        for (int i = 1; i <= vertexCount; i++) {
            if (!visited[i]) {
                ordering.add(i);
                visited[i] = true;
                
                // Add neighbors in order of their degree
                PriorityQueue<Integer> neighbors = new PriorityQueue<>(
                    (a, b) -> Integer.compare(adjList.get(b).size(), adjList.get(a).size())
                );
                neighbors.addAll(adjList.get(i));
                
                while (!neighbors.isEmpty()) {
                    int next = neighbors.poll();
                    if (!visited[next]) {
                        ordering.add(next);
                        visited[next] = true;
                        for (int neighbor : adjList.get(next)) {
                            if (!visited[neighbor]) {
                                neighbors.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        
        return ordering;
    }
}
