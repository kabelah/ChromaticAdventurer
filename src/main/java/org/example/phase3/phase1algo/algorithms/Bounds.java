package org.example.phase3.phase1algo.algorithms;

import org.example.phase3.phase1algo.structures.Graph;
import org.example.phase3.phase1algo.utils.HelpFunctions;
import org.example.phase3.phase1algo.utils.VertexOrdering;
import java.util.*;

/**
 * Class whose functions help define bounds on the chromatic number of a graph.
 */
public class Bounds {
    /**
     * Calculates and sets the Graph object's upperBound field.
     * Determines it using the greedy algorithm on vertices ordered by descending degree.
     *
     * @param graph The analyzed Graph object.
     */
    public static void upperBoundGreedy(Graph graph) {
        int[] coloring = new int[graph.getOrder()];
        Arrays.fill(coloring, -1);

        int[] vertexOrder = VertexOrdering.descendingDegree(graph);

        for (int i : vertexOrder) {
            List<Integer> adjacentColors = new ArrayList<>();
            for (int vertex : graph.getAdjacencyList()[i])
                if (coloring[vertex] != -1)
                    adjacentColors.add(coloring[vertex]);

            for (int j = 0; j < graph.getOrder(); j++)
                if (!adjacentColors.contains(j)) {
                    coloring[i] = j;
                    break;
                }
        }

        graph.setColoring(coloring);
        graph.setUpperBound(HelpFunctions.maxInArray(coloring) + 1);
    }

    public static class LowerBound{
        private static ArrayList<ArrayList<Integer>> cliques;

        /**
         * Calculates and sets the Graph object's lowerBound field.
         * Determines it by finding the size of the largest clique.
         *
         * @param graph The analyzed Graph object.
         */
        public static void lowerBoundLargestClique(Graph graph) {
            ArrayList<Integer> consideredVertices = new ArrayList<>();
            cliques = new ArrayList<>();

            for (int i = 0; i < graph.getOrder(); i++) {
                consideredVertices.add(i);
                ArrayList<Integer> a = new ArrayList<>();
                a.add(i);
                cliques.add(a);
            }

            find(consideredVertices, graph);

            graph.setLowerBound(cliques.getFirst().size());
        }

        /**
         * Returns the largest clique found during the last lower bound calculation.
         * @return List of vertices in the largest clique
         */
        public static List<Integer> getLargestClique() {
            return cliques.isEmpty() ? new ArrayList<>() : cliques.getFirst();
        }

        /**
         * Recursive function which with each call assigns additional vertices to cliques in the original cliques list of lists.
         * Once it returns cliques contains only lists with vertices which are parts of the largest clique(s) in the graph.
         *
         * @param consideredVertices List of every vertex in the graph used to keep track of which vertices are left to consider.
//         * @param cliques            List of lists of ints. The sublists get larger by one in each recursive level.
         * @param graph              The analyzed Graph object.
         */
        private static void find(ArrayList<Integer> consideredVertices, Graph graph) {
            ArrayList<Integer> availableVertices = new ArrayList<>(consideredVertices);
            ArrayList<ArrayList<Integer>> newCliques = new ArrayList<>();

            for (ArrayList<Integer> clique : cliques) {
                for (int i : clique)
                    if (availableVertices.contains(i))
                        availableVertices.remove((Integer) i);

                for (int i : availableVertices) {
                    boolean partOfClique = true;
                    for (int j : clique)
                        if (graph.getMatrix()[i][j] != 1) {
                            partOfClique = false;
                            break;
                        }
                    if (partOfClique) {
                        ArrayList<Integer> newClique = new ArrayList<>(clique);
                        newClique.add(i);
                        newCliques.add(newClique);
                    }
                }
            }

            if (newCliques.isEmpty())
                return;

            cliques = newCliques;

            find(consideredVertices, graph);
        }
    }
}
