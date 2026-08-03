package org.phase1.algorithms;


import org.phase1.structures.Graph;
import org.phase1.structures.GraphCharacteristics;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class AlgorithmHandling {
    private static final int BACKTRACKING_TIME = 2 * 60 * 1000;


    /**
     * Displays characteristics of the graph and runs necessary algorithms for analysis.
     *
     * @param graph The analyzed Graph object.
     */
    public static void evaluate(Graph graph) {
        System.out.println("Graph order: " + graph.getOrder());
        System.out.println("Number of edges: " + graph.getEdgeList().length);
        System.out.println("Graph density: " + graph.getDensity() + "\n");

        if (GraphCharacteristics.complete(graph)) {
            System.out.println("This is a complete graph.");
            System.out.println("The exact chromatic number of this graph is: " + graph.getOrder());
            return;
        }

        if (GraphCharacteristics.connected(graph)) {
            System.out.println("The graph is connected.");
        } else {
            System.out.println("The graph is not connected. It consists of the following subgraphs:");
            for (Set<Integer> subgraph : graph.getSubgraphs())
                System.out.println(subgraph);
        }

        Bounds.LowerBound.lowerBoundLargestClique(graph);
        int lowerBound = graph.getLowerBound();
        System.out.println("Lower bound (largest clique): " + lowerBound);

        Bounds.upperBoundGreedy(graph);
        int upperBoundWP = graph.getUpperBound();
        System.out.println("Upper bound (Welsh-Powell): " + upperBoundWP);

        ChromaticNumber.DSATUR(graph);
        int upperBoundDsatur = graph.getChromaticNumber();
        System.out.println("Upper bound (Dsatur): " + upperBoundDsatur);

        int lowestUpperBound = Math.min(upperBoundDsatur, upperBoundWP);
        if (lowerBound == lowestUpperBound) {
            System.out.println("The exact chromatic number is: " + lowerBound);

            return;
        } else
            System.out.println("Running backtracking algorithm...");

        AtomicInteger bestChromaticNumber = new AtomicInteger(Integer.MAX_VALUE);

        TimerCheck timer = new TimerCheck(BACKTRACKING_TIME);
        timer.start();

        Backtracking backtracking = new Backtracking(timer, bestChromaticNumber);
        backtracking.runBacktracking(graph.getMatrix(), lowestUpperBound);

        int finalChromaticNumber = bestChromaticNumber.get();
        if (finalChromaticNumber == Integer.MAX_VALUE) {
            System.out.println("No valid coloring found within the time limit.");
        } else {
            System.out.println("The lowest chromatic number found is: " + finalChromaticNumber);


            if (!timer.hasTimerExpired()) {
                timer.stop();
            }

            return;
        }

        System.out.println("Time expired! Terminating process...");
    }
}
