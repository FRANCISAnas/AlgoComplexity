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
        List<Vertex> sommets = graph.getVertices();
        Vertex sommetTODelete = Utility.getFirstRedVertex(graph);
        if(sommetTODelete==null)return null;
        List<Vertex> equals = new ArrayList<>();
        for (Vertex cuSomm : sommets) {
            if (cuSomm.getColor() == ColorBR.RED) {
                cuSomm.nbArcBleuSortantVersRouge();
                if (cuSomm.nbArcBleuSortantVersRouge < sommetTODelete.nbArcBleuSortantVersRouge) {
                    sommetTODelete = cuSomm;
                    equals.clear();
                } else if (cuSomm.nbArcBleuSortantVersRouge == sommetTODelete.nbArcBleuSortantVersRouge) {
                    equals.add(cuSomm);
                    if(!equals.contains(sommetTODelete))equals.add(sommetTODelete);
                }
            }
        }
        if(equals.isEmpty())return sommetTODelete;
        sommetTODelete = equals.get(0);
        sommetTODelete.nbArcBleuSortantVersBleu();
        for(Vertex vertex : equals){
            vertex.nbArcBleuSortantVersBleu();
            if(vertex.nbArcBleuSortantVersBleu>sommetTODelete.nbArcBleuSortantVersBleu){
                sommetTODelete = vertex;
            }
        }
        return sommetTODelete;
    }
    
}