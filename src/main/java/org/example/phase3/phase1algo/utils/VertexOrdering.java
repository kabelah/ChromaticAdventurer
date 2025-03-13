package org.example.phase3.phase1algo.utils;

import org.example.phase3.phase1algo.structures.Graph;
import java.util.*;

/**
 * Class with methods used for ordering vertices of a graph in certain ways.
 */
public class VertexOrdering {
    /**
     * @param graph The analyzed Graph object.
     * @return Array of vertices ordered from highest to lowest number of adjacent vertices.
     */
    public static int[] descendingDegree(Graph graph) {
        class Pair {
            final Integer vertex;
            final Integer degree;

            public Pair(Integer vertex, Integer degree) {
                this.vertex = vertex;
                this.degree = degree;
            }
        }

        List<Pair> pairs = new ArrayList<>();
        for (int vertex = 0; vertex < graph.getOrder(); vertex++)
            pairs.add(new Pair(vertex, graph.getAdjacencyList()[vertex].size()));

        pairs.sort((pair1, pair2) -> pair2.degree.compareTo(pair1.degree));

        int[] sortedVertices = new int[graph.getOrder()];
        for (int i = 0; i < graph.getOrder(); i++)
            sortedVertices[i] = pairs.get(i).vertex;

        return sortedVertices;
    }
}
