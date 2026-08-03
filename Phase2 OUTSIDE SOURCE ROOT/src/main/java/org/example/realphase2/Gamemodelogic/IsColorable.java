package org.example.realphase2.Gamemodelogic;
import java.util.ArrayList;
import java.util.List;
import org.example.realphase2.Generation.ColEdge;


// Since I do not know how to import the ColEdge edges nor the completed graph, I cannot finish this part of the game logic.
// All the logic that I could make should still work.
public class IsColorable {
    public static boolean isColorable(int[] colorArray, int selectedVertex, int selectedColor, List<ColEdge> edges) {
        List<Integer> touchingVertices = new ArrayList<>();
        // This for loop find every vertex that touches selectedVertex and adds it to the list touchingVertices.
        for (ColEdge edge : edges) {
            if (edge.u == selectedVertex) {
                touchingVertices.add(edge.v);
            } else if (edge.v == selectedVertex) {
                touchingVertices.add(edge.u);
            }
        }

        // This for loop finds the color of each vertex in the list touchingVertices.
        // It returns false if a vertex is colored with the same color as selectedColor.
        for (int vertex : touchingVertices) {
            if (colorArray[vertex-1] == selectedColor) {
                return false;
            }
        }

        return true;
    }
}