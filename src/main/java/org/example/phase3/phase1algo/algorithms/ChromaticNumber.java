package org.example.phase3.phase1algo.algorithms;

import org.example.phase3.phase1algo.structures.Graph;
import org.example.phase3.phase1algo.utils.HelpFunctions;
import org.example.phase3.phase1algo.utils.VertexOrdering;
import java.util.*;

/**
 * Class whose functions are different algorithms approximating the chromatic number of a graph.
 */
public class ChromaticNumber {
    /**
     * Uses the DSATUR algorithm to approximate the chromatic number of a graph.
     *
     * @param graph The analyzed Graph object.
     */
    public static void DSATUR(Graph graph) {
        int[] coloring = new int[graph.getOrder()];
        Arrays.fill(coloring, -1);

        int[] degrees = new int[graph.getOrder()];
        for (int i = 0; i < graph.getOrder(); i++)
            degrees[i] = graph.getAdjacencyList()[i].size();

        int[] saturationDegrees = new int[graph.getOrder()];

        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (a, b) -> {
                    if (saturationDegrees[a] != saturationDegrees[b]) {
                        return Integer.compare(saturationDegrees[b], saturationDegrees[a]);
                    } else {
                        return Integer.compare(degrees[b], degrees[a]);
                    }
                }
        );

        for (int i = 0; i < graph.getOrder(); i++)
            pq.offer(i);

        while (!pq.isEmpty()) {
            int vertex = pq.poll();
            boolean[] usedColors = new boolean[graph.getOrder()];

            for (int neighbor : graph.getAdjacencyList()[vertex]) {
                if (coloring[neighbor] != -1) {
                    usedColors[coloring[neighbor]] = true;
                }
            }

            int color;
            for (color = 0; color < usedColors.length; color++)
                if (!usedColors[color])
                    break;

            coloring[vertex] = color;

            for (int neighbor : graph.getAdjacencyList()[vertex])
                if (coloring[neighbor] == -1) {
                    Set<Integer> uncolored = new HashSet<>();
                    for (int newNeighbor : graph.getAdjacencyList()[neighbor])
                        if (coloring[newNeighbor] != -1)
                            uncolored.add(coloring[newNeighbor]);

                    saturationDegrees[neighbor] = uncolored.size();
                }


            PriorityQueue<Integer> newQueue = new PriorityQueue<>(
                    (a, b) -> {
                        if (saturationDegrees[a] != saturationDegrees[b]) {
                            return Integer.compare(saturationDegrees[b], saturationDegrees[a]);
                        } else
                            return Integer.compare(degrees[b], degrees[a]);
                    }
            );

            for (int i = 0; i < graph.getOrder(); i++)
                if (coloring[i] == -1)
                    newQueue.offer(i);

            pq = newQueue;
        }

        int chromaticNumber = HelpFunctions.maxInArray(coloring) + 1;
        if (graph.getChromaticNumber() != -1 && graph.getChromaticNumber() > chromaticNumber) {
            graph.setChromaticNumber(chromaticNumber);
            graph.setColoring(coloring);
        } else if (graph.getChromaticNumber() == -1) {
            graph.setChromaticNumber(chromaticNumber);
            graph.setColoring(coloring);
        }
    }

//    /**
//     * Uses the backtracking algorithm to find the exact chromatic number of a graph.
//     *
//     * @param graph The analyzed Graph object.
//     * @return The coloring array of the graph.
//     */
//    public static int[] Backtracking(Graph graph) {
//        return new int[0];
//    }
}
