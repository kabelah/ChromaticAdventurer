package org.example.realphase2.Gamemodelogic;

import org.example.realphase2.Generation.ColEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EdgeScrambler {

    // This method will scramble the vertices in a list of edges and returns the list.
    public static List<ColEdge> scrambleEdges (List<ColEdge> edges, int numVertices) {

        int numEdges = edges.size();
        List<Integer> numbersLeft = new ArrayList<Integer>();
        List<ColEdge> scrambledList = new ArrayList<ColEdge>();
        Random randomNumber = new Random();

        // Creates the new scrambled edge list with the same number of edges as the old edge list.
        // This is the list that the method returns.
        for (int i = 0; i < numEdges; i++) {
            scrambledList.add(new ColEdge(0, 0));
        }

        // Creates a list with every vertex.
        for (int i = 0; i < numVertices; i++) {
            numbersLeft.add(i+1);
        }

        // Preform the edge scrambling by getting a random number and using it to find a number
        // in the numbersLeft list and replace the first vertex with that number. This repeats
        // until all vertices have been shuffled around.
        for (int i = 0; i < numVertices; i++) {
            int randomNum = randomNumber.nextInt(numbersLeft.size());
            int vertexReplaced = numbersLeft.get(randomNum);
            numbersLeft.remove(randomNum);
            for (int j = 0; j < numEdges; j++) {
                if (edges.get(j).u == i+1) {
                    scrambledList.get(j).u = vertexReplaced;
                }
                if (edges.get(j).v == i+1) {
                    scrambledList.get(j).v = vertexReplaced;
                }
            }
        }
        return scrambledList;
    }
}