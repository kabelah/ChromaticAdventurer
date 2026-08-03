package org.example.realphase2.Generation;

/** 
 * represents and edge in a graph that connects two vertices
 * Each edge is defined using two endpoints, u and v
 */
public class ColEdge {
    public int u;
    public int v;

    /**
     * Constructs an edge connecting two vertices 
     * 
     * @param u The first vertex of the edge
     * @param v the second vertex of the edge
     */
    public ColEdge(int u, int v) {
        this.u = u;
        this.v = v;
    }
}