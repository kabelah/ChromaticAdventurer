package org.example.realphase2.ChromaticNumber.org.phase1.utils;

import java.util.ArrayList;

/**
 * A collection of utility functions assisting with conversions and speeding up calculations.
 */
public class HelpFunctions {
    /**
     * @param edgeList Edge list of a graph.
     * @param order    Number of vertices in the graph.
     * @return Adjacency list of the graph.
     */
    public static ArrayList<Integer>[] edgeListToAdjacencyList(int[][] edgeList, int order) {
        ArrayList<Integer>[] adjacencyList = new ArrayList[order];
        for (int i = 0; i < order; i++)
            adjacencyList[i] = new ArrayList<>();

        for (int[] ints : edgeList) {
            adjacencyList[ints[0]].add(ints[1]);
            adjacencyList[ints[1]].add(ints[0]);
        }

        return adjacencyList;
    }

    /**
     * @param adjacencyList The adjacency list of a graph.
     * @return The adjacency matrix of the graph.
     */
    public static int[][] adjacencyListToMatrix(ArrayList<Integer>[] adjacencyList) {
        int[][] matrix = new int[adjacencyList.length][adjacencyList.length];
        for (int i = 0; i < adjacencyList.length; i++)
            for (int j = 0; j < adjacencyList.length; j++)
                matrix[i][j] = 0;

        for (int i = 0; i < adjacencyList.length; i++)
            for (int j : adjacencyList[i]) {
                matrix[i][j] = 1;
                matrix[j][i] = 1;
            }


        return matrix;
    }

    /**
     * @param array Array to be considered.
     * @return Greatest int in the array.
     */
    public static int maxInArray(int[] array) {
        int max = 0;
        for (int i : array)
            if (i > max)
                max = i;

        return max;
    }

    /**
     * Formats and outputs a matrix to the console.
     *
     * @param matrix 2D int array to be printed.
     */
    public static void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            String line = "";
            for (int j = 0; j < matrix.length; j++)
                line += (ints[j] + "  ");
            System.out.println(line);
        }
    }
}
