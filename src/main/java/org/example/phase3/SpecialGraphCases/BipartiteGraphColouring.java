package org.example.phase3.SpecialGraphCases;

import org.example.phase3.Generation.ColEdge;
import org.example.phase3.Evaluation.Graph;
import org.example.phase3.Algorithms.ColoringResult;
import java.util.*;

public class BipartiteGraphColouring {
    public static ColoringResult colour(Graph graph) {
        List<ColEdge> edges = graph.getEdges();
        int numNodes = graph.getVertexCount();
        int[] colourArray = new int[numNodes];
        
        colourArray = colourBipartiteGraph(edges, colourArray);
        
        // Convert the result to a map
        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int i = 0; i < colourArray.length; i++) {
            colorMap.put(i + 1, colourArray[i]);
        }
        
        return new ColoringResult(colorMap, 2, "Bipartite Coloring");
    }

    /**
     * colours a bipartite graph with two colours (1 and 2).
     * 
     * @param edges       A list of edges representing the bipartite graph.
     * @param colourArray An array to store the colours of each node.
     * @return The updated colourArray with the assigned colours.
     */
    public static int[] colourBipartiteGraph(List<ColEdge> edges, int[] colourArray) {
        int numNodes = getNumNodes(edges);

        // Build adjacency list from edge list
        List<List<Integer>> adjList = buildAdjacencyList(edges, numNodes);
    
        // Try to colour each connected node
        for (int start = 0; start < numNodes; start++) {
            if (colourArray[start] == 0) { // If the node is uncoloured
                colourArray[start] = 1; // Start colouring with 1
                colouring(start, adjList, colourArray);
            }
        }
    
        return colourArray;
    }
    
    /**
     * Recursive function to colour the graph.
     * 
     * @param currentNode the current node whos neigbours are to be coloured
     * @param adjList the adjacency list that shows which nodes each node is connected to
     * @param colourArray the array where the colours for each node are stored
     */
    private static void colouring(int currentNode, List<List<Integer>> adjList, int[] colourArray) {
        for (int neighbor : adjList.get(currentNode)) {
            if (colourArray[neighbor] == 0) {
                // Assign the opposite colour to the neighbor
                if (colourArray[currentNode] == 1) {
                    colourArray[neighbor] = 2;
                } else {
                    colourArray[neighbor] = 1;
                }                
    
                // Recursively colour the neighbor's neighbors
                colouring(neighbor, adjList, colourArray);
            }
        }
    }
    
    /**
     * Builds an adjacency list from the given edge list, storing what nodes each node is connected to.
     * 
     * @param edges    A list of edges representing the graph.
     * @param numNodes The total number of nodes in the graph.
     * @return The adjacency list representation of the graph.
     */
    private static List<List<Integer>> buildAdjacencyList(List<ColEdge> edges, int numNodes) {
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<>());
        }
    
        for (ColEdge edge : edges) {
            adjList.get(edge.u - 1).add(edge.v - 1);
            adjList.get(edge.v - 1).add(edge.u - 1); // Add a connection for both nodes
        }
    
        return adjList;
    }

    /**
     * Determines the number of nodes in the graph.
     * 
     * @param edges A list of edges representing the graph
     * @return The total number of nodes in the graph
     */
    public static int getNumNodes(List<ColEdge> edges) {
        int max = 0;
        for (ColEdge edge : edges) {
            if (edge.v > max) {
                max = edge.v;
            }
            if (edge.u > max) {
                max = edge.u;
            }
        }

        return max;
    }
}
