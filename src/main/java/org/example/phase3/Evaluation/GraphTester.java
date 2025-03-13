package org.example.phase3.Evaluation;

import org.example.phase3.Generation.ColEdge;

import java.util.*;

public class GraphTester {

    public static String classifyGraph(Graph graph) {
        int V = graph.getVertexCount();
        int E = graph.getEdges().size();

        // 1. Check connectivity
        if (!isConnected(graph)) {
            System.out.println("[DEBUG] Graph is disconnected.");
            if (hasNoCycles(graph)) {
                return "FOREST";
            }
            if (isBipartite(graph)) {
                return "BIPARTITE";
            }
            return "UNKNOWN";
        }

        // 2. Check if the graph is acyclic (tree)
        if (hasNoCycles(graph)) {
            System.out.println("[DEBUG] Graph is connected and acyclic.");
            if (isStar(graph)) {
                return "STAR";
            }
            return "TREE";
        }

        // 3. Check for special structures
        if (isWheel(graph)) {
            return "WHEEL";
        }
        if (allVerticesDegree2(graph)) {
            return "CYCLE";
        }

        // 4. Check planarity and outerplanarity
        if (isOuterplanar(graph)) {
            return "OUTERPLANAR";
        }
        if (isPlanar(graph)) {
            return "PLANAR";
        }

        // 5. Additional classifications
        if (isChordal(graph)) {
            return "CHORDAL";
        }
        if (isSplit(graph)) {
            return "SPLIT";
        }
        if (isDense(graph)) {
            return "DENSE (NEAR-COMPLETE)";
        }
        if (isCograph(graph)) {
            return "COGRAPH";
        }

        // If all else fails
        return "UNKNOWN";
    }

    // -------------------------------------------------------------------------
    // Graph Property Checks
    // -------------------------------------------------------------------------

    public static boolean isConnected(Graph graph) {
        int V = graph.getVertexCount();
        List<ColEdge> edges = graph.getEdges();
        if (V <= 1) return true;
        if (edges.isEmpty()) return false;

        List<List<Integer>> adj = buildAdjList(graph);
        boolean[] visited = new boolean[V + 1];
        dfs(1, adj, visited);

        for (int i = 1; i <= V; i++) {
            if (!visited[i]) return false;
        }
        return true;
    }

    private static void dfs(int node, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true;
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adj, visited);
            }
        }
    }

    public static boolean hasNoCycles(Graph graph) {
        int V = graph.getVertexCount();
        List<List<Integer>> adj = buildAdjList(graph);
        boolean[] visited = new boolean[V + 1];
        for (int v = 1; v <= V; v++) {
            if (!visited[v] && dfsCycleDetect(v, -1, adj, visited)) {
                return false;
            }
        }
        return true;
    }

    private static boolean dfsCycleDetect(int curr, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[curr] = true;
        for (int neighbor : adj.get(curr)) {
            if (!visited[neighbor]) {
                if (dfsCycleDetect(neighbor, curr, adj, visited)) {
                    return true;
                }
            } else if (neighbor != parent) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBipartite(Graph graph) {
        int V = graph.getVertexCount();
        List<List<Integer>> adj = buildAdjList(graph);
        int[] colors = new int[V + 1];
        Arrays.fill(colors, -1);

        for (int start = 1; start <= V; start++) {
            if (colors[start] == -1) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(start);
                colors[start] = 1;

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int neighbor : adj.get(node)) {
                        if (colors[neighbor] == -1) {
                            colors[neighbor] = 1 - colors[node];
                            queue.add(neighbor);
                        } else if (colors[neighbor] == colors[node]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean isStar(Graph graph) {
        int V = graph.getVertexCount();
        int[] degree = getVertexDegrees(graph);
        int centerCount = 0, leafCount = 0;

        for (int deg : degree) {
            if (deg == V - 1) centerCount++;
            else if (deg == 1) leafCount++;
        }
        return centerCount == 1 && leafCount == V - 1;
    }

    public static boolean isWheel(Graph graph) {
        int V = graph.getVertexCount();
        if (V < 4) return false;

        int[] degree = getVertexDegrees(graph);
        int center = -1;

        for (int i = 1; i <= V; i++) {
            if (degree[i] == V - 1) {
                center = i;
                break;
            }
        }

        if (center == -1) return false;

        for (int i = 1; i <= V; i++) {
            if (i == center) continue;
            if (degree[i] != 3) return false;
        }
        return true;
    }

    public static boolean allVerticesDegree2(Graph graph) {
        int[] degree = getVertexDegrees(graph);
        for (int deg : degree) {
            if (deg != 2) return false;
        }
        return true;
    }

    public static boolean isOuterplanar(Graph graph) {
        int V = graph.getVertexCount();
        int E = graph.getEdges().size();

        return E <= 2 * V - 3 && !containsK4(graph) && !containsK23(graph);
    }

    public static boolean isPlanar(Graph graph) {
        // Kuratowski's theorem: A graph is planar if and only if it doesn't contain 
        // a subgraph that is a subdivision of K5 or K3,3
        int V = graph.getVertexCount();
        
        // Small graphs are always planar
        if (V < 5) return true;
        
        // If edges > 3V - 6, graph cannot be planar (Euler's formula)
        int E = graph.getEdges().size();
        if (E > 3*V - 6) return false;
        
        // TODO: Full Kuratowski subgraph check if needed
        return true;
    }

    public static boolean isChordal(Graph graph) {
        return false; // Replace with proper logic.
    }

    public static boolean isSplit(Graph graph) {
        return false; // Replace with proper logic.
    }

    public static boolean isDense(Graph graph) {
        int V = graph.getVertexCount();
        int E = graph.getEdges().size();
        double maxEdges = (double) V * (V - 1) / 2;

        return (E / maxEdges) >= 0.9;
    }

    public static boolean isCograph(Graph graph) {
        return false; // Replace with proper logic.
    }

    private static int[] getVertexDegrees(Graph graph) {
        int V = graph.getVertexCount();
        int[] degree = new int[V + 1];
        for (ColEdge edge : graph.getEdges()) {
            degree[edge.u]++;
            degree[edge.v]++;
        }
        return degree;
    }

    private static List<List<Integer>> buildAdjList(Graph graph) {
        int V = graph.getVertexCount();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (ColEdge edge : graph.getEdges()) {
            adj.get(edge.u).add(edge.v);
            adj.get(edge.v).add(edge.u);
        }
        return adj;
    }

    private static boolean containsK4(Graph graph) {
        return false; // Placeholder.
    }

    private static boolean containsK23(Graph graph) {
        return false; // Placeholder.
    }

    public static boolean isComplete(Graph graph) {
        int V = graph.getVertexCount();
        int[] degree = getVertexDegrees(graph);
        
        // In a complete graph, every vertex must have degree V-1
        for (int i = 1; i <= V; i++) {
            if (degree[i] != V - 1) return false;
        }
        return true;
    }

    public static boolean isCycle(Graph graph) {
        return isConnected(graph) && allVerticesDegree2(graph);
    }
}