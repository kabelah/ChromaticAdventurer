package org.example.phase3.GUI.components;

import javafx.scene.paint.Color;
import org.example.phase3.Generation.GraphDisplayManager;

import java.util.*;

// This class contains the code for the program to automatically color in a graph.
public class GraphPainter {


    // Keeps track of how many colors were used in the COLORLIST.
    static int colorTracker = 0;
    // List of 64 colors to be used in order when coloring a vertex a new color.
    // A new color will be randomly generated when all colors in this list are used.
    // DO NOT ADD DUPLICATE COLORS, LIGHTBLUE, OR BLACK TO THIS LIST!
    static final ArrayList<Color> COLORLIST = new ArrayList<>(List.of(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.WHITE, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PURPLE, Color.BROWN, Color.PINK, Color.GRAY, Color.LIGHTGREEN, Color.DARKBLUE, Color.DARKGREEN, Color.DARKRED, Color.BEIGE, Color.BLUEVIOLET, Color.ALICEBLUE, Color.ANTIQUEWHITE, Color.AQUA, Color.AQUAMARINE, Color.AZURE, Color.BURLYWOOD, Color.CADETBLUE, Color.CHARTREUSE, Color.CHOCOLATE, Color.CORAL, Color.CORNFLOWERBLUE, Color.CORNSILK, Color.LIGHTGOLDENRODYELLOW, Color.LIGHTCORAL, Color.CRIMSON, Color.OLIVE, Color.DARKGOLDENROD, Color.DARKSEAGREEN, Color.SEAGREEN, Color.SEASHELL, Color.LIGHTSEAGREEN, Color.DEEPPINK, Color.DEEPSKYBLUE, Color.FUCHSIA, Color.FORESTGREEN, Color.GOLD, Color.IVORY, Color.INDIGO, Color.INDIGO, Color.LAVENDER, Color.LAVENDERBLUSH, Color.LAWNGREEN, Color.HONEYDEW, Color.HOTPINK, Color.LIME, Color.LIMEGREEN, Color.LINEN, Color.DARKOLIVEGREEN ,Color.NAVY, Color.PEACHPUFF, Color.PERU, Color.SNOW, Color.YELLOWGREEN, Color.WHITESMOKE, Color.WHEAT, Color.OLIVEDRAB));
    // Keeps track of what colors were randomly generated. Colors in this list cannot be randomly generated.
    // Lightblue is removed because it is the uncolor color and black is removed since the vertex number is displayed in black.
    static ArrayList<Color> usedColors = new ArrayList<>(List.of(Color.LIGHTBLUE, Color.BLACK));
    static final ArrayList<Color> USEDCOLORSDEFUALT = new ArrayList<>(List.of(Color.LIGHTBLUE, Color.BLACK));
    static boolean validColorFound = false;


    // This method takes a colorArray and will color in the graph displayed on the screen
    // via GraphDisplayManager. It will also display a popup window with information
    // about the graph that was colored.
    public static void graphPainter(int[] colorArray, int chromaticNumber, String algorithmMessage) {
        // This array is used to keep track of the colored vertices after they have been
        // translated from integers to JavaFX Colors.
        Color[] translatedColorArray = new Color[colorArray.length];
        // This hash map will keep try of what integer ID corresponds to what JavaFX Color.
        HashMap<Integer, Color> colorMap = new HashMap<>();

        for (int i = 0; i < colorArray.length; i++) {
            // If the color ID has a color associated to it, that color will be added to the
            // translated color array in place of the ID number.
            // Otherwise, a new color will be associated to the number ID.
            if (colorMap.containsKey(colorArray[i])) {
                translatedColorArray[i] = colorMap.get(colorArray[i]);
            } else {
                colorMap.put(colorArray[i], getNewColor());
                translatedColorArray[i] = colorMap.get(colorArray[i]);
            }
        }

        // This for loop colors each vertex with the proper color it was assigned.
        for (int i = 0; i < colorArray.length; i++) {
            GraphDisplayManager.updateVertexColor(GraphDisplayManager.circle[i + 1], translatedColorArray[i]);
        }

        // Resets the usedColors list and the color tracker back to their defaults.
        usedColors.clear();
        usedColors.addAll(USEDCOLORSDEFUALT);
        colorTracker = 0;

        // This displays the message for the information about the graph.
        GraphDisplayManager.displayMessage(chromaticNumber, algorithmMessage);
    }


    // This method returns a new JavaFX Color to associate with one of the color IDs by
    // looking through the COLORLIST or by randomly generating a new color.
    private static Color getNewColor() {
        // If there are more colors left in the COLORLIST, then a color is
        // chosen and the next color in the list will be ready to be associated
        // with a new ID.
        // Otherwise, a new color will be randomly generated.
        if (colorTracker < COLORLIST.size()) {
            colorTracker++;
            return COLORLIST.get(colorTracker-1);
        } else {
            while (!validColorFound) {
                // Here a new color is randomly generated. If that color has already
                // been used in the graph, then a new color will be randomly generated.
                Random randomRGB = new Random();
                Color randomColor = Color.rgb(randomRGB.nextInt(256), randomRGB.nextInt(256), randomRGB.nextInt(256));
                if (!(COLORLIST.contains(randomColor) || usedColors.contains(randomColor))) {
                    usedColors.add(randomColor);
                    return randomColor;
                }
            }

        }
        return null;
    }
}