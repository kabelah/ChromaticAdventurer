package org.example.realphase2.scoring;

import org.example.realphase2.ChromaticNumber.org.phase1.ObtainChromaticNumber;
import org.example.realphase2.Gamemodelogic.FlagClass;
import org.example.realphase2.Generation.ColEdge;

import java.util.ArrayList;
import java.util.List;


public class GetChromaticNumber {
    public List<ColEdge> edges;
    public int numVertices;
    public int numEdges;
    public String graphText;

    // Empty constructor because the variables for the class need to be set in a method
    public GetChromaticNumber () {
    }

    // This function sets the key game logic values and calls the contractor of the chosen game mode
    // and gives it values it requires
    public void start(List<ColEdge> edges, int numVertices) {
        this.edges = edges;
        this.numVertices = numVertices;
        this.numEdges = edges.size();

        // This creates that text inside one of the graph files
        // graphText will be treated as the text inside one of the graph files
        String graphText = "VERTICES = " + numVertices +"\nEDGES = " + edges.size() + "\n";
        for (ColEdge edge : edges) {
            graphText = graphText + edge.u + " " + edge.v + "\n";
        }
        this.graphText = graphText;


    }
    //This obtains the chromatic number and passes it to FlagClass.chromaticNumber
    public int getChromaticNumber() {
        ObtainChromaticNumber.start(graphText);
        return FlagClass.chromaticNumber;
    }
}