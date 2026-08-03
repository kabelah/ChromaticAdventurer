package org.example.phase3.SpecialGraphCases;

import java.util.*;
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;

public class WheelGraphSolver {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Determine if the graph is a wheel
        int centralVertex = findCentralVertex(edges, vertexCount);
        int cycleVertexCount = vertexCount - 1; // Exclude the central vertex
        
        // Create color assignments map
        Map<Integer, Integer> colorMap = new HashMap<>();
        
        // Color the cycle vertices
        for (int i = 1; i <= cycleVertexCount; i++) {
            if (i == centralVertex) continue;
            colorMap.put(i, ((i - 1) % 2) + 1); // Alternate between 1 and 2
        }
        
        // Color the central vertex
        colorMap.put(centralVertex, 3); // Use color 3 for the center
        
        // If cycle is even, use a fourth color where necessary
        boolean isEvenCycle = (cycleVertexCount % 2 == 0);
        if (isEvenCycle) {
            for (int i = 1; i <= cycleVertexCount; i++) {
                if (i == centralVertex) continue;
                int nextVertex = (i % cycleVertexCount) + 1;
                if (nextVertex == centralVertex) nextVertex = 1;
                
                if (colorMap.get(i).equals(colorMap.get(nextVertex))) {
                    colorMap.put(i, 4); // Assign color 4
                }
            }
        }
        
        int chromaticNumber = isEvenCycle ? 4 : 3;
        return new ColoringResult(colorMap, chromaticNumber, "Wheel Graph Coloring");
    }

    private static int findCentralVertex(List<ColEdge> edges, int vertexCount) {
        // Count degrees of each vertex
        int[] degrees = new int[vertexCount + 1];
        for (ColEdge edge : edges) {
            degrees[edge.u]++;
            degrees[edge.v]++;
        }
        
        // Find vertex with degree V-1 (connected to all others)
        for (int i = 1; i <= vertexCount; i++) {
            if (degrees[i] == vertexCount - 1) {
                return i;
            }
        }
        
        throw new IllegalArgumentException("Not a wheel graph: no central vertex found");
    }
}