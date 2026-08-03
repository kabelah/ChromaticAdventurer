package org.example.phase3.Algorithms;

import java.util.Map;

/**
 * Common result class for all graph coloring algorithms.
 * Stores the results of a graph coloring operation including color assignments,
 * chromatic number, and the name of the algorithm used.
 */
public class ColoringResult {
    private final Map<Integer, Integer> colorAssignments;
    private final int chromaticNumber;
    private final String algorithmName;

    /**
     * Constructs a new ColoringResult with the specified parameters.
     *
     * @param colorAssignments Map of vertex IDs to their assigned colors
     * @param chromaticNumber The chromatic number found for the graph
     * @param algorithmName The name of the algorithm that produced this result
     */
    public ColoringResult(Map<Integer, Integer> colorAssignments, int chromaticNumber, String algorithmName) {
        this.colorAssignments = colorAssignments;
        this.chromaticNumber = chromaticNumber;
        this.algorithmName = algorithmName;
    }

    /**
     * @return Map of vertex IDs to their assigned colors
     */
    public Map<Integer, Integer> getColorAssignments() {
        return colorAssignments;
    }

    /**
     * @return The chromatic number of the graph
     */
    public int getChromaticNumber() {
        return chromaticNumber;
    }

    /**
     * @return The name of the algorithm used for coloring
     */
    public String getAlgorithmName() {
        return algorithmName;
    }
}