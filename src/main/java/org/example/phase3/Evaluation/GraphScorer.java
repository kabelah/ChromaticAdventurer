package org.example.phase3.Evaluation;

public class GraphScorer {

    private Graph currentGraph;

    public void setCurrentGraph(Graph graph) {
        this.currentGraph = graph;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public GraphType evaluateGraphType() {
        if (currentGraph == null) {
            System.out.println("[ERROR] No graph set.");
            return GraphType.UNKNOWN;
        }

        boolean connected = GraphTester.isConnected(currentGraph);
        boolean noCycle = GraphTester.hasNoCycles(currentGraph);
        boolean bip = GraphTester.isBipartite(currentGraph);
        
        GraphType result;
        
        // Order matters! Most specific and cheapest tests first
        if (!connected) {
            if (noCycle) {
                result = GraphType.FOREST;
            } else {
                result = GraphType.UNKNOWN;
            }
        }
        else if (GraphTester.isStar(currentGraph)) result = GraphType.STAR;
        else if (GraphTester.isCycle(currentGraph)) result = GraphType.CYCLE;
        else if (connected && noCycle) result = GraphType.TREE;
        else if (GraphTester.isComplete(currentGraph)) result = GraphType.COMPLETE;
        else if (GraphTester.isWheel(currentGraph)) result = GraphType.WHEEL;
        else if (bip) result = GraphType.BIPARTITE;
        else if (GraphTester.isPlanar(currentGraph)) result = GraphType.PLANAR;
        else result = GraphType.UNKNOWN;
        
        System.out.println("[RESULT] Graph classified as: " + result);
        return result;
    }
}