package org.example.realphase2.Gamemodelogic;

import javafx.scene.paint.Color;
import org.example.realphase2.Generation.ColEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainGameLogic {
    public List<ColEdge> edges;
    public int numVertices;
    public int numEdges;
    public int gameMode;
    public int[] colorArray;

    public ToTheBitterEnd selectedGameModeTTBE;
    public RandomOrder selectedGameModeRM;
    public IChangedMyMind selectedGameModeICMM;

    private final Map<Color, Integer> colorIDMap = new HashMap<>();
    private int colorID = 1;

    public MainGameLogic() {
    }

    public void setValues(List<ColEdge> edges, int numVertices, int gameMode) {
        this.edges = edges;
        this.numVertices = numVertices;
        this.colorArray = new int[numVertices];
        this.numEdges = edges.size();
        this.gameMode = gameMode;

        if (gameMode == 1) {
            this.selectedGameModeTTBE = new ToTheBitterEnd(numVertices, numEdges, colorArray, edges);
        } else if (gameMode == 2) {
            this.selectedGameModeRM = new RandomOrder(numVertices, numEdges, colorArray, edges);
        } else if (gameMode == 3) {
            this.selectedGameModeICMM = new IChangedMyMind(numVertices, numEdges, colorArray, edges);
        } else {
            System.out.println("Error: Invalid Game Mode ID " + gameMode);
        }
    }

    public void tryToColor(String vertexString, Color color) {
        int vertexInt = Integer.parseInt(vertexString);
        colorIDMap.putIfAbsent(color, colorID++);
        int colorIDInt = colorIDMap.getOrDefault(color, 0);

        if (gameMode == 1) {
            this.colorArray = selectedGameModeTTBE.getColorArray();
            selectedGameModeTTBE.colorVertex(vertexInt, colorIDInt);
        } else if (gameMode == 2) {
            this.colorArray = selectedGameModeRM.getColorArray();
            selectedGameModeRM.colorVertex(vertexInt, colorIDInt);
        } else if (gameMode == 3) {
            this.colorArray = selectedGameModeICMM.getColorArray();
            selectedGameModeICMM.colorVertex(vertexInt, colorIDInt);
        } else {
            System.out.println("Error: Invalid Game Mode ID " + gameMode);
        }
    }

    public void unColor(String vertexString) {
        int vertexInt = Integer.parseInt(vertexString);

        if (gameMode == 1) {
            selectedGameModeTTBE.colorVertex(vertexInt, 0);
        }
        else if (gameMode == 3) {
            selectedGameModeICMM.colorVertex(vertexInt, 0);
        } else {
            System.out.println("Error: Invalid Game Mode ID " + gameMode);
        }
    }

    public boolean isGraphDone() {
        if (gameMode == 1) {
            return selectedGameModeTTBE.isGraphFullyColored();
        } else if (gameMode == 2) {
            return selectedGameModeRM.isGraphFullyColored();
        } else if (gameMode == 3) {
            return selectedGameModeICMM.isGraphFullyColored();
        } else {
            System.out.println("Error: Invalid Game Mode ID " + gameMode);
            return false;
        }
    }

    public ToTheBitterEnd getSelectedGameModeTTBE() {
        return selectedGameModeTTBE;
    }

    public RandomOrder getSelectedGameModeRM() {
        return selectedGameModeRM;
    }

    public IChangedMyMind getSelectedGameModeICMM() {
        return selectedGameModeICMM;
    }
}