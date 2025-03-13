package org.example.realphase2.Gamemodelogic;

import java.util.Scanner;
import java.util.List;
import org.example.realphase2.Generation.ColEdge;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class HintSystem {
    private List<ColEdge> edges;
    private int[] colourArray;

    private static final Map<Integer, String> COLOR_NAMES = new HashMap<>() {{
        put(1, "Red");
        put(2, "Blue");
        put(3, "Green");
        put(4, "Yellow");
        put(5, "Purple");
        put(6, "Orange");
        put(7, "Pink");
        put(8, "Brown");
    }};

    public HintSystem(List<ColEdge> edges, int[] colourArray) {
        this.edges = edges;
        this.colourArray = colourArray;
    }

    /**
     * Asks is a user wants a hint.
     * If the user wants a hint, it tells them which node would be best to colour next.
     */

    // This needs to be implemented in a loop that checks whether the graph is fully coloured or not
    // like using !isGraphFullyColoured in the ToTheBitterEnd mode

    public void askAndGiveHints() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want a hint? (yes/no): ");
        String hintAnswer = scanner.nextLine().toLowerCase();
        if (hintAnswer.equals("yes")) {
            int hintNode = getNodeWithMostNeighbours();
            int colouredNeighbours = countColouredNeighbours(hintNode);
            if (colouredNeighbours > 0) { // If no nodes have been coloured yet, doesnt need to mention how many coloured neighbours there are.
                System.out.println("I would suggest you try colouring node " + Integer.toString(hintNode) + "; it has " + colouredNeighbours + " coloured neighbours");
            } else {
                System.out.println("I would suggest you try colouring node " + Integer.toString(hintNode));
            }
            
        } else if (hintAnswer.equals("no")) {
            System.out.println("No Problem, Good Luck!");
        } else {
            System.out.println("Invalid input!");
        }
    }

    /**
     * Gets the uncoloured node with the most edges connected to coloured nodes.
     * 
     * @return The node with the most coloured neighbours
     */
    public int getNodeWithMostNeighbours() {
        int mostColouredNeighbours = -1;
        int nodeWithMostColouredNeighbours = -1;

        for (int i = 0; i < colourArray.length; i++) {
            if (colourArray[i] == 0) {
                int numColouredNeighbours = countColouredNeighbours(i);
                if (numColouredNeighbours > mostColouredNeighbours) {
                    mostColouredNeighbours = numColouredNeighbours;
                    nodeWithMostColouredNeighbours = i;
                }
            }
        }
        return nodeWithMostColouredNeighbours;
    }

    /**
     * counts how many adjacent coloured nodes the given node has
     * 
     * @param node the node to count the neighbours off
     * @return the number of coloured neighbours
     */
    public int countColouredNeighbours(int node) {
        int count = 0;
        for (ColEdge edge : edges) {
            if (edge.u == node && colourArray[edge.v - 1] != 0) {
                count++;
            } else if (edge.v == node && colourArray[edge.u - 1] != 0) {
                count++;
            }
        }
        return count;
    }

    public String giveHints(int gameMode) {
        int displayNode;
        
        if (gameMode == 1) {
            // TTBE mode - use most neighbors strategy
            int hintNode = getNodeWithMostNeighbours();
            displayNode = hintNode + 1;
        } else {
            // RandomOrder and ICMM - find next uncolored vertex
            displayNode = getNextUncoloredVertex();
        }
        
        // Find valid color
        int suggestedColorNum = 1;
        while (!IsColorable.isColorable(colourArray, displayNode, suggestedColorNum, edges)) {
            suggestedColorNum++;
            if (suggestedColorNum > 8) break;
        }
        
        String suggestedColor = COLOR_NAMES.getOrDefault(suggestedColorNum, "color " + suggestedColorNum);
        return "I suggest coloring node " + displayNode + " with " + suggestedColor;
    }

    private int getNextUncoloredVertex() {
        for (int i = 0; i < colourArray.length; i++) {
            if (colourArray[i] == 0) {
                return i + 1; // Convert to 1-based indexing
            }
        }
        return 1; // Default if all colored
    }
}
