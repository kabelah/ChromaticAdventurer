package org.example.realphase2.Gamemodelogic;
import java.util.List;
import org.example.realphase2.Generation.ColEdge;

public class ToTheBitterEnd {
    int numVertices;
    int numEdges;
    int[] colorArray;
    List<ColEdge> edges;
    private HintSystem hints;

    // Constructor for To The Bitter End's game logic
    public ToTheBitterEnd(int numVertices, int numEdges, int[] colorArray, List<ColEdge> edges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.colorArray = colorArray;
        this.edges = edges;
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
            if (IsColorable.isColorable(colorArray, selectedVertex, selectedColor, edges)) {
                colorArray[selectedVertex-1] = selectedColor;
                hints = new HintSystem(edges, colorArray);
            } else {
                System.out.println("Illegal color. TTBE");
                FlagClass.illegalColor = true;
            }
        } else {
            colorArray[selectedVertex-1] = selectedColor;
            hints = new HintSystem(edges, colorArray);
        }
    }

    // Getter function for getting the color array
    public int[] getColorArray() {
        return colorArray;
    }

    public String getHint() {
        return hints.giveHints(1);
    }
}