package org.example.realphase2.ChromaticNumber.org.phase1.structures;


import org.example.realphase2.ChromaticNumber.org.phase1.data.ReadGraph;
import org.example.realphase2.ChromaticNumber.org.phase1.utils.HelpFunctions;

import java.util.ArrayList;
import java.util.Set;

/**
 * Class whose objects represent a graph with various structures simplifying its analysis.
 */
public class Graph {
    private final int ORDER;
    private final int[][] EDGE_LIST;
    private final int[][] MATRIX;
    private final ArrayList<Integer>[] ADJACENCY_LIST;
    private int[] coloring;
    private Set<Set<Integer>> subgraphs;
    private int lowerBound;
    private int upperBound;
    private int chromaticNumber;
    private final double DENSITY;


    /**
     * Constructor for loading a Graph object from a .txt file.
     *
     * @param graphText The graph as a string.
     */
    public Graph(String graphText) {
        int[][] readResults = ReadGraph.read(graphText);
        ORDER = readResults[0][0];
        EDGE_LIST = new int[readResults.length - 1][2];
        for (int i = 0; i < EDGE_LIST.length; i++) {
            EDGE_LIST[i][0] = readResults[i + 1][0];
            EDGE_LIST[i][1] = readResults[i + 1][1];
        }
        ADJACENCY_LIST = HelpFunctions.edgeListToAdjacencyList(getEdgeList(), getOrder());
        MATRIX = HelpFunctions.adjacencyListToMatrix(getAdjacencyList());

        DENSITY = (double) (2 * EDGE_LIST.length) / (ORDER * (ORDER - 1));
        chromaticNumber = -1;
    }

    /**
     * Constructor for creating a graph object from another Graph objects subgraph.
     *
     * @param graph    Original Graph object.
     * @param subgraph Set containing indexes of vertices to be included in new Graph object.
     */
    public Graph(Graph graph, Set<Integer> subgraph) {
        ORDER = subgraph.size();


        EDGE_LIST = new int[0][0];
        MATRIX = new int[0][0];
        ADJACENCY_LIST = new ArrayList[0];
        DENSITY = (double) (2 * EDGE_LIST.length) / ORDER * (ORDER - 1);
    }


// ----- GETTERS -----

    /**
     * Returns the number of vertices in the graph.
     */
    public int getOrder() {
        return ORDER;
    }

    public int[][] getEdgeList() {
        return EDGE_LIST;
    }

    public int[][] getMatrix() {
        return MATRIX;
    }

    public ArrayList<Integer>[] getAdjacencyList() {
        return ADJACENCY_LIST;
    }

    public int[] getColoring() {
        return coloring;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public Set<Set<Integer>> getSubgraphs() {
        return subgraphs;
    }

    public int getChromaticNumber() {
        return chromaticNumber;
    }

    public double getDensity() {
        return DENSITY;
    }

    // ----- SETTERS -----

    public void setColoring(int[] coloring) {
        this.coloring = coloring;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public void setChromaticNumber(int chromaticNumber) {
        this.chromaticNumber = chromaticNumber;
    }

    public void setSubgraphs(Set<Set<Integer>> subgraphs) {
        this.subgraphs = subgraphs;
    }
}
