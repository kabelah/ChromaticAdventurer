package org.example.realphase2.ChromaticNumber.org.phase1.algorithms;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements a parallel backtracking algorithm for graph coloring using MRV and LCV heuristics.
 * It searches for the chromatic number of a graph within a given time limit.
 */
public class Backtracking {

    /**
     * Number of available threads for parallel execution
     */
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    /**
     * Atomic integer to keep track of the best chromatic number found
     */
    private AtomicInteger bestChromaticNumber;

    /**
     * Executor service for managing threads
     */
    private ExecutorService executorService;

    /**
     * Adjacency list representation of the graph
     */
    private List<Integer>[] adjacencyList;

    /**
     * Timer to check for time expiration
     */
    private TimerCheck timer;

    /**
     * Constructs a Backtracking instance with a timer and an atomic integer for the best chromatic number.
     *
     * @param timer               Timer to enforce time limits on the algorithm
     * @param bestChromaticNumber AtomicInteger to track the best chromatic number found
     */
    public Backtracking(TimerCheck timer, AtomicInteger bestChromaticNumber) {
        this.bestChromaticNumber = bestChromaticNumber;
        this.timer = timer;
    }

    /**
     * Runs the parallel backtracking algorithm to find the chromatic number of a graph.
     *
     * @param adjacencyMatrix  The adjacency matrix of the graph
     * @param dsaturUpperBound The upper bound for the chromatic number obtained from DSATUR algorithm
     */
    public void runBacktracking(int[][] adjacencyMatrix, int dsaturUpperBound) {
        int n = adjacencyMatrix.length;
        int[] vertexColors = new int[n];
        Arrays.fill(vertexColors, -1);
        buildAdjacencyList(adjacencyMatrix);
        this.executorService = Executors.newFixedThreadPool(NUM_THREADS);
        try {
            parallelBacktracking(n, vertexColors, dsaturUpperBound);
        } catch (Exception e) {
            // Suppress exceptions to prevent unwanted prints
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Builds the adjacency list from the given adjacency matrix.
     *
     * @param adjacencyMatrix The adjacency matrix of the graph
     */
    private void buildAdjacencyList(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;
        adjacencyList = new List[n];
        for (int i = 0; i < n; i++) {
            adjacencyList[i] = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    adjacencyList[i].add(j);
                }
            }
        }
    }

    /**
     * Initiates the parallel backtracking process to find the chromatic number.
     *
     * @param n            Number of vertices in the graph
     * @param vertexColors Array to keep track of vertex colors
     * @param upperBound   Initial upper bound for the chromatic number
     * @throws Exception If the computation is interrupted
     */
    public void parallelBacktracking(int n, int[] vertexColors, int upperBound) throws Exception {
        int[] vertexOrder = getOrderOfVertices(adjacencyList, n);
        for (int currentBound = upperBound; currentBound >= 1; currentBound--) {
            if (timer.hasTimerExpired()) {
                break;
            }
            bestChromaticNumber.set(currentBound);
            if (submitParallelTasks(n, vertexColors, vertexOrder, currentBound)) {
                break;
            }
        }
    }

    /**
     * Submits parallel tasks to explore each color option at the top level.
     *
     * @param n            Number of vertices in the graph
     * @param vertexColors Array of current vertex colors
     * @param vertexOrder  Order of vertices for coloring
     * @param maxColors    Maximum number of colors to try
     * @return True if a valid coloring is found; false otherwise
     * @throws Exception If the computation is interrupted
     */
    private boolean submitParallelTasks(int n, int[] vertexColors, int[] vertexOrder, int maxColors) throws Exception {
        int numTasks = Math.min(maxColors, NUM_THREADS);
        Future<Boolean>[] results = new Future[numTasks];
        for (int color = 0; color < numTasks; color++) {
            int[] vertexColorsCopy = Arrays.copyOf(vertexColors, vertexColors.length);
            int colorChoice = color;
            results[color] = executorService.submit(() -> {
                if (timer.hasTimerExpired()) {
                    return false;
                }
                vertexColorsCopy[vertexOrder[0]] = colorChoice;
                BitSet[] availableColors = new BitSet[n];
                for (int i = 0; i < n; i++) {
                    BitSet colors = new BitSet(maxColors);
                    colors.set(0, maxColors);
                    availableColors[i] = colors;
                }
                for (int neighbor : adjacencyList[vertexOrder[0]]) {
                    availableColors[neighbor].clear(colorChoice);
                    if (availableColors[neighbor].isEmpty()) {
                        return false;
                    }
                }
                Deque<Change> changeStack = new ArrayDeque<>();
                Set<Integer> unassignedVertices = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    if (vertexColorsCopy[i] == -1) {
                        unassignedVertices.add(i);
                    }
                }
                unassignedVertices.remove(vertexOrder[0]);
                return backtrack(n, vertexColorsCopy, maxColors, availableColors, changeStack, unassignedVertices);
            });
        }
        for (Future<Boolean> result : results) {
            if (timer.hasTimerExpired()) {
                break;
            }
            if (result.get()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the backtracking algorithm with MRV and LCV heuristics.
     *
     * @param n                  Number of vertices in the graph
     * @param vertexColors       Array of current vertex colors
     * @param maxColors          Maximum number of colors to try
     * @param availableColors    Array of BitSets representing available colors for each vertex
     * @param changeStack        Stack to keep track of changes for backtracking
     * @param unassignedVertices Set of unassigned vertices
     * @return True if a valid coloring is found; false otherwise
     */
    public boolean backtrack(int n, int[] vertexColors, int maxColors, BitSet[] availableColors,
                             Deque<Change> changeStack, Set<Integer> unassignedVertices) {
        if (timer.hasTimerExpired()) {
            return false;
        }
        if (unassignedVertices.isEmpty()) {
            return true;
        }
        int vertex = selectMRVVertex(availableColors, unassignedVertices);
        BitSet colors = availableColors[vertex];
        if (colors.isEmpty()) {
            return false;
        }
        List<Integer> orderedColors = orderColorsByLCV(vertex, colors, availableColors);
        for (int color : orderedColors) {
            if (timer.hasTimerExpired()) {
                return false;
            }
            if (color >= bestChromaticNumber.get()) {
                continue;
            }
            vertexColors[vertex] = color;
            int changeStackSize = changeStack.size();
            boolean failure = false;
            for (int neighbor : adjacencyList[vertex]) {
                if (vertexColors[neighbor] == -1) {
                    if (availableColors[neighbor].get(color)) {
                        availableColors[neighbor].clear(color);
                        changeStack.push(new Change(neighbor, color));
                        if (availableColors[neighbor].isEmpty()) {
                            failure = true;
                            break;
                        }
                    }
                }
            }
            if (!failure) {
                unassignedVertices.remove(vertex);
                if (backtrack(n, vertexColors, maxColors, availableColors, changeStack, unassignedVertices)) {
                    return true;
                }
                unassignedVertices.add(vertex);
            }
            vertexColors[vertex] = -1;
            while (changeStack.size() > changeStackSize) {
                Change change = changeStack.pop();
                availableColors[change.vertex].set(change.color);
            }
        }
        return false;
    }

    /**
     * Selects the unassigned vertex with the fewest available colors (MRV heuristic).
     *
     * @param availableColors    Array of BitSets representing available colors for each vertex
     * @param unassignedVertices Set of unassigned vertices
     * @return The vertex with the minimum remaining values
     */
    private int selectMRVVertex(BitSet[] availableColors, Set<Integer> unassignedVertices) {
        int minDomainSize = Integer.MAX_VALUE;
        int selectedVertex = -1;
        for (int vertex : unassignedVertices) {
            int domainSize = availableColors[vertex].cardinality();
            if (domainSize < minDomainSize) {
                minDomainSize = domainSize;
                selectedVertex = vertex;
                if (domainSize == 1) {
                    break;
                }
            }
        }
        return selectedVertex;
    }

    /**
     * Orders colors using the Least Constraining Value (LCV) heuristic.
     *
     * @param vertex          The vertex for which colors are being ordered
     * @param colors          BitSet of available colors for the vertex
     * @param availableColors Array of BitSets representing available colors for each vertex
     * @return A list of colors ordered by the least constraining value
     */
    private List<Integer> orderColorsByLCV(int vertex, BitSet colors, BitSet[] availableColors) {
        List<Integer> colorList = new ArrayList<>();
        for (int color = colors.nextSetBit(0); color >= 0; color = colors.nextSetBit(color + 1)) {
            colorList.add(color);
        }
        Map<Integer, Integer> colorConstraints = new HashMap<>();
        for (int color : colorList) {
            int constraints = 0;
            for (int neighbor : adjacencyList[vertex]) {
                if (availableColors[neighbor].get(color)) {
                    constraints++;
                }
            }
            colorConstraints.put(color, constraints);
        }
        colorList.sort(Comparator.comparingInt(colorConstraints::get));
        return colorList;
    }

    /**
     * Records changes for backtracking.
     */
    class Change {
        int vertex;
        int color;

        /**
         * Constructs a Change instance representing a change in the color availability of a vertex.
         *
         * @param vertex The vertex whose color availability has changed
         * @param color  The color that has been removed or added
         */
        public Change(int vertex, int color) {
            this.vertex = vertex;
            this.color = color;
        }
    }

    /**
     * Gets the order of vertices sorted by their degree in descending order.
     *
     * @param adjacencyList Adjacency list of the graph
     * @param n             Number of vertices in the graph
     * @return An array of vertex indices sorted by degree
     */
    public int[] getOrderOfVertices(List<Integer>[] adjacencyList, int n) {
        Integer[] vertices = new Integer[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = i;
        }
        Arrays.sort(vertices, Comparator.comparingInt((Integer v) -> adjacencyList[v].size()).reversed());
        int[] sortedVertices = new int[n];
        for (int i = 0; i < n; i++) {
            sortedVertices[i] = vertices[i];
        }
        return sortedVertices;
    }
}
