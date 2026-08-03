package org.example.realphase2.Gamemodelogic;
import java.util.ArrayList;
import java.util.List;
import org.example.realphase2.Generation.ColEdge;

public class RandomOrder {
    int numVertices;
    int numEdges;
    int[] colorArray;
    List<ColEdge> edges;
    List<Integer> vertexColored;
    private HintSystem hints;


    // Constructor for To The Bitter End's game logic
    public RandomOrder(int numVertices, int numEdges, int[] colorArray, List<ColEdge> edges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.colorArray = colorArray;
        this.edges = edges;
        this.vertexColored = new ArrayList<Integer>();
        this.vertexColored.add(0);
        this.hints = new HintSystem(edges, colorArray);
    }

    // Returns true if every vertex in the graph has been colored.
    public boolean isGraphFullyColored() {
        for (int vertex : colorArray) {
            if (vertex == 0) {
                return false;
            }
        }
        return true;
    }

    // Colors the vertex by setting its value in the colorArray to be the selected color.
    public void colorVertex(int selectedVertex, int selectedColor) {
        // If the color is 0 (uncolored), then isColorable must be skipped.

        if (selectedColor != 0) {
            if ((IsColorable.isColorable(colorArray, selectedVertex, selectedColor, edges)) && (vertexColored.get(vertexColored.size() - 1) == selectedVertex - 1)) {
                colorArray[selectedVertex-1] = selectedColor;
                vertexColored.add(selectedVertex);
                hints = new HintSystem(edges, colorArray);  // Update hints when coloring
            }
            else if (!(IsColorable.isColorable(colorArray, selectedVertex, selectedColor, edges))) {
                System.out.println("Illegal color. Random Order");
                FlagClass.illegalColor = true;
            }
            else if (!(vertexColored.get(vertexColored.size() - 1) == selectedVertex - 1)) {
                System.out.println("Wrong vertex. Random Order");
                FlagClass.illegalVertex = true;
            }
        } else {
            System.out.println("You cannot uncolor in random order with the color LIGHTBLUE");
            FlagClass.illegalColor = true;
            colorArray[selectedVertex-1] = selectedColor;
            hints = new HintSystem(edges, colorArray);  // Update hints when uncoloring
        }
    }

    // Getter function for getting the color array
    public int[] getColorArray() {
        return colorArray;
    }

    public String getHint() {
        return hints.giveHints(2);
    }
}