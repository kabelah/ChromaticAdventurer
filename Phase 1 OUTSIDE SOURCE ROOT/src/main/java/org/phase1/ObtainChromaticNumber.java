package org.phase1;

import org.phase1.algorithms.AlgorithmHandling;
import org.phase1.structures.Graph;

// Run this with the argument being only the name of the .txt file containing the graph.
public class ObtainChromaticNumber {
    public static void start(String graphGiven) {
        Graph graph = new Graph(graphGiven);

        AlgorithmHandling.evaluate(graph);
    }
}
