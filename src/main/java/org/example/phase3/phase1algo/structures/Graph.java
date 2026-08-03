package org.example.phase3.phase1algo.structures;

import org.example.phase3.phase1algo.data.ReadGraph;
import org.example.phase3.phase1algo.utils.HelpFunctions;
import java.util.*;

/**
 * Represents a graph structure with various properties and characteristics.
 * Stores the graph's order, matrix representation, edge list, and various
 * computed properties such as bounds and chromatic number.
 */
public class Graph {
    private final int ORDER;
    private final int[][] matrix;
    private final int[] edgeList;
    private List<Set<Integer>> subgraphs;
    private int lowerBound;
    private int upperBound;
    private int chromaticNumber;
    private List<Integer>[] adjacencyList;
    private int[] coloring;

    /**
     * Constructs a new Graph with the specified parameters.
     * Initializes the graph structure and converts the edge list to an adjacency list.
     *
     * @param order The number of vertices in the graph
     * @param matrix The adjacency matrix representation
     * @param edgeList The list of edges in the graph
     */
    public Graph(int order, int[][] matrix, int[] edgeList) {
        this.ORDER = order;
        this.matrix = matrix;
        this.edgeList = edgeList;
        this.lowerBound = -1;
        this.upperBound = -1;
        this.chromaticNumber = -1;
        // Convert edgeList to proper format for adjacencyList initialization
        int[][] edgeArray = new int[edgeList.length/2][2];
        for (int i = 0; i < edgeList.length; i += 2) {
            edgeArray[i/2][0] = edgeList[i];
            edgeArray[i/2][1] = edgeList[i + 1];
        }
        this.adjacencyList = HelpFunctions.edgeListToAdjacencyList(edgeArray, order);
        this.coloring = new int[order];
    }

    public int getOrder() {
        return ORDER;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[] getEdgeList() {
        return edgeList;
    }

    public List<Set<Integer>> getSubgraphs() {
        return subgraphs;
    }

    public void setSubgraphs(List<Set<Integer>> subgraphs) {
        this.subgraphs = subgraphs;
    }

    public void setSubgraphs(Set<Set<Integer>> subgraphs) {
        this.subgraphs = new ArrayList<>(subgraphs);
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getChromaticNumber() {
        return chromaticNumber;
    }

    public void setChromaticNumber(int chromaticNumber) {
        this.chromaticNumber = chromaticNumber;
    }

    public List<Integer>[] getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(List<Integer>[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int[] getColoring() {
        return coloring;
    }

    public void setColoring(int[] coloring) {
        this.coloring = coloring;
    }

    public double getDensity() {
        int edges = edgeList.length / 2;
        return (2.0 * edges) / (ORDER * (ORDER - 1));
    }
}
