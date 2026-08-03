package org.example.phase3.Generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGraphGenerator {

    /**
 * Generates a list of edges to form a random graph with the specified number
 * of vertices and edges.
 *
 * @param numVertices The number of vertices in the graph.
 * @param numEdges    The total number of edges in the graph.
 * @return A list of ColEdge representing the edges of the graph.
 * @throws IllegalArgumentException If the number of vertices or edges is invalid
 */
    public static List<ColEdge> generateEdges(int numVertices, int numEdges) {
        // e will contain the edges of the graph.
        List<ColEdge> edges = new ArrayList<>();

        if (numVertices < 0) {
            GraphDisplayManager.displayError("Number of vertices must be at least 0.");
            throw new IllegalArgumentException("Number of vertices must be at least 0.");
        }
        if (numEdges < 0 || numEdges > ((numVertices * (numVertices - 1)) / 2)) {
            GraphDisplayManager.displayError("Invalid number of edges for the given number of vertices.");
            throw new IllegalArgumentException("Invalid number of edges for the given number of vertices.");
        }
        if (numEdges < numVertices - 1) {
            GraphDisplayManager.displayError("Not enough edges to connect every vertex.");
            throw new IllegalArgumentException("Not enough edges to connect every vertex.");
        }

        Random randomVertex = new Random();
        List<Integer> disconnectedVertices = new ArrayList<>();
        List<Integer> connectedVertices = new ArrayList<>();

        // Creates a list of every vertex to keep track of disconnected vertices.
        for (int i = 0; i < numVertices; i++) {
            disconnectedVertices.add(i+1);
        }

        // Adds the first vertex to the connection list. This vertex will be the first vertex to have an edge.
        int firstConnectedVertex = randomVertex.nextInt(numVertices);
        connectedVertices.add(disconnectedVertices.get(firstConnectedVertex));
        disconnectedVertices.remove(firstConnectedVertex);
        // Creates a connected graph using the data store in the list of connected/visited vertices and the
        // list of disconnected/unused vertices.
        for (int i = 1; i < numVertices; i++) {
            int secondVertexPosition = randomVertex.nextInt(disconnectedVertices.size());
            int firstVertex = connectedVertices.get(randomVertex.nextInt(connectedVertices.size()));
            int secondVertex = disconnectedVertices.get(secondVertexPosition);
            edges.add(new ColEdge(firstVertex, secondVertex));
            connectedVertices.add(disconnectedVertices.get(secondVertexPosition));
            disconnectedVertices.remove(secondVertexPosition);
        }


        // If all the edges have not been created already, then new ones will be made with random vertices.
        if (numEdges != numVertices - 1) {
            for (int i = numVertices - 1; i < numEdges; i++) {
                boolean duplicate = true;

                // This while loop will reroll the random vertices if a duplicate is found.
                // This helps to prevent duplicate edges.
                while (duplicate) {
                    int u = randomVertex.nextInt(numVertices) + 1;
                    int v = randomVertex.nextInt(numVertices) + 1;

                    // If both vertices are the same, then an edge cannot be created
                    // as a vertex cannot share an edge with itself.
                    if (u != v && !containsDuplicate(u, v, edges)) {
                        edges.add(new ColEdge(u, v));
                        duplicate = false;
                    }
                }
            }
        }
        return edges;
    }


    /**
     * Checks whether a pair of vertices (u, v) are already connected by an edge
     * in the given list of edges.
     *
     * @param u     The first vertex.
     * @param v     The second vertex.
     * @param edges The list of edges to check for duplicates.
     * @return true if an edge between u and v already exists, false otherwise.
     */
    public static boolean containsDuplicate(int u, int v, List<ColEdge> edges) {
        for (ColEdge edge : edges) {
            // Checks if the edge already exists.
            if ((edge.u == u && edge.v == v) || (edge.u == v && edge.v == u)) {
                return true;
            }
        }
        return false;
    }
}