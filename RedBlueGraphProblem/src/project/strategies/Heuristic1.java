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
        if(toDelete==null)return null;
        List<Vertex> equals = new ArrayList<>();
        for (Vertex v : vertices) {
            if (v.getColor() == ColorBR.RED) {
                v.nbArcBleuSortantVersRouge();
                if (v.nbArcBleuSortantVersRouge < toDelete.nbArcBleuSortantVersRouge) {
                    toDelete = v;
                    equals.clear();
                } else if (v.nbArcBleuSortantVersRouge == toDelete.nbArcBleuSortantVersRouge) {
                    equals.add(v);
                    if(!equals.contains(toDelete))equals.add(toDelete);
                }
            }
        }
        if(equals.isEmpty())return toDelete;
        toDelete = equals.get(0);
        toDelete.nbArcBleuSortantVersBleu();
        for(Vertex vertex : equals){
            vertex.nbArcBleuSortantVersBleu();
            if(vertex.nbArcBleuSortantVersBleu>toDelete.nbArcBleuSortantVersBleu){
                toDelete = vertex;
            }
        }
        return toDelete;
    }
    
}
