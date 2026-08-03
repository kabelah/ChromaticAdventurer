package org.example.phase3.Evaluation;

import org.example.phase3.Generation.ColEdge;

import java.util.List;

/**
 * A simple Graph class that:
 *  - Stores a list of edges.
 *  - Computes 'vertexCount' by finding the maximum-labeled vertex.
 */
public class Graph {
    private List<ColEdge> edges;
    private int vertexCount;  // The highest-labeled vertex in the edge list

    public Graph(List<ColEdge> edges) {
        this.edges = edges;
        this.vertexCount = findMaxVertex(edges);
    }

    private int findMaxVertex(List<ColEdge> edgeList) {
        int max = 0;
        for (ColEdge e : edgeList) {
            if (e.u > max) max = e.u;
            if (e.v > max) max = e.v;
        }
        return max;
    }

    public List<ColEdge> getEdges() {
        return edges;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    
}