package org.example.phase3.SpecialGraphCases;

import java.util.*;
import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;

public class PlanarGraphSolver {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int vertexCount = graph.getVertexCount();
        
        // Create color assignments map
        Map<Integer, Integer> colorMap = new HashMap<>();
        boolean[] usedColors = new boolean[vertexCount]; // Track colors used by neighbors
        
        // Loop through vertices and assign the smallest available color
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            Arrays.fill(usedColors, false);
            
            // Mark colors used by neighbors
            for (ColEdge edge : edges) {
                // Adjust for 1-based indices in ColEdge
                if (edge.u - 1 == vertex && colorMap.containsKey(edge.v)) {
                    usedColors[colorMap.get(edge.v)] = true;
                }
                if (edge.v - 1 == vertex && colorMap.containsKey(edge.u)) {
                    usedColors[colorMap.get(edge.u)] = true;
                }
            }
            
            // Assign the smallest available color
            for (int color = 0; color < vertexCount; color++) {
                if (!usedColors[color]) {
                    colorMap.put(vertex + 1, color);
                    break;
                }
            }
        }
        
        // Determine the chromatic number (maximum color used + 1)
        int chromaticNumber = colorMap.values().stream().mapToInt(Integer::intValue).max().orElse(-1) + 1;
        
        return new ColoringResult(colorMap, chromaticNumber, "Greedy (Planar)");
    }
}