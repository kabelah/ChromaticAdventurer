package org.example.phase3.phase1algo.algorithms;

import org.example.phase3.phase1algo.structures.Graph;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * Store the best coloring found
     */
    private int[] bestColoring;

    /**
     * Lock for synchronizing coloring updates
     */
    private final Object coloringLock = new Object();

    /**
     * Lower bound for the chromatic number
     */
    private int lowerBound;

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
     * @param upperBound     The upper bound for the chromatic number obtained from DSATUR algorithm
     * @param lowerBound     The lower bound for the chromatic number
     */
    public void runBacktracking(int[][] adjacencyMatrix, int upperBound, int lowerBound) {
        int n = adjacencyMatrix.length;
        this.lowerBound = lowerBound;
        int[] vertexColors = new int[n];
        Arrays.fill(vertexColors, -1);
        bestColoring = new int[n];
        Arrays.fill(bestColoring, -1);
        buildAdjacencyList(adjacencyMatrix);
        
        // Use more threads for larger graphs
        int threadCount = Math.min(Math.max(NUM_THREADS, n/10), 32);
        this.executorService = Executors.newFixedThreadPool(threadCount);
        
        try {
            parallelBacktracking(n, vertexColors, upperBound);
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
        
        // Start from upperBound-1 since we already have a solution for upperBound
        for (int bound = upperBound - 1; bound >= lowerBound; bound--) {
            final int currentBound = bound;  // Create final copy for lambda
            if (timer.hasTimerExpired()) {
                System.out.println("Timer expired at bound: " + currentBound);
                break;
            }
            
            System.out.println("Trying coloring with " + currentBound + " colors...");
            bestChromaticNumber.set(currentBound);  // Set this as our target
            
            // Divide the search space among threads
            int numThreads = Math.min(currentBound, Runtime.getRuntime().availableProcessors() * 2);
            CountDownLatch latch = new CountDownLatch(numThreads);
            AtomicBoolean foundSolution = new AtomicBoolean(false);
            
            // Launch parallel tasks with different initial vertex colorings
            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                executorService.submit(() -> {
                    try {
                        int[] colors = new int[n];
                        Arrays.fill(colors, -1);
                        
                        // Each thread starts with different high-degree vertices colored differently
                        for (int j = 0; j < Math.min(n, numThreads); j++) {
                            int vertex = vertexOrder[j];  // Use highest degree vertices
                            colors[vertex] = (j + threadId) % currentBound;
                        }
                        
                        BitSet[] availableColors = initializeAvailableColors(n, currentBound);
                        Set<Integer> unassigned = new HashSet<>();
                        
                        // Initialize unassigned vertices
                        for (int j = numThreads; j < n; j++) {
                            unassigned.add(vertexOrder[j]);
                        }
                        
                        // Update available colors based on initial coloring
                        for (int v = 0; v < n; v++) {
                            if (colors[v] != -1) {
                                for (int neighbor : adjacencyList[v]) {
                                    if (colors[neighbor] == -1) {
                                        availableColors[neighbor].clear(colors[v]);
                                    }
                                }
                            }
                        }
                        
                        if (backtrack(n, colors, currentBound, availableColors, new ArrayDeque<>(), unassigned)) {
                            foundSolution.set(true);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            // Wait for all threads to finish or timer to expire
            while (!latch.await(1, TimeUnit.SECONDS)) {
                if (timer.hasTimerExpired()) {
                    break;
                }
            }
            
            if (!foundSolution.get() && !timer.hasTimerExpired()) {
                // If we couldn't find a solution with this many colors, we can't do better
                bestChromaticNumber.set(currentBound + 1);
                break;
            }
            
            // If we found a solution and still have time, continue to next bound
            if (foundSolution.get() && !timer.hasTimerExpired()) {
                System.out.println("Found solution with " + currentBound + " colors, trying fewer colors...");
            }
        }
    }

    private BitSet[] initializeAvailableColors(int n, int maxColors) {
        BitSet[] availableColors = new BitSet[n];
        for (int i = 0; i < n; i++) {
            BitSet colors = new BitSet(maxColors);
            colors.set(0, maxColors);
            availableColors[i] = colors;
        }
        return availableColors;
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
            synchronized (coloringLock) {
                if (isValidColoring(vertexColors)) {
                    Set<Integer> distinctColors = new HashSet<>();
                    for (int color : vertexColors) {
                        distinctColors.add(color);
                    }
                    int chromaticNum = distinctColors.size();
                    
                    if (chromaticNum <= maxColors) {
                        System.arraycopy(vertexColors, 0, bestColoring, 0, vertexColors.length);
                        System.out.println("Found valid coloring with " + chromaticNum + " colors");
                        return true;
                    }
                }
            }
            return false;
        }

        // Select vertex with smallest domain and highest impact
        int vertex = selectNextVertex(vertexColors, availableColors, unassignedVertices);
        BitSet colors = availableColors[vertex];
        
        if (colors.isEmpty()) {
            return false;
        }

        // Get colors ordered by least constraining value and most used
        List<Integer> orderedColors = getOptimizedColorOrder(vertex, colors, vertexColors, availableColors);
        Map<Integer, Set<Integer>> forwardCheckResults = new HashMap<>();
        
        for (int color : orderedColors) {
            if (timer.hasTimerExpired() || color >= bestChromaticNumber.get()) {
                continue;
            }

            // Try to assign color with forward checking
            Set<Integer> affectedVertices = forwardCheck(vertex, color, vertexColors, availableColors, unassignedVertices);
            if (affectedVertices != null) {
                vertexColors[vertex] = color;
                unassignedVertices.remove(vertex);
                forwardCheckResults.put(color, affectedVertices);
                
                if (backtrack(n, vertexColors, maxColors, availableColors, changeStack, unassignedVertices)) {
                    return true;
                }
                
                // Restore state with conflict-directed backjumping
                restoreState(vertex, color, vertexColors, availableColors, unassignedVertices, affectedVertices);
            }
        }
        
        return false;
    }

    private int selectNextVertex(int[] vertexColors, BitSet[] availableColors, Set<Integer> unassignedVertices) {
        int minDomain = Integer.MAX_VALUE;
        int maxImpact = -1;
        int selectedVertex = -1;
        
        for (int vertex : unassignedVertices) {
            int domain = availableColors[vertex].cardinality();
            int impact = calculateVertexImpact(vertex, vertexColors);
            
            if (domain < minDomain || (domain == minDomain && impact > maxImpact)) {
                minDomain = domain;
                maxImpact = impact;
                selectedVertex = vertex;
            }
        }
        
        return selectedVertex;
    }

    private int calculateVertexImpact(int vertex, int[] vertexColors) {
        int uncoloredNeighbors = 0;
        Set<Integer> neighborColors = new HashSet<>();
        
        for (int neighbor : adjacencyList[vertex]) {
            if (vertexColors[neighbor] == -1) {
                uncoloredNeighbors++;
            } else {
                neighborColors.add(vertexColors[neighbor]);
            }
        }
        
        return uncoloredNeighbors * 2 + neighborColors.size();
    }

    private List<Integer> getOptimizedColorOrder(int vertex, BitSet colors, int[] vertexColors, BitSet[] availableColors) {
        List<Integer> colorList = new ArrayList<>();
        Map<Integer, Integer> colorUsage = new HashMap<>();
        Map<Integer, Integer> colorConstraints = new HashMap<>();
        
        // Count color usage in graph
        for (int color = colors.nextSetBit(0); color >= 0; color = colors.nextSetBit(color + 1)) {
            colorList.add(color);
            colorUsage.put(color, 0);
            for (int v = 0; v < vertexColors.length; v++) {
                if (vertexColors[v] == color) {
                    colorUsage.put(color, colorUsage.get(color) + 1);
                }
            }
        }
        
        // Calculate constraints
        for (int color : colorList) {
            int constraints = 0;
            for (int neighbor : adjacencyList[vertex]) {
                if (availableColors[neighbor].get(color)) {
                    constraints++;
                }
            }
            colorConstraints.put(color, constraints);
        }
        
        // Sort by combination of usage and constraints
        colorList.sort((c1, c2) -> {
            int usageComp = Integer.compare(colorUsage.get(c2), colorUsage.get(c1));
            if (usageComp != 0) return usageComp;
            return Integer.compare(colorConstraints.get(c1), colorConstraints.get(c2));
        });
        
        return colorList;
    }

    private Set<Integer> forwardCheck(int vertex, int color, int[] vertexColors, 
                                    BitSet[] availableColors, Set<Integer> unassignedVertices) {
        Set<Integer> affectedVertices = new HashSet<>();
        
        // Check and update domains of unassigned neighbors
        for (int neighbor : adjacencyList[vertex]) {
            if (unassignedVertices.contains(neighbor)) {
                if (!availableColors[neighbor].get(color)) {
                    continue;  // Color already not available
                }
                
                // Simulate removing the color
                availableColors[neighbor].clear(color);
                affectedVertices.add(neighbor);
                
                // Check if domain becomes empty
                if (availableColors[neighbor].isEmpty()) {
                    // Restore domains and return null to indicate failure
                    for (int v : affectedVertices) {
                        availableColors[v].set(color);
                    }
                    return null;
                }
            }
        }
        
        return affectedVertices;
    }

    private void restoreState(int vertex, int color, int[] vertexColors, BitSet[] availableColors,
                            Set<Integer> unassignedVertices, Set<Integer> affectedVertices) {
        vertexColors[vertex] = -1;
        unassignedVertices.add(vertex);
        
        // Restore domains of affected vertices
        for (int v : affectedVertices) {
            availableColors[v].set(color);
        }
    }

    private boolean isValidColoring(int[] coloring) {
        for (int i = 0; i < coloring.length; i++) {
            for (int neighbor : adjacencyList[i]) {
                if (coloring[i] == coloring[neighbor]) {
                    return false;
                }
            }
        }
        return true;
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
        
        Arrays.sort(vertices, (v1, v2) -> {
            int degree1 = adjacencyList[v1].size();
            int degree2 = adjacencyList[v2].size();
            return Integer.compare(degree2, degree1);
        });
        
        int[] sortedVertices = new int[n];
        for (int i = 0; i < n; i++) {
            sortedVertices[i] = vertices[i];
        }
        return sortedVertices;
    }

    /**
     * Gets the best coloring found.
     *
     * @return The best coloring found
     */
    public int[] getBestColoring() {
        return bestColoring;
    }

    private static class DomainInfo {
        BitSet availableColors;
        int domainSize;
        
        DomainInfo(int maxColors) {
            availableColors = new BitSet(maxColors);
            availableColors.set(0, maxColors);
            domainSize = maxColors;
        }
        
        void removeColor(int color) {
            if (availableColors.get(color)) {
                availableColors.clear(color);
                domainSize--;
            }
        }
        
        void restoreColor(int color) {
            if (!availableColors.get(color)) {
                availableColors.set(color);
                domainSize++;
            }
        }
    }

    private static class ConflictSet {
        Set<Integer> conflicts = new HashSet<>();
        
        void addConflict(int vertex) {
            conflicts.add(vertex);
        }
        
        Set<Integer> getConflicts() {
            return conflicts;
        }
    }
}

