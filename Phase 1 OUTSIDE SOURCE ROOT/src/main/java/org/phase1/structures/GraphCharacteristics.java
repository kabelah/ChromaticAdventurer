package org.phase1.structures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class whose methods recognize what type of graph is being analyzed and find structures in it.
 */
public class GraphCharacteristics {
    /**
     * @param graph The analyzed Graph object.
     * @return True if the graph is bipartite, false otherwise.
     */
    public static boolean bipartite(Graph graph) {
        return false;
    }

    /**
     * @param graph The analyzed Graph object.
     * @return True if the graph is connected, false otherwise.
     */
    public static boolean connected(Graph graph) {
        Set<Integer> visited = new HashSet<>();

        ConnectedHelp.depthFirstSearch(0, visited, graph.getAdjacencyList());

        if (visited.size() < graph.getOrder()) {
            ConnectedHelp.subgraphs(graph, visited);
            return false;
        }

        return true;
    }

    static class ConnectedHelp {
        /**
         * Uses depth first search to recursively looks for all neighbors of a starting vertex.
         *
         * @param vertex        Integer representing the starting vertex.
         * @param visited       Set storing all visited neighbors.
         * @param adjacencyList Adjacency list of the analyzed graph.
         */
        private static void depthFirstSearch(int vertex, Set<Integer> visited, List<Integer>[] adjacencyList) {
            visited.add(vertex);
            for (int i : adjacencyList[vertex])
                if (!visited.contains(i))
                    depthFirstSearch(i, visited, adjacencyList);
        }

        /**
         * In case the graph is not connected this method is used for assigning the Graph object's subgraphs variable.
         *
         * @param graph    The analyzed Graph object.
         * @param subgraph The first subgraph found when checking if the graph is connected.
         */
        public static void subgraphs(Graph graph, Set<Integer> subgraph) {
            Set<Set<Integer>> result = new HashSet<>();
            result.add(subgraph);

            for (int i = 1; i < graph.getOrder(); i++) {
                boolean evaluated = false;
                for (Set<Integer> subset : result)
                    if (subset.contains(i)) {
                        evaluated = true;
                        break;
                    }

                if (!evaluated) {
                    Set<Integer> newSubgraph = new HashSet<>();
                    depthFirstSearch(i, newSubgraph, graph.getAdjacencyList());
                    result.add(newSubgraph);
                }
            }

            graph.setSubgraphs(result);
        }
    }


    /**
     * @param graph The analyzed Graph object.
     * @return True if the graph is complete, false otherwise.
     */
    public static boolean complete(Graph graph) {
        for (int i = 0; i < graph.getOrder(); i++)
            for (int j = i + 1; j < graph.getOrder(); j++)
                if (i != j)
                    if (graph.getMatrix()[i][j] == 0) return false;

        return true;
    }
}
