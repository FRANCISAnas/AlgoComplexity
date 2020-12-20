package project.strategies;

import project.core.ColorBR;
import project.graph.Graph;
import project.core.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Heuristic1 implements Strategy {
    /** Amélioration de la Strat0 dans des cas particuliers d'égalité**/

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> vertices = graph.getVertices();
        Vertex toDelete = Utility.getFirstRedVertex(graph);
        if(toDelete==null) return null;
        List<Vertex> exaequo = new ArrayList<>();
        int score = toDelete.nOutgoingBlueEdgesToRed();
        for (Vertex v : vertices) {
            if (v.getColor() == ColorBR.RED) {
                int tmp = v.nOutgoingBlueEdgesToRed();
                if (tmp < score) {
                    exaequo.clear();
                    toDelete = v;
                    score = tmp;
                } else if (tmp == score)
                    exaequo.add(v);
            }
        }
        if(exaequo.isEmpty()) return toDelete;
        score = toDelete.nOutgoingBlueEdgesToBlue();
        for(Vertex v: exaequo){
            int tmp = v.nOutgoingBlueEdgesToBlue();
            if(tmp > score) {
                toDelete = v;
                score = tmp;
            }
        }
        return toDelete;
    }
}
