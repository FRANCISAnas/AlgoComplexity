package project.strategies;

import project.graph.Graph;
import project.core.Vertex;

import java.util.List;

public class Heuristic2 implements Strategy {

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        Vertex toDelete = Utility.getFirstRedVertex(graph);
        if(toDelete==null) return null;
        int score = toDelete.calculateScore();
        for (Vertex v : redVertex) {
            int v_score = v.calculateScore();
            if(v_score > score){
                toDelete = v;
                score = v_score;
            }
        }
        return toDelete;
    }
}
