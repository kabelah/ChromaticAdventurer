package org.example.phase3.DefaultGraphCases;

import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Generation.ColEdge;
import java.util.*;

public class DSATUR {
    /**
     * Uses the DSATUR algorithm to approximate the chromatic number of a graph.
     */
    public static Map<Integer, Integer> DSATUR(Graph graph) {
        // Build adjacency list locally from the edges
        List<Set<Integer>> adjacencyList = new ArrayList<>();
        int vertexCount = graph.getVertexCount();
        
        // Initialize adjacency lists (add one extra for 1-based indexing)
        for (int i = 0; i <= vertexCount; i++) {
            adjacencyList.add(new HashSet<>());
        }
        
        // Populate adjacency lists from edges (using 1-based vertex numbers)
        for (ColEdge edge : graph.getEdges()) {
            adjacencyList.get(edge.u).add(edge.v);
            adjacencyList.get(edge.v).add(edge.u);
        }

        // Initialize coloring array
        int[] coloring = new int[vertexCount + 1];  // +1 for 1-based indexing
        Arrays.fill(coloring, -1);

        // Calculate initial degrees
        int[] degrees = new int[vertexCount + 1];  // +1 for 1-based indexing
        for (int i = 1; i <= vertexCount; i++) {  // Start from 1
            degrees[i] = adjacencyList.get(i).size();
        }

        int[] saturationDegrees = new int[vertexCount + 1];  // +1 for 1-based indexing

        // Create priority queue with custom comparator
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (a, b) -> {
                    if (saturationDegrees[a] != saturationDegrees[b]) {
                        return Integer.compare(saturationDegrees[b], saturationDegrees[a]);
                    } else {
                        return Integer.compare(degrees[b], degrees[a]);
                    }
                }
        );

        for (int i = 1; i <= vertexCount; i++) {  // Start from 1
            pq.offer(i);
        }

        while (!pq.isEmpty()) {
            int vertex = pq.poll();
            boolean[] usedColors = new boolean[vertexCount + 1];  // +1 for potential colors

            for (int neighbor : adjacencyList.get(vertex)) {
                if (coloring[neighbor] != -1) {
                    usedColors[coloring[neighbor]] = true;
                }
            }

            int color;
            for (color = 1; color < usedColors.length; color++) {  // Start from 1
                if (!usedColors[color]) break;
            }

            coloring[vertex] = color;

            // Update saturation degrees of uncolored neighbors
            for (int neighbor : adjacencyList.get(vertex)) {
                if (coloring[neighbor] == -1) {
                    Set<Integer> neighborColors = new HashSet<>();
                    for (int newNeighbor : adjacencyList.get(neighbor)) {
                        if (coloring[newNeighbor] != -1) {
                            neighborColors.add(coloring[newNeighbor]);
                        }
                    }
                    saturationDegrees[neighbor] = neighborColors.size();
                }
            }

            // Rebuild queue with updated priorities
            PriorityQueue<Integer> newQueue = new PriorityQueue<>(
                    (a, b) -> {
                        if (saturationDegrees[a] != saturationDegrees[b]) {
                            return Integer.compare(saturationDegrees[b], saturationDegrees[a]);
                        } else {
                            return Integer.compare(degrees[b], degrees[a]);
                        }
                    }
            );

            for (int i = 1; i <= vertexCount; i++) {  // Start from 1
                if (coloring[i] == -1) {
                    newQueue.offer(i);
                }
            }

            pq = newQueue;
        }

        // Convert array to map for return value
        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int i = 1; i <= vertexCount; i++) {  // Start from 1
            colorMap.put(i, coloring[i]);
        }

        return colorMap;
    }
}
